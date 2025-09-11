package com.tenpay.anno;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Used for proguard keeping.
 * */
@Target(value = {FIELD, METHOD})
@Retention(value = RUNTIME)
public @interface TKeeping {

    /**
     * description for the method or field.
     * */
    String value() default "";
}
