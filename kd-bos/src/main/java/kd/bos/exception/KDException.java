package kd.bos.exception;


import kd.sdk.annotation.SdkDeprecated;
import kd.sdk.annotation.SdkInternal;
import kd.sdk.annotation.SdkPublic;

import java.io.PrintWriter;
import java.io.Serial;
import java.io.StringWriter;

/**
 * @author Jacob Suen
 * @since 13:33 8月 12, 2024
 */
@SdkPublic
public class KDException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -6277095208290718410L;
    private static final Object[] empty_args = new Object[0];
    private static final ErrorCode empty_error_code = new ErrorCode("", "");
    protected ErrorCode errorCode;
    protected Object[] args;

    /**
     * @deprecated
     */
    @SdkInternal
    @SdkDeprecated
    @Deprecated
    public KDException(String message) {
        super(message);
        this.errorCode = empty_error_code;
    }

    public KDException(ErrorCode errorCode, String arg, Throwable cause) {
        this(cause, errorCode, arg);
    }

    public KDException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.args = args;
    }

    public KDException(Throwable cause, ErrorCode errorCode, Object... args) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.args = args;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public Object[] getArgs() {
        return this.args == null ? empty_args : this.args;
    }

    public String getStackTraceMessage() {
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        this.printStackTrace(writer);
        writer.flush();
        return sw.toString();
    }

    public String getMessage() {
        return this.errorCode != null && this.errorCode != empty_error_code ? this.format(this.errorCode.getMessage()) : super.getMessage();
    }

    private String format(String msg) {
        if (this.args != null && this.args.length != 0) {
            try {
                return String.format(msg, this.args);
            } catch (Exception var3) {
                return msg;
            }
        } else {
            return msg;
        }
    }
}