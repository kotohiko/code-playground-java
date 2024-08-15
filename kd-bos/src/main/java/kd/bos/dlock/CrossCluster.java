package kd.bos.dlock;

import kd.bos.instance.Instance;

/**
 * @author Jacob Suen
 * @since 13:36 8月 12, 2024
 */
public final class CrossCluster {

    private static final boolean supportMultiCluster = Boolean.getBoolean("bos.supportMultiCluster");
    private static final String crossClusterPath = System.getProperty("bos.crossClusterPath", "cross_cluster");

    public CrossCluster() {
    }

    public static String getClusterNameForPath() {
        return supportMultiCluster ? crossClusterPath : Instance.getClusterName();
    }
}