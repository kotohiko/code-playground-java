package kd.bos.instance;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

/**
 * @author Kingdee
 * @since 16:26 8月 12, 2024
 */
public class AppStore {

    public static final String APPSTORE_URL = "APPSTORE_URL";
    public static final String LIBSNAMEPATHMAP_KEY = "libs.name.pathmapping";
    public static final String LIBS = "libs";
    private static final String LOADAPPIDFROMAPPSTORE = "loadappidfromappstore";
    private final Set<String> cloudIds = new HashSet<>(2);
    private final AtomicBoolean inited = new AtomicBoolean(false);
    private Set<String> configAppidSet = new HashSet<>(1);
    private final Set<String> configInnerAppidSet = new HashSet<>(1);
    private static final Map<String, AppStore> instances = new ConcurrentHashMap<>(2);
    private final String region;

    private AppStore(String region) {
        this.region = region;
    }

    public static AppStore get() {
        return get("");
    }

    public static AppStore get(String region) {
        return region == null ? null : (AppStore) instances.computeIfAbsent(region, (k) -> {
            return new AppStore(region);
        });
    }

    protected Set<String> getConfiggedAppidsInAppStore() {
        this.init();
        return this.configAppidSet;
    }

    protected Set<String> getConfiggedInnerAppids() {
        this.init();
        return this.configInnerAppidSet;
    }

    private synchronized void init() {
        if (this.inited.compareAndSet(false, true)) {
            this.configAppidSet = this.initConfigInAppStore();
        }
    }

    private Set<String> initConfigInAppStore() {
        Set<String> configAppidSet = new HashSet<>(4);
        String appstore_url = this.getSystemProperties("APPSTORE_URL");
        if (appstore_url != null && !"false".equals(this.getSystemProperties("loadappidfromappstore"))) {
            if (!appstore_url.endsWith("/")) {
                appstore_url = appstore_url + "/";
            }

            String appstoreUrl = appstore_url;
            String libstr = this.getSystemProperties("libs");
            String[] libs;
            String[] var8;
            int var9;
            int var10;
            String libUri;
            if (libstr != null && libstr.trim().length() > 0) {
                String appidsFromEnvStr = this.getSystemProperties("appIds");
                String[] libs;
                int var19;
                String libUri;
                if (appidsFromEnvStr != null && appidsFromEnvStr.length() > 0) {
                    libs = appidsFromEnvStr.split(",|;");
                    libs = libs;
                    var19 = libs.length;

                    for (var9 = 0; var9 < var19; ++var9) {
                        libUri = libs[var9];
                        configAppidSet.add(libUri);
                    }

                    libs = libstr.split(",|;");
                    var8 = libs;
                    var9 = libs.length;

                    for (var10 = 0; var10 < var9; ++var10) {
                        libUri = var8[var10];
                        String libUrl = appstoreUrl + libUri;

                        try {
                            this.parseCloudLibFromUrl(libUrl, appstoreUrl, (Set) null, true);
                        } catch (Exception var14) {
                            this.praseAppstoreError(var14);
                        }
                    }

                    return configAppidSet;
                }

                libs = libstr.split(",|;");
                libs = libs;
                var19 = libs.length;

                for (var9 = 0; var9 < var19; ++var9) {
                    libUri = libs[var9];
                    libUri = appstoreUrl + libUri;

                    try {
                        this.parseCloudLibFromUrl(libUri, appstoreUrl, configAppidSet, false);
                    } catch (Exception var15) {
                        this.praseAppstoreError(var15);
                    }
                }

                if (this.cloudIds.size() > 0) {
                    Map<String, HashMultimap<String, String>> m = Cluster.getAppCloudMapping();
                    m.forEach((accountid, cloudappmapping) -> {
                        this.cloudIds.forEach((cloud) -> {
                            Set<String> appidsInCloud = cloudappmapping.get(cloud);
                            this.initCloudAppids(cloud, appidsInCloud, configAppidSet);
                        });
                    });
                }
            } else {
                Map<String, String> libsNamePathMap = getDefaultlibsNamePathMap();
                String libsNamePathMapStr = this.getSystemProperties("libs.name.pathmapping");
                if (libsNamePathMapStr != null) {
                    libs = libsNamePathMapStr.split(",");
                    var8 = libs;
                    var9 = libs.length;

                    for (var10 = 0; var10 < var9; ++var10) {
                        libUri = var8[var10];
                        String[] libsNamePathPair = libUri.split("=");
                        if (libsNamePathPair.length == 2) {
                            libsNamePathMap.put(libsNamePathPair[0], libsNamePathPair[1]);
                        }
                    }
                }

                libsNamePathMap.forEach((libsName, subPath) -> {
                    String libs = this.getSystemProperties(libsName);
                    if (libs != null) {
                        String[] var6 = libs.split(",");
                        int var7 = var6.length;

                        for (int var8 = 0; var8 < var7; ++var8) {
                            String lib = var6[var8];
                            if (lib.endsWith(".xml")) {
                                String xmlUrl = appstore_url + subPath + "/" + lib;

                                try {
                                    this.parseAppidFromXml(xmlUrl, configAppidSet, false);
                                } catch (Exception var12) {
                                    this.praseAppstoreError(var12);
                                }
                            }
                        }
                    }

                });
            }

            if (!configAppidSet.isEmpty()) {
                System.setProperty("appIdsFromAppStore", (String) configAppidSet.stream().collect(Collectors.joining(",")));
            }

            if (!this.configInnerAppidSet.isEmpty()) {
                System.setProperty("innerAppIdsFromAppStore", (String) this.configInnerAppidSet.stream().collect(Collectors.joining(",")));
            }

            return configAppidSet;
        } else {
            return configAppidSet;
        }
    }

    private void initCloudAppids(String cloud, Set<String> appidsInCloud, Set<String> configAppidSet) {
        if (appidsInCloud != null && !appidsInCloud.isEmpty()) {
            Set<String> appidsExcludedSet = this.getExcludeFromCloudSet();
            Set<String> appidsDeployInCloud = new HashSet(appidsInCloud);
            appidsDeployInCloud.removeAll(appidsExcludedSet);
            configAppidSet.addAll(appidsDeployInCloud);

            try {
                InstanceSenior.addCloud(cloud, appidsDeployInCloud);
                System.setProperty("cloud--" + cloud, InstanceSenior.getDeployedAppByCloud(cloud).toString());
            } catch (Error | Exception var7) {
                ExceptionUtils.parseException(var7);
            }

        } else {
            System.setProperty("cloud--" + cloud, "cloud not exists");
        }
    }

    private void praseAppstoreError(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        SafeLogUtils.error(AppStore.class, "getConfiggedAppidsInAppStoreError", e);
        if (this.getSystemProperties("appIds") == null && Boolean.parseBoolean(this.getSystemProperties("appSplit"))) {
            LockSupport.parkNanos(1000000000L);
            System.exit(1);
        }

    }

    private void parseCloudLibFromUrl(String libUrl, String appstoreUrl, Set<String> configAppidSet, boolean onlyInnerApppIds) throws ParserConfigurationException, IOException, SAXException {
        Consumer<Document> consumer = (document) -> {
            Element root = (Element) document.getElementsByTagName("root").item(0);
            if (root != null) {
                NodeList list = null;
                int i;
                Element ele;
                String innerAppids;
                int var12;
                int splitIndex;
                String subPath;
                if (!onlyInnerApppIds) {
                    list = root.getElementsByTagName("appIds");
                    if (list != null) {
                        for (i = 0; i < list.getLength(); ++i) {
                            ele = (Element) list.item(i);
                            innerAppids = ele.getFirstChild().getNodeValue();
                            Set<String> appidsExcludedSet = this.getExcludeFromCloudSet();
                            if (innerAppids != null) {
                                String[] var11 = innerAppids.split(",");
                                var12 = var11.length;

                                for (splitIndex = 0; splitIndex < var12; ++splitIndex) {
                                    subPath = var11[splitIndex];
                                    if (!appidsExcludedSet.contains(subPath)) {
                                        configAppidSet.add(subPath);
                                    }
                                }
                            }
                        }
                    }

                    list = root.getElementsByTagName("cloud");
                    if (list != null) {
                        for (i = 0; i < list.getLength(); ++i) {
                            ele = (Element) list.item(i);
                            innerAppids = ele.getFirstChild().getNodeValue();
                            this.parseConfigCloud(innerAppids);
                        }
                    }
                }

                list = root.getElementsByTagName("innerAppIds");
                if (list != null) {
                    for (i = 0; i < list.getLength(); ++i) {
                        ele = (Element) list.item(i);
                        innerAppids = ele.getFirstChild().getNodeValue();
                        if (innerAppids != null) {
                            String[] var24 = innerAppids.split(",");
                            int var26 = var24.length;

                            for (var12 = 0; var12 < var26; ++var12) {
                                String innerAppid = var24[var12];
                                this.configInnerAppidSet.add(innerAppid);
                            }
                        }
                    }
                }

                list = root.getElementsByTagName("libs");

                for (i = 0; i < list.getLength(); ++i) {
                    ele = (Element) list.item(i);
                    NodeList libList = ele.getElementsByTagName("lib");

                    for (int j = 0; j < libList.getLength(); ++j) {
                        Element eleJ = (Element) libList.item(j);
                        String libconfigs = eleJ.getFirstChild().getNodeValue();
                        splitIndex = libconfigs.indexOf(47);
                        if (splitIndex > 0) {
                            subPath = libconfigs.substring(0, splitIndex);
                            String libs = libconfigs.substring(splitIndex + 1);
                            String[] var16 = libs.split(",");
                            int var17 = var16.length;

                            for (String lib : var16) {
                                if (lib.endsWith(".xml")) {
                                    String xmlUrl = appstoreUrl + subPath + "/" + lib;

                                    try {
                                        this.parseAppidFromXml(xmlUrl, (Set) null, true);
                                    } catch (Exception var22) {
                                        SafeLogUtils.error(AppStore.class, "get inner appId Error," + xmlUrl, var22);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        };
        parseXml(libUrl, consumer);
    }

    private void initCloudAppids(String cloud, Set<String> appidsInCloud, Set<String> configAppidSet) {
        if (appidsInCloud != null && !appidsInCloud.isEmpty()) {
            Set<String> appidsExcludedSet = this.getExcludeFromCloudSet();
            Set<String> appidsDeployInCloud = new HashSet(appidsInCloud);
            appidsDeployInCloud.removeAll(appidsExcludedSet);
            configAppidSet.addAll(appidsDeployInCloud);

            try {
                InstanceSenior.addCloud(cloud, appidsDeployInCloud);
                System.setProperty("cloud--" + cloud, InstanceSenior.getDeployedAppByCloud(cloud).toString());
            } catch (Error | Exception var7) {
                ExceptionUtils.parseException(var7);
            }

        } else {
            System.setProperty("cloud--" + cloud, "cloud not exists");
        }
    }

    private void praseAppstoreError(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        pw.flush();
        SafeLogUtils.error(AppStore.class, "getConfiggedAppidsInAppStoreError", e);
        if (this.getSystemProperties("appIds") == null && Boolean.parseBoolean(this.getSystemProperties("appSplit"))) {
            LockSupport.parkNanos(1000000000L);
            System.exit(1);
        }

    }

    private void parseCloudLibFromUrl(String libUrl, String appstoreUrl, Set<String> configAppidSet, boolean onlyInnerApppIds) throws ParserConfigurationException, IOException, SAXException {
        Consumer<Document> consumer = (document) -> {
            Element root = (Element) document.getElementsByTagName("root").item(0);
            if (root != null) {
                NodeList list = null;
                int i;
                Element ele;
                String innerAppids;
                int var12;
                int splitIndex;
                String subPath;
                if (!onlyInnerApppIds) {
                    list = root.getElementsByTagName("appIds");
                    if (list != null) {
                        for (i = 0; i < list.getLength(); ++i) {
                            ele = (Element) list.item(i);
                            innerAppids = ele.getFirstChild().getNodeValue();
                            Set<String> appidsExcludedSet = this.getExcludeFromCloudSet();
                            if (innerAppids != null) {
                                String[] var11 = innerAppids.split(",");
                                var12 = var11.length;

                                for (splitIndex = 0; splitIndex < var12; ++splitIndex) {
                                    subPath = var11[splitIndex];
                                    if (!appidsExcludedSet.contains(subPath)) {
                                        configAppidSet.add(subPath);
                                    }
                                }
                            }
                        }
                    }

                    list = root.getElementsByTagName("cloud");
                    if (list != null) {
                        for (i = 0; i < list.getLength(); ++i) {
                            ele = (Element) list.item(i);
                            innerAppids = ele.getFirstChild().getNodeValue();
                            this.parseConfigCloud(innerAppids);
                        }
                    }
                }

                list = root.getElementsByTagName("innerAppIds");
                if (list != null) {
                    for (i = 0; i < list.getLength(); ++i) {
                        ele = (Element) list.item(i);
                        innerAppids = ele.getFirstChild().getNodeValue();
                        if (innerAppids != null) {
                            String[] var24 = innerAppids.split(",");
                            int var26 = var24.length;

                            for (var12 = 0; var12 < var26; ++var12) {
                                String innerAppid = var24[var12];
                                this.configInnerAppidSet.add(innerAppid);
                            }
                        }
                    }
                }

                list = root.getElementsByTagName("libs");

                for (i = 0; i < list.getLength(); ++i) {
                    ele = (Element) list.item(i);
                    NodeList libList = ele.getElementsByTagName("lib");

                    for (int j = 0; j < libList.getLength(); ++j) {
                        Element eleJ = (Element) libList.item(j);
                        String libconfigs = eleJ.getFirstChild().getNodeValue();
                        splitIndex = libconfigs.indexOf(47);
                        if (splitIndex > 0) {
                            subPath = libconfigs.substring(0, splitIndex);
                            String libs = libconfigs.substring(splitIndex + 1);
                            String[] var16 = libs.split(",");
                            int var17 = var16.length;

                            for (int var18 = 0; var18 < var17; ++var18) {
                                String lib = var16[var18];
                                if (lib.endsWith(".xml")) {
                                    String xmlUrl = appstoreUrl + subPath + "/" + lib;

                                    try {
                                        this.parseAppidFromXml(xmlUrl, (Set) null, true);
                                    } catch (Exception var22) {
                                        SafeLogUtils.error(AppStore.class, "get inner appId Error," + xmlUrl, var22);
                                    }
                                }
                            }
                        }
                    }
                }
            }

        };
        parseXml(libUrl, consumer);
    }
}