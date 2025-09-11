package com.tenpay.injector.visitor;

import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Symbol;
import com.tenpay.injector.TBinding;
import com.tenpay.injector.base.BaseVisitor;
import com.tenpay.injector.base.ClsBean;
import com.tenpay.injector.base.RScanner;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;

/**
 * Visit append information in annotation.
 * delanding
 */
public class VisitorAppendInfo extends BaseVisitor {

    public VisitorAppendInfo(Filer filer, Trees jcTrees, RScanner rScanner) {
        super(filer, jcTrees, rScanner);
    }

    /**
     * visitor process
     */
    public void visit(Set<? extends Element> sets, Map<String, ClsBean> clsMap) {
        for (Element element : sets) {
            if (element instanceof Symbol.VarSymbol) {
                Symbol owner = ((Symbol.VarSymbol) element).owner;

                String clsName = owner.getSimpleName().toString();
                String clsPkgName = owner.getQualifiedName().subName(0,
                        owner.getQualifiedName().length() - clsName.length() - 1).toString();

                int id = element.getAnnotation(TBinding.class).value();
                List<Object> elemets = elementToId(element, TBinding.class, id);
                if (elemets.size() > 0) {
                    Object obj = elemets.get(0);
                    //String findId = null;
                    if (obj instanceof Symbol.VarSymbol) {
                        String annoName = ((Symbol.ClassSymbol) ((Symbol.VarSymbol) obj).owner).fullname.toString();
                        String pkgName = annoName.contains("R2") ? annoName.substring(0,
                                annoName.lastIndexOf("R2") - 1) : clsPkgName;
                        //findId = String.format("%s.R.id.%s", pkgName, obj.toString());

                        int visiable = 0x00000000;
                        StringBuffer relys = new StringBuffer();
                        for (int i = 1; i < elemets.size(); i++) {
                            Object rely = elemets.get(i);
                            if (rely instanceof Symbol.VarSymbol) {
                                relys.append(String.format(relys.length() <= 0 ? "%s.R.id.%s" : ", %s.R.id.%s",
                                        pkgName, rely.toString()));
                            }
                        }

                        String genClsName = String.format("%s.%s", clsPkgName, clsName + "Binding");
                        clsMap.get(genClsName).con.addStatement("injector($S, $N, new int[]{$N}, $N)",
                                element.getSimpleName(), String.format("%s.R.id.%s", pkgName, obj.toString()),
                                relys.toString(), "0");
                    }
                }
            }
        }
    }

}
