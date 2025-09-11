package com.tenpay.injector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

/**
 * Report annotation
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR}) @Retention(RetentionPolicy.CLASS)
public @interface TReporter {
    /**
     * report key string
     * */
    String value();

    /**
     * report arguments
     * */
    String[] args() default {};
}