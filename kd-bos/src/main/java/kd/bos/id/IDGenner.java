package kd.bos.id;

import kd.bos.bundle.Resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 金蝶苍穹 bos 库中的雪花算法
 *
 * @author Kingdee
 * @since 18:56 Jul 31, 2024
 */
public class IDGenner {

    public static final int default_max_worker_id = getMaxWorkerId(13);
    private static final int MIN_TIME_BITS = 39;
    private static final int DEFAULT_WORKER_BITS = 13;
    private static final int DEFAULT_SEQUENCE_BITS = 10;
    private static final String DEFAULT_EPOCH_DATE = "2017-01-01";
    private static final int MAX_WORKER_SEQUENCE_BITS = 24;
    private final SimpleDateFormat sdf_date;
    private final SimpleDateFormat sdf_ts;
    private final int workerBits;
    private final int sequenceBits;
    private final int timeBits;
    private final int clusterNumberBits;
    private final long epoch;
    private final int workerId;
    private final int shiftTime;
    private final int shiftWorker;
    private final int sequenceMask;
    private final long tolerantClockBackTimestamp;
    private final String desc;
    private int sequence;
    private long lastTimestamp;

    public IDGenner(int workerId, long tolerantClockBackTimestamp) {
        this(workerId, tolerantClockBackTimestamp, null);
    }

    public IDGenner(int workerId, long tolerantClockBackTimestamp, String desc) {
        this(workerId, tolerantClockBackTimestamp, 13, 10, "2017-01-01", desc);
    }

    protected IDGenner(int workerId, long tolerantClockBackTimestamp, int workerBits, int sequenceBits,
                       String epochDate, String startupDesc) {
        this.sdf_date = new SimpleDateFormat("yyyy-MM-dd");
        this.sdf_ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        this.sequence = 0;
        this.lastTimestamp = -1L;
        this.tolerantClockBackTimestamp = tolerantClockBackTimestamp;
        this.workerId = workerId;
        int maxWorkerId = getMaxWorkerId(workerBits);
        if (this.workerId >= 0 && this.workerId <= maxWorkerId) {
            this.workerBits = workerBits;
            this.clusterNumberBits = IDServiceConf2ZK.getClusterNumberBits();
            this.sequenceBits = sequenceBits;
            this.shiftTime = workerBits + sequenceBits;
            this.shiftWorker = sequenceBits;
            this.sequenceMask = ~(-1 << sequenceBits);
            this.timeBits = 63 - workerBits - sequenceBits;
            if (this.timeBits < 39) {
                throw new IllegalArgumentException(this.logPrefix()
                        + Resources.getString("bos-id", "IDGenner_1", new Object[0]) + 39
                        + Resources.getString("bos-id", "IDGenner_2", new Object[0]) + this.timeBits
                        + Resources.getString("bos-id", "IDGenner_3", new Object[0]) + 24 + ".");
            } else {
                try {
                    this.epoch = this.sdf_date.parse(epochDate).getTime();
                } catch (ParseException var10) {
                    throw new IllegalArgumentException(Resources.getString("bos-id", "IDGenner_4"), var10);
                }

                this.desc = "[" + this + "]epoch:" + this.sdf_date.format(this.epoch);
                IDServiceLog.debug(this.desc);
            }
        } else {
            throw new IllegalArgumentException(this.logPrefix()
                    + Resources.getString("bos-id", "IDGenner_0")
                    + maxWorkerId);
        }
    }

    public static int getMaxWorkerId(int workerBits) {
        if (IDServiceConf2ZK.isEnableDifferentCluster()) {
            int clusterNumberBits = IDServiceConf2ZK.getClusterNumberBits();
            return (1 << workerBits - clusterNumberBits) - 1;
        } else {
            return (1 << workerBits) - 1;
        }
    }

    private String logPrefix() {
        return "[workerId:" + this.workerId + "] ";
    }

    private long createId(long timestamp) {
        if (IDServiceConf2ZK.isEnableDifferentCluster()) {
            int clusterNumber = IDServiceConf2ZK.getClusterNumber();
            return timestamp - this.epoch << this.shiftTime | (long) this.workerId << this.shiftWorker
                    + this.clusterNumberBits | (long) clusterNumber << this.shiftWorker | (long) this.sequence;
        } else {
            return timestamp - this.epoch << this.shiftTime | (long) this.workerId << this.shiftWorker
                    | (long) this.sequence;
        }
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        timestamp = this.checkClockBackwards(timestamp);
        if (this.lastTimestamp == timestamp) {
            this.sequence = this.sequence + 1 & this.sequenceMask;
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else if (this.sequence != 0) {
            this.sequence = 0;
        }

        this.lastTimestamp = timestamp;
        return this.createId(timestamp);
    }

    private long checkClockBackwards(long timestamp) {
        if (timestamp < this.lastTimestamp) {
            boolean fixedByWait = true;
            long offset = this.lastTimestamp - timestamp;
            if (offset <= this.tolerantClockBackTimestamp && this.tolerantClockBackTimestamp > 0L) {
                try {
                    this.wait(this.tolerantClockBackTimestamp << 1);
                } catch (InterruptedException var7) {
                }

                timestamp = this.timeGen();
                if (timestamp < this.lastTimestamp) {
                    fixedByWait = false;
                }
            }

            if (!fixedByWait) {
                throw new IllegalStateException(this.logPrefix() + Resources.getString("bos-id",
                        "IDGenner_5", new Object[0]) + offset + Resources.getString("bos-id",
                        "IDGenner_6", new Object[0]) + this.sdf_ts.format(timestamp) + "。");
            }
        }

        return timestamp;
    }

    protected long tilNextMillis(long lastTimestamp) {
        long timestamp;
        for (timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {
        }

        return timestamp;
    }

    protected long timeGen() {
        return System.currentTimeMillis();
    }

    public int getWorkerId() {
        return this.workerId;
    }

    public String getDescription() {
        return this.desc;
    }

    public String toString() {
        return this.getClass().getSimpleName() + "-" + this.workerId;
    }

    public Date getCreateTime(long id) {
        return new Date((id >> this.workerBits + this.sequenceBits) + this.epoch);
    }

    public IDRange getIDRangeOfDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long min;
        if (IDServiceConf2ZK.isEnableDifferentCluster()) {
            int clusterNumber = IDServiceConf2ZK.getClusterNumber();
            min = c.getTimeInMillis() - this.epoch << this.shiftTime | (long) clusterNumber << this.shiftWorker;
        } else {
            min = c.getTimeInMillis() - this.epoch << this.shiftTime;
        }

        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        long max;
        if (IDServiceConf2ZK.isEnableDifferentCluster()) {
            int clusterNumber = IDServiceConf2ZK.getClusterNumber();
            max = c.getTimeInMillis() - this.epoch << this.shiftTime | (long) default_max_worker_id << this.shiftWorker +
                    this.clusterNumberBits | (long) clusterNumber << this.shiftWorker | (long) this.sequenceMask;
        } else {
            max = c.getTimeInMillis() - this.epoch << this.shiftTime | (long) default_max_worker_id << this.shiftWorker
                    | (long) this.sequenceMask;
        }

        return new IDRange(min, max);
    }
}