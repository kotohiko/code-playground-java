package com.jacob.snowflake.kingdee.bos.id;

import com.jacob.snowflake.kingdee.bos.bundle.Resources;

/**
 * @author Kingdee
 * @since 15:34 Aug 01, 2024
 */
public class IDServiceConf2ZK {

    public static final String idService_store_zookeeper = "IDService.store.zookeeper";
    public static final String idService_store_path = "IDService.store.path";
    public static final String idService_worker = "IDService.worker";
    public static final String idService_support_different_zk_clusters_enable
            = "IDService.support.different.zk.clusters.enable";
    public static final String idService_cluster_number = "IDService.cluster.number";
    public static final String idService_cluster_number_bits = "IDService.cluster.number.bits";
    public static final int default_idService_cluster_number_bits = 3;
    public static final int min_idService_cluster_number_bits = 1;
    public static final int max_idService_cluster_number_bits = 5;

    IDServiceConf2ZK() {
    }

    private static String getRootPath() {
        return "/" + CrossCluster.getClusterNameForPath() + "/runtime/ids";
    }

    public static IDServiceConf conf() {
        IDServiceConf conf = IDServiceConf.createDefault();
        String zookeeper = System.getProperty("IDService.store.zookeeper");
        if (zookeeper == null) {
            zookeeper = System.getProperty("configUrl");
        }

        conf.setServer(zookeeper);
        String rootPath = null;
        if (CrossCluster.getClusterNameForPath() != null) {
            rootPath = getRootPath();
        } else {
            rootPath = System.getProperty("IDService.store.path");
            if (rootPath == null || rootPath.isEmpty()) {
                throw new IllegalArgumentException(Resources.getString("bos-id", "IDServiceConf2ZK_0")
                        + "IDService.store.zookeeper" + "。");
            }
        }

        String zkroot = ZKFactory.getZkRootPath(zookeeper);
        if (rootPath.startsWith("/")) {
            rootPath = zkroot + rootPath.substring(1);
        } else {
            rootPath = zkroot + rootPath;
        }

        conf.setRootPath(rootPath);
        String worker = System.getProperty("IDService.worker", "0");
        int workers = 0;
        if (worker != null && worker.matches("\\d+")) {
            workers = Integer.parseInt(worker);
        }

        if (workers > 0) {
            conf.setGroupWorkers(workers);
        }

        return conf;
    }

    public static boolean isEnableDifferentCluster() {
        return Boolean.getBoolean("IDService.support.different.zk.clusters.enable");
    }

    public static int getClusterNumberBits() {
        int clusterNumberBits = Integer.getInteger("IDService.cluster.number.bits", 3);
        if (clusterNumberBits >= 1 && clusterNumberBits <= 5) {
            return clusterNumberBits;
        } else {
            throw new IllegalStateException(String.format(Resources.getString("bos-id", "IDServiceConf2ZK_2"),
                    clusterNumberBits, "[1,5]"));
        }
    }

    public static int getClusterNumber() {
        Integer clusterNumber = Integer.getInteger("IDService.cluster.number");
        int minNumber = 0;
        int maxNumber = (1 << getClusterNumberBits()) - 1;
        if (clusterNumber != null && clusterNumber >= minNumber && clusterNumber <= maxNumber) {
            return clusterNumber;
        } else {
            throw new IllegalStateException(String.format(Resources.getString("bos-id", "IDServiceConf2ZK_1"),
                    System.getProperty("IDService.cluster.number"), "[" + minNumber + "," + maxNumber + "]"));
        }
    }
}