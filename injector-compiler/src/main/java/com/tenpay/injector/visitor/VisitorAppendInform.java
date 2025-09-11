package com.tenpay.injector.visitor;

import com.squareup.javapoet.ClassName;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Symbol;
import com.tenpay.injector.TBinding;
import com.tenpay.injector.base.BaseVisitor;
import com.tenpay.injector.base.ClsBean;
import com.tenpay.injector.base.RScanner;

import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;

/**
 * replace R2 to R in annotation, and generate XXXBinding.java file.
 * delanding
 */
public class VisitorAppendInform extends BaseVisitor {

    public VisitorAppendInform(Filer filer, Trees jcTrees, RScanner rScanner) {
        super(filer, jcTrees, rScanner);
    }

    /**
     * visitor for the filed annotation.
     * @param sets annotation element in class.
     * @param clsMap class map for the R inner class fileds, such as R.id.xxx etc.
     */
    public void visit(Set<? extends Element> sets, Map<String, ClsBean> clsMap) {
        ClassName viewCls = ClassName.get("android.view", "View");
        for (Element element : sets) {
            if (element instanceof Symbol.VarSymbol) {
                Symbol owner = ((Symbol.VarSymbol) element).owner;

                String clsName = owner.getSimpleName().toString();
                String clsPkgName = owner.getQualifiedName().subName(0,
                        owner.getQualifiedName().length() - clsName.length() - 1).toString();

                int id = element.getAnnotation(TBinding.class).value();
                Map<String, String> attrs = getEleAnnotationAttr(element, TBinding.class, id);
                if (attrs.size() > 0) {
                    Object jobj = elementToId(element, TBinding.class, id).get(0);
                    String annoName = ((Symbol.ClassSymbol) ((Symbol.VarSymbol) jobj).owner).fullname.toString();
                    String pkgName = annoName.contains("R2") ? annoName.substring(0, annoName.lastIndexOf("R2") - 1)
                            : annoName.substring(0, annoName.lastIndexOf("R") - 1);

                    ClassName rCls = ClassName.get(pkgName, "R");
                    String genClsName = String.format("%s.%s", clsPkgName, clsName + "Binding");
                    String realRID = attrs.get("value")
                            .replace("R2.", "")
                            .replace("R.", "");

                    if (attrs.containsKey("parent") && attrs.containsKey("visiable")) {
                        String visiableFeild = attrs.get("visiable").replace("View.", "");
                        String parentFeild = attrs.get("parent").replace("R2", "R");
                        clsMap.get(genClsName).con
                                .addStatement("injector($S, $T.$N, $T.$N, new int[]$N)",
                                        element.getSimpleName(), rCls, realRID, viewCls, visiableFeild, parentFeild);
                    } else if (attrs.containsKey("visiable")) {
                        String visiableFeild = attrs.containsKey("visiable")
                                ? attrs.get("visiable").replace("View.", "") : "VISIBLE";
                        clsMap.get(genClsName).con
                                .addStatement("injector($S, $T.$N, $T.$N)",
                                        element.getSimpleName(), rCls, realRID, viewCls, visiableFeild);
                    } else if (attrs.containsKey("parent")) {
                        String parentFeild = attrs.get("parent").replace("R2", "R");
                        clsMap.get(genClsName).con
                                .addStatement("injector($S, $T.$N, $N, new int[]$N)",
                                        element.getSimpleName(), rCls, realRID, "-1", parentFeild);
                    } else {
                        clsMap.get(genClsName).con
                                .addStatement("injector($S, $T.$N)",
                                        element.getSimpleName(), rCls, realRID);
                    }
                }
            }
        }
    }

}
