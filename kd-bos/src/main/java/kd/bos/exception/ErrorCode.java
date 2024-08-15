package kd.bos.exception;

import kd.sdk.annotation.SdkDeprecated;
import kd.sdk.annotation.SdkInternal;
import kd.sdk.annotation.SdkPublic;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Jacob Suen
 * @since 13:34 8月 12, 2024
 */
@SdkPublic
public final class ErrorCode implements Serializable {

    @Serial
    private static final long serialVersionUID = -6928905891033233175L;
    private final String code;
    private final String message;
    private LangMessage langMessage;

    /**
     * @deprecated
     */
    @SdkInternal
    @SdkDeprecated
    @Deprecated
    public static ErrorCode of(String errorCode, ResourceMessageGetter resourceMessageGetter) {
        return new ErrorCode(errorCode, resourceMessageGetter);
    }

    /**
     * @deprecated
     */
    @SdkInternal
    @SdkDeprecated
    @Deprecated
    public static ErrorCode of(String errorCode, String project, String key, String desc) {
        ErrorCode result = new ErrorCode(errorCode, desc);
        result.langMessage = new LangMessage(project, key, desc);
        return result;
    }

    private ErrorCode(String errorCode, ResourceMessageGetter resourceMessageGetter) {
        this.code = errorCode;
        this.message = errorCode;
    }

    public ErrorCode(String errorCode, String message) {
        this.code = errorCode;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public LangMessage getLangMessage() {
        return this.langMessage;
    }

    @SdkInternal
    public String toString() {
        return this.code;
    }

    @SdkInternal
    public int hashCode() {
        return this.code.hashCode();
    }

    @SdkInternal
    public boolean equals(Object obj) {
        return obj instanceof ErrorCode ? this.code.equals(((ErrorCode) obj).code) : false;
    }

    @SdkPublic
    public static class LangMessage implements Serializable {
        private final String project;
        private final String key;
        private final String desc;

        private LangMessage(String project, String key, String desc) {
            this.project = project;
            this.key = key;
            this.desc = desc;
        }

        public String getProject() {
            return this.project;
        }

        public String getKey() {
            return this.key;
        }

        public String getDesc() {
            return this.desc;
        }

        @SdkInternal
        public String toString() {
            return "LangMessage{project='" + this.project + '\'' + ", key='" + this.key + '\'' + ", desc='" + this.desc + '\'' + '}';
        }
    }

    /**
     * @deprecated
     */
    @SdkDeprecated
    @SdkInternal
    @Deprecated
    interface ResourceMessageGetter extends Serializable {
        String get();
    }
}