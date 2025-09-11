package com.tenpay.injector.visitor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.sun.source.util.Trees;
import com.tenpay.injector.Configer;
import com.tenpay.injector.base.BaseVisitor;
import com.tenpay.injector.base.ClsBean;
import com.tenpay.injector.base.RScanner;
import com.tenpay.injector.base.UtilFileGen;

import java.lang.reflect.Field;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

/**
 * Super class generator for XXXBinding.java.
 * delanding
 * */
class VisitorClsSuper extends BaseVisitor {

    private ClassName mViewCls = ClassName.get("android.view", "View");
    private ClassName mNsExeption = ClassName.get("java.util", "NoSuchElementException");

    public VisitorClsSuper(Filer filer, Trees jcTrees, RScanner rScanner) {
        super(filer, jcTrees, rScanner);
    }

    /**visit entry*/
    public void visit(Map<String, ClsBean> clsMap) {
        ClassName activity = ClassName.get("android.app", "Activity");
        MethodSpec.Builder conBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(activity, "activity")
                .addStatement("this.$N = $N", "mActivity", "activity");

        FieldSpec filed = FieldSpec.builder(activity, "mActivity")
                .addModifiers(Modifier.PRIVATE)
                .build();
        TypeSpec.Builder clsBuilder = TypeSpec
                .classBuilder(Configer.BASENAME)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.ABSTRACT)
                .addField(filed)
                .addMethod(conBuilder.build())
                .addMethod(genInjector())
                .addMethod(genOverride2Injector())
                .addMethod(genOverrideInjector());
        UtilFileGen.writeToDisk(JavaFile.builder(Configer.BASEPKG, clsBuilder.build()).build(), mFiler);
    }

    /**
     * Generate function injector.
     * */
    private MethodSpec genInjector() {
        return MethodSpec.methodBuilder("injector")
                .addModifiers(Modifier.PROTECTED)
                .addParameter(String.class, "fildName")
                .addParameter(int.class, "id")
                .addParameter(int.class, "visiable")
                .addParameter(int[].class, "rely")
                .addStatement(CodeBlock.builder()
                        .add("Class cls = mActivity.getClass()")
                        .build())
                .beginControlFlow("try")
                .addStatement("$T filed = cls.getDeclaredField(fildName)", Field.class)
                .addStatement("filed.setAccessible(true)")

                .beginControlFlow("if (rely.length > 0)")

                .addStatement("$T parent = mActivity.findViewById(rely[0])", mViewCls)

                .beginControlFlow("for (int i = 1; i < rely.length; i++)")
                .beginControlFlow("if (parent == null)")
                .addStatement("throw new $T($S + rely[i - 1])", mNsExeption, "no id found: ")
                .endControlFlow()

                .addStatement("parent = parent.findViewById(rely[i])")
                .endControlFlow()
                .beginControlFlow("if (parent == null)")
                .addStatement("throw new $T($S + rely[rely.length - 1])", mNsExeption, "no id found: ")
                .endControlFlow()
                .addStatement("$T view = parent.findViewById(id)", mViewCls)
                .beginControlFlow("if (visiable >= 0 && view.getVisibility() != visiable)")
                .addStatement("view.setVisibility(visiable)")
                .endControlFlow()
                .addStatement("filed.set(mActivity, view)")

                .nextControlFlow("else")
                .addStatement("$T view = mActivity.findViewById(id)", mViewCls)
                .beginControlFlow("if (visiable >= 0 && view.getVisibility() != visiable)")
                .addStatement("view.setVisibility(visiable)")
                .endControlFlow()
                .addStatement("filed.set(mActivity, view)")
                .endControlFlow()

                .nextControlFlow("catch ($T e)", NoSuchFieldException.class)
                .addStatement("e.printStackTrace()")
                .nextControlFlow("catch ($T e)", IllegalAccessException.class)
                .addStatement("e.printStackTrace()")
                .endControlFlow()
                .build();
    }

    /**
     * Generate base class' injector function.
     * */
    private MethodSpec genOverrideInjector() {
        return MethodSpec.methodBuilder("injector")
                .addModifiers(Modifier.PROTECTED)
                .addParameter(String.class, "fildName")
                .addParameter(int.class, "id")
                .addStatement(CodeBlock.builder()
                        .add("injector(fildName, id, -1, new int[]{})")
                        .build())
                .build();
    }

    /**
     * Generate base class' injector override function.
     * */
    private MethodSpec genOverride2Injector() {
        return MethodSpec.methodBuilder("injector")
                .addModifiers(Modifier.PROTECTED)
                .addParameter(String.class, "fildName")
                .addParameter(int.class, "id")
                .addParameter(int.class, "visiable")
                .addStatement(CodeBlock.builder()
                        .add("injector(fildName, id, visiable, new int[]{})")
                        .build())
                .build();
    }

}
