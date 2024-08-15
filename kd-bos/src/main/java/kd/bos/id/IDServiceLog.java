package kd.bos.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kingdee
 * @since 15:38 Aug 01, 2024
 */
final class IDServiceLog {

    private static final Logger logger = LoggerFactory.getLogger("kd.bos.id.IDService");

    IDServiceLog() {
    }

    public static void debug(String msg) {
        logger.info(msg);
    }
}