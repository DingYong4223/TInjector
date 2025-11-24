package com.tenpay.injector.base;

//import com.sun.tools.javac.code.Symbol;
//import com.sun.tools.javac.code.Type;
//import com.sun.tools.javac.tree.JCTree;
//import com.sun.tools.javac.tree.TreeScanner;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

/**
 * R class scanner.
 * delanding
 * */
public class RScanner/* extends TreeScanner */{
    List<Object> mResIds = new ArrayList<>();

//    /**
//     * JCTree.JCIdent object visit processing
//     * @see JCTree.JCIdent
//     * */
//    @Override public void visitIdent(JCTree.JCIdent jcIdent) {
//        super.visitIdent(jcIdent);
//        Symbol symbol = jcIdent.sym;
//        if (symbol.type instanceof Type.JCPrimitiveType) {
//            //int value = (Integer) requireNonNull(((Symbol.VarSymbol) symbol).getConstantValue());
//            mResIds.add(symbol);
//        }
//    }
//
//    /**
//     * JCTree.JCFieldAccess object visit processing
//     * @see JCTree.JCFieldAccess
//     * */
//    @Override public void visitSelect(JCTree.JCFieldAccess jcFieldAccess) {
//        Symbol symbol = jcFieldAccess.sym;
//        //int value = (Integer) requireNonNull(((Symbol.VarSymbol) symbol).getConstantValue());
//        mResIds.add(symbol);
//    }
//
//    /**
//     * JCTree.JCLiteral object visit processing
//     * @see JCTree.JCLiteral
//     * */
//    @Override public void visitLiteral(JCTree.JCLiteral jcLiteral) {
//        try {
//            //int value = (Integer) jcLiteral.value;
//            mResIds.add(jcLiteral);
//        } catch (Exception ignored) {
//            out.println("RScanner visitLiteral error " + ignored.getMessage());
//        }
//    }
//
//    void reset() {
//        mResIds.clear();
//    }
}
