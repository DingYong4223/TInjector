package com.tenpay.injector.plugin.bytes

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * Injector plugin entry.
 * Created by delanding on 15/11/2020.
 */
class LoggerClassVisitor internal constructor(
    cv: ClassVisitor?,
    private val methodParametersMap: Map<String, List<ByteBean>>
) : ClassVisitor(Opcodes.ASM5, cv) {

    private var className: String? = null
    private val includeMethods: MutableList<String> = ArrayList()
    private val implMethods: MutableList<String> = ArrayList()
    fun attachIncludeMethodsAndImplMethods(
        includeMethods: List<String>,
        implMethods: List<String>
    ) {
        this.includeMethods.addAll(includeMethods)
        this.implMethods.addAll(implMethods)
    }

    /**
     * class visitor when asm read a .class file.
     * */
    override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String,
        interfaces: Array<String>
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        className = name
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
    ): MethodVisitor {
        val mv = cv.visitMethod(access, name, desc, signature, exceptions)
        if (includeMethods.contains(name)) {
            val methodUniqueKey = name + desc
            val temp = LoggerMethodSorter(
                className!!,
                methodParametersMap[methodUniqueKey]!!,
                name,
                access,
                desc,
                mv
            )
            if (implMethods.contains(name)) {
                temp.switchToLoggerImpl()
            }
            return temp
        }
        return mv
    }
}
