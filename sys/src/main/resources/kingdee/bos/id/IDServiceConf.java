package kingdee.bos.id;

import com.jacob.sys.snowflake.kingdee.bos.bundle.Resources;
import com.jacob.sys.snowflake.kingdee.bos.lock.DLockProvider;

/**
 * @author Kingdee
 * @since 13:29 8月 12, 2024
 */
class IDServiceConf {

    private static final int MAX_WORKER_COUNT;
    private static final String NODE_NAME_LOCK = "k";
    private static final String NODE_NAME_VALUE = "v";
    private static final String NODE_NAME_WORKERLIST = "w";
    private static final String NODE_NAME_WORKERTIME = "t";
    private static final String LONG_KEY_NODE_PATH = "l";
    private static final String INT_KEY_NODE_PATH = "i";
    private IDClockBackStrategy clockBackStrategy;
    private DLockProvider lockProvider;
    private String server;
    private int sessionReconnectCount;
    private int sessionTimeout;
    private int logStatusInterval;
    private String rootPath;
    private int groupWorkers;
    private int intIdMinValue;
    private int batchGenIntSize;

    static IDServiceConf createDefault() {
        return new IDServiceConf();
    }

    private IDServiceConf() {
        this.clockBackStrategy = IDClockBackStrategy.abandon_then_restart;
        this.lockProvider = DLockProvider.apache_curator_framework;
        this.server = "localhost:2181";
        this.rootPath = "/";
        this.sessionReconnectCount = 3;
        this.sessionTimeout = 5000;
        this.logStatusInterval = 3600000;
        this.groupWorkers = 1;
        this.intIdMinValue = 100001;
        this.batchGenIntSize = 100;
    }

    DLockProvider getLockProvider() {
        return this.lockProvider;
    }

    void setLockProvider(DLockProvider lockProvider) {
        this.lockProvider = lockProvider;
    }

    int getSessionTimeout() {
        return this.sessionTimeout;
    }

    void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    int getSessionReconnectCount() {
        return this.sessionReconnectCount;
    }

    void setSessionReconnectCount(int sessionReconnectCount) {
        this.sessionReconnectCount = sessionReconnectCount;
    }

    int getBatchGenIntSize() {
        return this.batchGenIntSize;
    }

    void setBatchGenIntSize(int batchGenIntSize) {
        this.batchGenIntSize = batchGenIntSize;
    }

    int getGroupWorkers() {
        return this.groupWorkers;
    }

    void setGroupWorkers(int groupWorkers) {
        if (groupWorkers >= 1 && groupWorkers <= this.getMaxWorkerCount()) {
            this.groupWorkers = groupWorkers;
        } else {
            throw new IllegalArgumentException("Workers " + groupWorkers + Resources.getString("bos-id", "IDServiceConf_0", new Object[0]) + this.getMaxWorkerCount());
        }
    }

    String getRootPath() {
        return this.rootPath;
    }

    void setRootPath(String rootPath) {
        if (rootPath != null && rootPath.length() != 0) {
            if (!rootPath.endsWith("/")) {
                rootPath = rootPath + "/";
            }
        } else {
            rootPath = "/";
        }

        this.rootPath = rootPath;
    }

    String getServer() {
        return this.server;
    }

    void setServer(String server) {
        this.server = server;
    }

    int getLogStatusInterval() {
        return this.logStatusInterval;
    }

    void setLogStatusInterval(int logStatusInterval) {
        this.logStatusInterval = logStatusInterval;
    }

    int getMaxWorkerCount() {
        return MAX_WORKER_COUNT;
    }

    int getIntIdMinValue() {
        return this.intIdMinValue;
    }

    void setIntIdMinValue(int intIdMinValue) {
        this.intIdMinValue = intIdMinValue;
    }

    IDClockBackStrategy getClockBackStrategy() {
        return this.clockBackStrategy;
    }

    void setClockBackStrategy(IDClockBackStrategy clockBackStrategy) {
        this.clockBackStrategy = clockBackStrategy;
    }

    String getLongLockPath() {
        return this.rootPath + "l" + "/" + "k";
    }

    String getLongWorkerListPath() {
        return this.rootPath + "l" + "/" + "w";
    }

    String getLongWorkerTimePath() {
        return this.rootPath + "l" + "/" + "t";
    }

    String getLongWorkerTimePath(int wokerId) {
        return this.rootPath + "l" + "/" + "t" + "/" + wokerId;
    }

    String getIntLockPath(String tenantId, String tablename) {
        return this.rootPath + "i" + "/" + tenantId.toLowerCase() + "/" + tablename.toLowerCase() + "/" + "k";
    }

    String getIntValuePath(String tenantId, String tablename) {
        return this.rootPath + "i" + "/" + tenantId.toLowerCase() + "/" + tablename.toLowerCase() + "/" + "v";
    }

    String getStatus() {
        StringBuilder ss = new StringBuilder(256);
        ss.append("    rootPath=").append(this.rootPath);
        ss.append("\r\n    sessionTimeout=").append(this.sessionTimeout).append("ms");
        ss.append("\r\n    sessionReconnectCount=").append(this.sessionReconnectCount);
        ss.append("\r\n    groupWorkers=").append(this.groupWorkers);
        ss.append("\r\n    logStatusInterval=").append(this.logStatusInterval).append("ms");
        ss.append("\r\n    clockBackStrategy=").append(this.clockBackStrategy);
        ss.append("\r\n    lockProvider=").append(this.lockProvider);
        return ss.toString();
    }

    static {
        MAX_WORKER_COUNT = IDGenner.default_max_worker_id + 1;
    }
}