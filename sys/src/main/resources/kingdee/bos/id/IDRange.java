package kingdee.bos.id;

/**
 * @author Kingdee
 * @since 15:24 Aug 01, 2024
 */
public final class IDRange {

    private final long minId;
    private final long maxId;

    public IDRange(long minId, long maxId) {
        this.minId = minId;
        this.maxId = maxId;
    }

    public long getMinId() {
        return minId;
    }

    public long getMaxId() {
        return maxId;
    }

    @Override
    public String toString() {
        return this.minId + ":" + this.maxId;
    }
}