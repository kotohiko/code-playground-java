package kingdee.bos.id;

import com.jacob.sys.snowflake.kingdee.bos.bundle.Resources;

/**
 * @author Jacob Suen
 * @since 14:45 8月 12, 2024
 */
public enum IDClockBackStrategy {

    abandon_then_restart(Resources.getString("bos-id", "IDClockBackStrategy_0", new Object[0])),
    ignore_then_continue(Resources.getString("bos-id", "IDClockBackStrategy_1", new Object[0])),
    throw_exception(Resources.getString("bos-id", "IDClockBackStrategy_2", new Object[0]));

    private String desc;

    private IDClockBackStrategy(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }
}