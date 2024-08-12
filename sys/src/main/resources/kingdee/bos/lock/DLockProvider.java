package kingdee.bos.lock;

import com.jacob.sys.snowflake.kingdee.bos.bundle.Resources;
import com.jacob.sys.snowflake.kingdee.bos.exception.KDException;

/**
 * @author Kingdee
 * @since 13:31 8月 12, 2024
 */
public enum DLockProvider {

    apache_curator_framework,
    apache_zookeeper;

    private DLockProvider() {
    }

    public static DLock createDLock(DLockProvider lockProvider, String zkServer,
                                    int dlockSessionTimeout, DLockListener dl) throws KDException {
        switch (lockProvider) {
            case apache_curator_framework:
                return new CuratorLocker(zkServer, dlockSessionTimeout, dl);
            default:
                throw new KDException(new IllegalArgumentException(Resources
                        .getString("bos-id", "DLockProvider_0", new Object[0]) + lockProvider),
                        BosErrorCode.bosId, new Object[0]);
        }
    }
}