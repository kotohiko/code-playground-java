package kd.bos.lock;

import kd.bos.exception.KDException;

import java.util.function.Function;

/**
 * @author Kingdee
 * @since 16:32 8月 12, 2024
 */
public interface DLockListener {
    <T> T retryOnDisconnected(Function<DLock, T> var1) throws KDException;
}