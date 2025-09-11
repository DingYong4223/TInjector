package com.tenpay.injector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TLogger annotation
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR}) @Retention(RetentionPolicy.CLASS)
public @interface TLogger {
}