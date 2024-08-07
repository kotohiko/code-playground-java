package com.jacob.snowflake.kingdee.sdk.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SdkPublic {
    String scriptName() default "";
}