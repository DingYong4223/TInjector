package com.tenpay.injector.plugin.bytes

import com.tenpay.injector.TLoggerClass
import com.tenpay.injector.plugin.bytes.Utils.getClazzDesc
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * Injector plugin entry.
 * Created by delanding on 15/11/2020.
 */
class ReporterClassPreVisitor(cv: ClassVisitor?) : ClassVisitor(Opcodes.ASM5, cv) {

    val methodParamsMap: MutableMap<String, List<ByteBean>> = HashMap()
    //private var mLoggerMethodPreVisitor: LoggerMethodPreVisitor? = null
    var isNeedParameter = false
        private set
    private var classLogger = false
    val includes: MutableList<String> = ArrayList()
    val impls: MutableList<String> = ArrayList()
    override fun visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor {
        val orgin = super.visitAnnotation(desc, visible)
        if (getClazzDesc(TLoggerClass::class.java) == desc) {
            classLogger = true
        }
        return orgin
    }

    /**
     * method visitor when class was processed, we can get the class name, signature etc.
     * */
    override fun visitMethod(
        access: Int,
        name: String,
        desc: String,
        signature: String?,
        exceptions: Array<String>?
    ): MethodVisitor? {
        println("visitMethod name: " + name + ", desc: " + desc + ", signature: " + signature + "exception: " + exceptions)
        val mv = cv.visitMethod(access, name, desc, signature, exceptions)
        val methodUniqueKey = name + desc
        return if (mv == null) null else ReporterMethodPreVisitor(
                name,
                methodUniqueKey,
                methodParamsMap,
                mv,
                classLogger,
                object : MethodCollector {
                    override fun onIncludeMethod(methodName: String, useImpl: Boolean) {
                        if (useImpl) {
                            impls.add(methodName)
                        }
                        includes.add(methodName)
                        isNeedParameter = true
                    }
                })
    }

    interface MethodCollector {
        fun onIncludeMethod(methodName: String, useImpl: Boolean)
    }
}
