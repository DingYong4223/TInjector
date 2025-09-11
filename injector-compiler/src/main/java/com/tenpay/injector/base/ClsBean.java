package com.tenpay.injector.base;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class ClsBean {

    public MethodSpec.Builder con;
    public TypeSpec.Builder cls;

    public ClsBean(TypeSpec.Builder cls, MethodSpec.Builder con) {
        this.con = con;
        this.cls = cls;
    }

}
