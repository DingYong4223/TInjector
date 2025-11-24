package com.tenpay.injector;

import com.sun.source.util.Trees;
import com.tenpay.injector.base.ClsBean;
import com.tenpay.injector.base.RScanner;
import com.tenpay.injector.visitor.VisitorAppendInform;
import com.tenpay.injector.visitor.VisitorFinisher;
import com.tenpay.injector.visitor.VisitorInitor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import static java.lang.System.out;

/**
 * Injector entry class.
 * When java file compile, the process function will be called, and you can process yourself logic.
 * delanding
 * */
public class InjectorProcessor extends AbstractProcessor {

    private Filer filer;
    private Trees jcTrees;
//    private final RScanner rScanner = new RScanner();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(TBinding.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        filer = env.getFiler();
        try {
            jcTrees = Trees.instance(env);
        } catch (IllegalArgumentException ignored) {
            try {
                for (Field field : processingEnv.getClass().getDeclaredFields()) {
                    if (field.getName().equals("delegate") || field.getName().equals("processingEnv")) {
                        field.setAccessible(true);
                        ProcessingEnvironment javacEnv = (ProcessingEnvironment) field.get(processingEnv);
                        jcTrees = Trees.instance(javacEnv);
                        break;
                    }
                }
            } catch (Throwable ignored2) {
                out.println("InjectorProcessor init error " + ignored2.getMessage());
            }
        }
        //Context context = ((JavacProcessingEnvironment) env).getContext();
    }

    /**
     * Processing entry point.
     * */
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        out.println("start process tenpay injector...");
        Set<? extends Element> sets = roundEnv.getElementsAnnotatedWith(TBinding.class);
        Map<String, ClsBean> clsMap = new HashMap<>();
//        //new VisitorClsSuper(filer, jcTrees, rScanner).visit(clsMap);
//        new VisitorInitor(filer, jcTrees, rScanner).visit(sets, clsMap);
//        //new VisitorAppendInfo(filer, jcTrees, rScanner).visit(sets, clsMap);
//        new VisitorAppendInform(filer, jcTrees, rScanner).visit(sets, clsMap);
//        new VisitorFinisher(filer, jcTrees, rScanner).visit(sets, clsMap);
        return true;
    }

}



