package com.tenpay.injector.plugin.bytes

import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

object Utils : Opcodes {

    @JvmStatic
    fun getLoadOpcodeFromDesc(desc: String): Int {
        var opcode: Int = Opcodes.ILOAD
        when {
            "F" == desc -> {
                opcode = Opcodes.FLOAD
            }
            "J" == desc -> {
                opcode = Opcodes.LLOAD
            }
            "D" == desc -> {
                opcode = Opcodes.DLOAD
            }
            desc.startsWith("L") -> {
                opcode = Opcodes.ALOAD
            }
            desc.startsWith("[") -> {
                opcode = Opcodes.ALOAD
            }
        }
        return opcode
    }

    @JvmStatic
    fun getStoreOpcodeFromType(type: Type): Int {
        var opcode = Opcodes.ISTORE
        when (type.sort) {
            Type.LONG -> opcode = Opcodes.LSTORE
            Type.FLOAT -> opcode = Opcodes.FSTORE
            Type.DOUBLE -> opcode = Opcodes.DSTORE
            Type.OBJECT -> opcode = Opcodes.ASTORE
            Type.ARRAY -> opcode = Opcodes.ASTORE
        }
        return opcode
    }

    /**
     * get opcode by type
     * */
    @JvmStatic
    fun getLoadOpcodeFromType(type: Type): Int {
        var opcode = Opcodes.ILOAD
        when (type.sort) {
            Type.LONG -> opcode = Opcodes.LLOAD
            Type.FLOAT -> opcode = Opcodes.FLOAD
            Type.DOUBLE -> opcode = Opcodes.DLOAD
            Type.OBJECT -> opcode = Opcodes.ALOAD
            Type.ARRAY -> opcode = Opcodes.ALOAD
        }
        return opcode
    }

    /**
     * get class description.
     * @param clazz class object.
     */
    @JvmStatic
    fun getClazzDesc(clazz: Class<*>): String {
        return String.format("L%s;", clazz.name.replace('.', '/'))
    }
}
