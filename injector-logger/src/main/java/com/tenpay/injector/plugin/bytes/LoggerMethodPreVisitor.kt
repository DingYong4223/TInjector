package com.tenpay.injector.plugin.bytes

import com.tenpay.injector.TLogger
import com.tenpay.injector.TLoggerImpl
import com.tenpay.injector.TLoggerSkip
import com.tenpay.injector.plugin.bytes.Utils.getClazzDesc
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

/**
 * Annotation get by the visitor, only special annotation class will be processed.
 * Created by delanding on 15/11/2020.
 */
class LoggerMethodPreVisitor(
    private val methodName: String,
    private val methodKey: String,
    private val methodParametersMap: MutableMap<String, List<ByteBean>>,
    mv: MethodVisitor?,
    needParameter: Boolean,
    methodCollector: LoggerClassPreVisitor.MethodCollector
) : MethodVisitor(Opcodes.ASM5, mv), Opcodes {

    private val mByteBeans: MutableList<ByteBean> = ArrayList()
    private var needParameter = false
    private val labelList: MutableList<Label> = ArrayList()
    private val methodCollector: LoggerClassPreVisitor.MethodCollector
    private var useImpl = false

    /**
     * annotation visitor.
     * */
    override fun visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor {
        val defaultAv = super.visitAnnotation(desc, visible)
        when (desc) {
            getClazzDesc(TLoggerSkip::class.java) -> {
                needParameter = false
            }
            getClazzDesc(TLoggerImpl::class.java) -> {
                needParameter = true
                useImpl = true
            }
            getClazzDesc(TLogger::class.java) -> {
                needParameter = true
            }
        }
        return defaultAv
    }

    /**
     * local variable visitor.
     * */
    override fun visitLocalVariable(
        name: String,
        desc: String,
        signature: String?,
        start: Label,
        end: Label,
        index: Int
    ) {
        if ("this" != name && start === labelList[0] && needParameter) {
            val type = Type.getType(desc)
            if (type.sort == Type.OBJECT || type.sort == Type.ARRAY) {
                mByteBeans.add(ByteBean(name, getClazzDesc(Any::class.java), index))
            } else {
                mByteBeans.add(ByteBean(name, desc, index))
            }
        }
        super.visitLocalVariable(name, desc, signature, start, end, index)
    }

    override fun visitEnd() {
        if (needParameter) {
            methodCollector.onIncludeMethod(methodName, useImpl)
        }
        methodParametersMap[methodKey] = mByteBeans
        super.visitEnd()
    }

    override fun visitLabel(label: Label) {
        labelList.add(label)
        super.visitLabel(label)
    }

    init {
        this.needParameter = needParameter
        this.methodCollector = methodCollector
    }
}
