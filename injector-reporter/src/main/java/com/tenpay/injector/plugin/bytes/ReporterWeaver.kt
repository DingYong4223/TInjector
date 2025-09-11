package com.tenpay.injector.plugin.bytes

import com.tenpay.injector.plugin.asm.BaseWeaver
import com.tenpay.injector.plugin.asm.ExtClassWriter
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.IOException
import java.io.InputStream

/**
 * Created by delanding on 15/11/2020.
 */
class ReporterWeaver : BaseWeaver() {

    override fun setExtension(extension: Any?) {}

    @Throws(IOException::class)
    override fun weaveSingleClassToByteArray(inputStream: InputStream): ByteArray {
        val classReader = ClassReader(inputStream)
        var classWriter: ClassWriter = ExtClassWriter(classLoader, ClassWriter.COMPUTE_MAXS)
        val lcpv = ReporterClassPreVisitor(classWriter)
        classReader.accept(lcpv, ClassReader.EXPAND_FRAMES)
        if (lcpv.isNeedParameter) {
            classWriter = ExtClassWriter(classLoader, ClassWriter.COMPUTE_MAXS)
            val loggerClassVisitor = ReporterClassVisitor(classWriter, lcpv.methodParamsMap)
            loggerClassVisitor.attachIncludeMethodsAndImplMethods(lcpv.includes, lcpv.impls)
            classReader.accept(loggerClassVisitor, ClassReader.EXPAND_FRAMES)
        }
        return classWriter.toByteArray()
    }

    override fun isWeavableClass(fullQualifiedClassName: String): Boolean {
        val superResult = super.isWeavableClass(fullQualifiedClassName)
        val isByteCodePlugin = fullQualifiedClassName.startsWith(INJECTORLIB)
        return superResult && !isByteCodePlugin
    }

    companion object {
        private const val INJECTORLIB = "com.tenpay.injector"
    }
}
