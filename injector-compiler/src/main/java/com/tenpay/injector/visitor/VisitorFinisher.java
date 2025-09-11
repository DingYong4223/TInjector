package com.tenpay.injector.visitor;

import com.squareup.javapoet.JavaFile;
import com.sun.source.util.Trees;
import com.tenpay.injector.base.BaseVisitor;
import com.tenpay.injector.base.ClsBean;
import com.tenpay.injector.base.RScanner;
import com.tenpay.injector.base.UtilFileGen;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;

public class VisitorFinisher extends BaseVisitor {

    public VisitorFinisher(Filer filer, Trees jcTrees, RScanner rScanner) {
        super(filer, jcTrees, rScanner);
    }

    public void visit(Set<? extends Element> sets, Map<String, ClsBean> clsMap) {
        for (String key: clsMap.keySet()) {
            ClsBean clsBean = clsMap.get(key);
            clsBean.cls.addMethod(clsBean.con.build());
            String pkg = key.substring(0, key.lastIndexOf("."));
            UtilFileGen.writeToDisk(JavaFile.builder(pkg, clsBean.cls.build()).build(), mFiler);
        }
    }

}
