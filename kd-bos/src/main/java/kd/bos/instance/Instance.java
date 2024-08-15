package kd.bos.instance;

import kd.bos.bundle.Resources;
import kd.bos.util.SystemProperties;

import java.security.SecureRandom;
import java.util.Set;

/**
 * @author Kingdee
 * @since 14:35 Aug 12, 2024
 */
public class Instance {

    public static final String KEY_CLUSTERNAME = "clusterName";
    public static final String KEY_DATACENTERNAME = "dataCenterName";
    public static final int INSTANCE_ID_LENGTH_MAX = 70;
    public static final String KEY_APPNAME = "appName";
    public static final String MSERVICE_NAME = "mservice";
    public static final String KEY_CONFIGAPPNAME = "configAppName";
    public static final String KEY_APPSPLIT = "appSplit";
    public static final String KEY_WEBMSERVICEINONE = "webmserviceinone";
    public static final String KEY_LIGHTWEIGHTDEPLOY = "lightweightdeploy";
    public static final String KEY_APPIDS = "appIds";
    public static final String KEY_DEPLOYEDAPPIDS = "deployedAppIds";
    public static final String KEY_INNERAPPIDS = "innerAppIds";
    public static final String KEY_HASBOSINNERAPPIDS = "defalutBosInnerAppId.enable";
    public static final String KEY_EXCLUEFROMCLOUD = "appIds.excludeFromCloudNode";
    public static final String KEY_CUSTOMAPPIDS = "customAppIds";
    public static final String KEY_REGISTEDAPPIDS = "registedAppIds";
    public static final String KEY_DEBUG_APP_NAME = "debug";
    public static final String KEY_CLOUD_PRE_TAG = "cloud--";
    private static final String clusterName = SystemProperties.getWithEnv("clusterName");
    private static final String appName;
    private static final String[] configAppNames;
    private static final boolean appSplit;
    private static final boolean webMserviceInOne;
    private static final boolean lightWeightDeploy;
    private static final String instanceId;
    protected static String evnAppIds;
    protected static String evnInnerAppIds;
    protected static String evnDeployAppIds;
    private static final boolean pausedServiceByMonitor;

    public Instance() {
    }

    public static String getClusterName() {
        return clusterName;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static String getDataCenterName() {
        throw new UnsupportedOperationException(Resources.getString("bos-util", "Instance_0", new Object[0]));
    }

    public static String[] parseAppIds(String str) {
        return str == null ? null : str.split("[,;]");
    }

    public static String getAppName() {
        return appName;
    }

    public static String[] getConfigAppName() {
        return configAppNames;
    }

    public static String getInstanceId() {
        return instanceId;
    }

    public static String[] getAppIds() {
        return InstanceSenior.getAppIds();
    }

    public static void addAppIds(String appId) {
        InstanceSenior.addAppIds(appId);
    }

    public static String[] getDeployedAppIds() {
        return InstanceSenior.getDeployedAppIds();
    }

    public static String[] getInnerAppIds() {
        return InstanceSenior.getInnerAppIds();
    }

    public static boolean isAppSplit() {
        return appSplit;
    }

    public static boolean isWebMserviceInOne() {
        return webMserviceInOne;
    }

    public static boolean isStandaloneWebNode() {
        return !webMserviceInOne && Boolean.getBoolean("isWebNode");
    }

    public static boolean isLightWeightDeploy() {
        return lightWeightDeploy;
    }

    private static String createInstanceId(String appName) {
        return appName + "-" + (new SecureRandom()).nextInt(9) + System.currentTimeMillis() % 1000000000L;
    }

    public static String getAppNameFromInstanceId(String instanceId) {
        if (instanceId == null) {
            return "NON";
        } else {
            int tagIndex = instanceId.lastIndexOf("-");
            return instanceId.substring(0, tagIndex);
        }
    }

    public static boolean isPausedServiceByMonitor() {
        return pausedServiceByMonitor;
    }

    public static boolean isDebugInstance() {
        return InstanceSenior.isDebugInstance();
    }

    public static Set<String> getDeployedClouds() {
        return InstanceSenior.getDeployedClouds();
    }

    public static String getDeployedCloudByApp(String appId) {
        return InstanceSenior.getDeployedCloudByApp(appId);
    }

    static {
        if (clusterName == null) {
            throw new Error("clusterName not configured.");
        } else {
            appName = SystemProperties.getWithEnv("appName");
            if (appName == null) {
                throw new Error("appName not configured.");
            } else {
                String configAppName = SystemProperties.getWithEnv("configAppName");
                if (configAppName == null) {
                    configAppName = appName;
                }

                configAppNames = configAppName.split(",");
                appSplit = Boolean.parseBoolean(SystemProperties.getWithEnv("appSplit"));
                webMserviceInOne = Boolean.parseBoolean(SystemProperties.getWithEnv("webmserviceinone"));
                lightWeightDeploy = Boolean.parseBoolean(SystemProperties.getWithEnv("lightweightdeploy"));
                evnAppIds = SystemProperties.getWithEnv("appIds");
                evnInnerAppIds = SystemProperties.getWithEnv("innerAppIds");
                evnDeployAppIds = SystemProperties.getWithEnv("deployedAppIds");
                instanceId = createInstanceId(getAppName());
                pausedServiceByMonitor = false;
            }
        }
    }
}