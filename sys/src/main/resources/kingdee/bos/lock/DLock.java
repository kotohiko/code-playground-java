package kingdee.bos.lock;

import com.jacob.sys.snowflake.kingdee.bos.exception.KDException;

import java.util.List;

/**
 * @author Kingdee
 * @since 16:24 8月 12, 2024
 */
public interface DLock extends AutoCloseable {

    <T> T lock(String var1, DLockHandler<T> var2) throws KDException;

    <T> void lockAsync(String var1, DLockHandler<T> var2) throws KDException;

    String get(String var1) throws KDException;

    List<String> getChildren(String var1) throws KDException;

    void set(String var1, String var2) throws KDException;

    void setEphemeral(String var1, String var2) throws KDException;

    void del(String var1) throws KDException;
}