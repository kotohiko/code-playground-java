package kingdee.bos.instance;

import com.jacob.sys.snowflake.kingdee.bos.util.SystemProperties;

import java.util.*;

/**
 * @author Kingdee
 * @since 14:40 8月 12, 2024
 */
public class InstanceSenior {

    private static String[] appIds = initAppIds();
    private static final String[] deployedAppIds = initDeployedAppIds();
    private static String[] innerAppIds = initInnerAppIds();
    private static boolean debugInstance = false;
    private static final Set<String> clouds = new HashSet<>(2);
    private static final Map<String, String> appCloudMapping = new HashMap<>(2);

    public InstanceSenior() {
    }

    private static String[] initDeployedAppIds() {
        String str = Instance.evnDeployAppIds;
        return str != null ? Instance.parseAppIds(str) : null;
    }

    private static String[] initInnerAppIds() {
        String str = Instance.evnInnerAppIds;
        HashSet set;
        if (str != null) {
            set = new HashSet(Arrays.asList(Instance.parseAppIds(str)));
        } else {
            set = new HashSet(1);
        }

        if (!"false".equals(SystemProperties.getWithEnv("defalutBosInnerAppId.enable"))) {
            set.add("bos");
        }

        set.addAll(AppStore.get().getConfiggedInnerAppids());
        return (String[]) set.toArray(new String[0]);
    }

    private static String[] initAppIds() {
        Object appidSet;
        try {
            appidSet = AppStore.get().getConfiggedAppidsInAppStore();
        } catch (Throwable var7) {
            appidSet = new HashSet();
        }

        String str = Instance.evnAppIds;
        if (str != null) {
            String[] ids = Instance.parseAppIds(str);
            String[] var3 = ids;
            int var4 = ids.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String id = var3[var5];
                ((Set) appidSet).add(id);
            }
        }

        return ((Set) appidSet).isEmpty() ? null : (String[]) ((Set) appidSet).toArray(new String[0]);
    }

    protected static String[] getAppIds() {
        return appIds;
    }

    protected static void addAppIds(String appId) {
        if (appId != null && appId.length() > 0) {
            if (appIds != null && appIds.length > 0) {
                String[] newAppIds = Arrays.copyOf(appIds, appIds.length + 1);
                newAppIds[newAppIds.length - 1] = appId;
                appIds = newAppIds;
            } else {
                appIds = new String[]{appId};
            }
        }

    }

    protected static String[] getDeployedAppIds() {
        return deployedAppIds;
    }

    protected static String[] getInnerAppIds() {
        return innerAppIds;
    }

    protected static boolean isDebugInstance() {
        return debugInstance;
    }

    protected static Set<String> getDeployedClouds() {
        return Collections.unmodifiableSet(clouds);
    }

    protected static void addCloud(String cloud, Set<String> appidsDeployInCloud) {
        if (cloud != null) {
            clouds.add(cloud);
            appidsDeployInCloud.forEach((appid) -> {
                appCloudMapping.put(appid, cloud);
            });
        }

    }

    protected static String getDeployedCloudByApp(String appId) {
        return (String) appCloudMapping.get(appId);
    }

    protected static Set<String> getDeployedAppByCloud(String cloud) {
        Set<String> s = new HashSet(2);
        appCloudMapping.forEach((app, cloudid) -> {
            if (cloudid.equals(cloud)) {
                s.add(app);
            }

        });
        return s;
    }

    static {
        int var2;
        if (InstanceSenior.appIds != null && InstanceSenior.appIds.length > 0) {
            String[] var0 = InstanceSenior.appIds;
            int var1 = var0.length;

            for (var2 = 0; var2 < var1; ++var2) {
                String id = var0[var2];
                if ("debug".equals(id)) {
                    debugInstance = true;
                    break;
                }
            }
        }

        if (!debugInstance && innerAppIds != null && innerAppIds.length > 0) {
            List<String> appIds = new ArrayList(innerAppIds.length);
            String[] var6 = innerAppIds;
            var2 = var6.length;

            for (int var7 = 0; var7 < var2; ++var7) {
                String id = var6[var7];
                if (!"debug".equals(id)) {
                    appIds.add(id);
                }
            }

            if (appIds.size() != innerAppIds.length) {
                innerAppIds = appIds.toArray(new String[appIds.size()]);
            }
        }
    }
}