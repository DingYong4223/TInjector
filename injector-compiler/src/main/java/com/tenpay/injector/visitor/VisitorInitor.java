package com.tenpay.injector.visitor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.sun.source.util.Trees;
//import com.sun.tools.javac.code.Symbol;
import com.tenpay.injector.Configer;
import com.tenpay.injector.base.BaseVisitor;
import com.tenpay.injector.base.ClsBean;
import com.tenpay.injector.base.RScanner;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * 为activity生成 对应的bind文件
 */
public class VisitorInitor extends BaseVisitor {

    public VisitorInitor(Filer filer, Trees jcTrees, RScanner rScanner) {
        super(filer, jcTrees, rScanner);
    }

    /**
     * 为activity生成 对应的bind文件
     */
    public void visit(Set<? extends Element> sets, Map<String, ClsBean> clsMap) {
        for (Element element : sets) {
//            if (element instanceof Symbol.VarSymbol) {
//                Symbol owner = ((Symbol.VarSymbol) element).owner;
//                String clsName = owner.getSimpleName().toString();
//                String pkgName = owner.getQualifiedName().subName(0,
//                        owner.getQualifiedName().length() - clsName.length() - 1).toString();
//
//                ClassName activity = ClassName.get("android.app", "Activity");
//                MethodSpec.Builder conBuilder = MethodSpec.constructorBuilder()
//                        .addModifiers(Modifier.PUBLIC)
//                        .addParameter(activity, "activity")
//                        .addStatement("super($N)", "activity");
//
//                ClassName superCls = ClassName.get(Configer.BASEPKG, Configer.BASENAME);
//                TypeSpec.Builder clsBuilder = TypeSpec
//                        .classBuilder(clsName + "Binding")
//                        .superclass(superCls)
//                        .addModifiers(Modifier.PUBLIC);
//                String genClsName = String.format("%s.%s", pkgName, clsName + "Binding");
//                clsMap.put(genClsName, new ClsBean(clsBuilder, conBuilder));
//            }
        }
    }

}
