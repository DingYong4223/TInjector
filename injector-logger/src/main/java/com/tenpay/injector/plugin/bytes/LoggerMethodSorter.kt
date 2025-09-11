package com.tenpay.injector.plugin.bytes

import com.tenpay.injector.plugin.bytes.Utils.getLoadOpcodeFromDesc
import com.tenpay.injector.plugin.bytes.Utils.getLoadOpcodeFromType
import com.tenpay.injector.plugin.bytes.Utils.getStoreOpcodeFromType
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.LocalVariablesSorter

/**
 * Injector plugin entry.
 * Created by delanding on 15/11/2020.
 */
class LoggerMethodSorter(
    className: String,
    byteBeans: List<ByteBean>,
    name: String,
    access: Int,
    desc: String,
    mv: MethodVisitor?
) : LocalVariablesSorter(Opcodes.ASM5, access, desc, mv), Opcodes {
    private val mByteBeans: List<ByteBean>
    private var className: String? = null
    private val methodName: String
    private var logMethod = true
    private var logHook = false
    private var timingStartVarIndex = 0
    private val methodDesc: String
    fun switchToLoggerImpl() {
        logMethod = false
        logHook = true
    }

    /**
     * inject new code to the specified .class method. here we inject TMLogger.
     * */
    override fun visitCode() {
        super.visitCode()
        if (!logMethod && !logHook) return
        val qwlogger = newLocal(Type.getObjectType("com/tenpay/injector/TMLogger"))
        mv.visitTypeInsn(Opcodes.NEW, "com/tenpay/injector/TMLogger")
        mv.visitInsn(Opcodes.DUP)
        mv.visitLdcInsn(className)
        mv.visitLdcInsn(methodName)
        mv.visitMethodInsn(
            Opcodes.INVOKESPECIAL,
            "com/tenpay/injector/TMLogger",
            "<init>",
            "(Ljava/lang/String;Ljava/lang/String;)V",
            false
        )
        mv.visitVarInsn(Opcodes.ASTORE, qwlogger)
        for (i in mByteBeans.indices) {
            val byteBean = mByteBeans[i]
            val name = byteBean.name
            val desc = byteBean.desc
            val index = byteBean.index
            val opcode = getLoadOpcodeFromDesc(desc)
            val fullyDesc = String.format("(Ljava/lang/String;%s)Lcom/tenpay/injector/TMLogger;", desc)
            visitPrint(qwlogger, index, opcode, name, fullyDesc)
        }
        mv.visitVarInsn(Opcodes.ALOAD, qwlogger)
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/tenpay/injector/TMLogger", "log", "()V", false)
        timingStartVarIndex = newLocal(Type.LONG_TYPE)
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "java/lang/System",
            "currentTimeMillis",
            "()J",
            false
        )
        mv.visitVarInsn(Opcodes.LSTORE, timingStartVarIndex)
    }

    /**
     * inject add function call.
     * */
    private fun visitPrint(
        varIndex: Int,
        localIndex: Int,
        opcode: Int,
        name: String,
        desc: String
    ) {
        mv.visitVarInsn(Opcodes.ALOAD, varIndex)
        mv.visitLdcInsn(name)
        mv.visitVarInsn(opcode, localIndex)
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "com/tenpay/injector/TMLogger",
            "add",
            desc, false
        )
        mv.visitInsn(Opcodes.POP)
    }

    override fun visitInsn(opcode: Int) {
        if (!logMethod && !logHook) {
            return super.visitInsn(opcode)
        }
        if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
            var rType = Type.getReturnType(methodDesc)
            var returnDesc = methodDesc.substring(methodDesc.indexOf(")") + 1)
            if (returnDesc.startsWith("[") || returnDesc.startsWith("L")) {
                returnDesc = "Ljava/lang/Object;"
            }
            var tempValIndex = -1
            if (rType !== Type.VOID_TYPE || opcode == Opcodes.ATHROW) {
                if (opcode == Opcodes.ATHROW) {
                    rType = Type.getType("Ljava/lang/Object;")
                }
                tempValIndex = newLocal(rType)
                var storeOpcocde = getStoreOpcodeFromType(rType)
                if (opcode == Opcodes.ATHROW) {
                    storeOpcocde = Opcodes.ASTORE
                }
                mv.visitVarInsn(storeOpcocde, tempValIndex)
            }
            visiteTMLogger(rType, opcode, tempValIndex, returnDesc)
        }
        super.visitInsn(opcode)
    }

    private fun visiteTMLogger(rType: Type, opcode: Int, tempValIndex: Int, returnDesc: String) {
        var temp = returnDesc
        mv.visitMethodInsn(
            Opcodes.INVOKESTATIC,
            "java/lang/System",
            "currentTimeMillis",
            "()J",
            false
        )
        mv.visitVarInsn(Opcodes.LLOAD, timingStartVarIndex)
        mv.visitInsn(Opcodes.LSUB)
        val index = newLocal(Type.LONG_TYPE)
        mv.visitVarInsn(Opcodes.LSTORE, index)
        mv.visitLdcInsn(className)
        mv.visitLdcInsn(methodName)
        mv.visitVarInsn(Opcodes.LLOAD, index)
        if (rType !== org.objectweb.asm.Type.VOID_TYPE || opcode == Opcodes.ATHROW) {
            var loadOpcode = getLoadOpcodeFromType(rType)
            if (opcode == Opcodes.ATHROW) {
                loadOpcode = Opcodes.ALOAD
                temp = "Ljava/lang/Object;"
            }
            mv.visitVarInsn(loadOpcode, tempValIndex)
            val formatDesc =
                String.format("(Ljava/lang/String;Ljava/lang/String;J%s)V", temp)
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/tenpay/injector/TMLogger", "ret", formatDesc, false)
            mv.visitVarInsn(loadOpcode, tempValIndex)
        } else {
            mv.visitLdcInsn("void")
            mv.visitMethodInsn(
                Opcodes.INVOKESTATIC, "com/tenpay/injector/TMLogger", "ret",
                "(Ljava/lang/String;Ljava/lang/String;JLjava/lang/Object;)V", false
            )
        }
    }

    init {
        if (!className.endsWith("/")) {
            this.className = className.substring(className.lastIndexOf("/") + 1)
        } else {
            this.className = className
        }
        mByteBeans = byteBeans
        methodName = name
        methodDesc = desc
    }
}
