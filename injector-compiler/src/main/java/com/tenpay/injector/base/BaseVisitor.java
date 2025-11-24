package com.tenpay.injector.base;

import com.sun.source.util.Trees;
//import com.sun.tools.javac.tree.JCTree;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

/**
 * Base visitor for class. All Visitor should extends this class.
 * delanding
 * */
public abstract class BaseVisitor {

    /**
     * filter for the class.
     * */
    protected Filer mFiler;

    /**
     * class' tree
     * */
    protected Trees mJcTrees;

    /**
     * R class scanner, scan the android project's R.java file which generated automatically.
     * */
    protected RScanner mScanner;

    public BaseVisitor(Filer filer, Trees jcTrees, RScanner rScanner) {
        this.mFiler = filer;
        this.mJcTrees = jcTrees;
        this.mScanner = rScanner;
    }

    /**
     * parse the java file name by element and annotation.
     * */
    protected List<Object> elementToId(Element element, Class<? extends Annotation> annotation, int value) {
//        JCTree tree = (JCTree) mJcTrees.getTree(element, getMirror(element, annotation));
//        if (tree != null) { // tree can be null if the references are compiled types and not source
//            mScanner.reset();
//            tree.accept(mScanner);
//            if (!mScanner.mResIds.isEmpty()) {
//                return mScanner.mResIds;
//            }
//        }
        return null;
    }

    private static AnnotationMirror getMirror(Element element,
                                              Class<? extends Annotation> annotation) {
        for (AnnotationMirror annotationMirror : element.getAnnotationMirrors()) {
            if (annotationMirror.getAnnotationType().toString().equals(annotation.getCanonicalName())) {
                return annotationMirror;
            }
        }
        return null;
    }

    protected Map<String, String> getEleAnnotationAttr(Element element,
            Class<? extends Annotation> annotation, int value) {
//        JCTree tree = (JCTree) mJcTrees.getTree(element, getMirror(element, annotation));
        // tree can be null if the references are compiled types and not source
        Map<String, String> map = new HashMap<>();
//        if (tree instanceof JCTree.JCAnnotation) {
//            JCTree.JCAnnotation jja = (JCTree.JCAnnotation)tree;
//            for (JCTree.JCExpression item: jja.args) {
//                if (item instanceof JCTree.JCAssign) {
//                    JCTree.JCAssign assign = (JCTree.JCAssign)item;
//                    map.put(((JCTree.JCIdent)assign.lhs).name.toString(), assign.rhs.toString());
//                }
//            }
//        }
        return map;
    }


}
