package com.tenpay.injector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
public @interface TBinding {
    /**
     * View related View ID.
     */
    int value();

    /**
     * View view's parent ID.
     */
    int[] parent() default {};

    /**
     * view view's default visiable state.
     */
    int visiable() default 0x00000000;

    /**
     * TextView's default text show.
     */
    String text() default "";
}
