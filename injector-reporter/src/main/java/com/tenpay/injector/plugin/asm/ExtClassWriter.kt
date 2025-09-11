package com.tenpay.injector.plugin.asm

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import java.io.IOException

/**
 * rewrite class after .class file modification.
 * Created by delanding on 15/11/2020.
 */
class ExtClassWriter(private val urlClassLoader: ClassLoader?, flags: Int) : ClassWriter(flags) {

    /**
     * get common super class of the modified clazz.
     * this function will be called when class was modified.
     * */
    override fun getCommonSuperClass(type1: String?, type2: String?): String {
        if (type1 == null || type2 == null) {
            return OBJECT
        } else if (type1 == OBJECT || type2 == OBJECT) {
            return OBJECT
        }
        if (type1 == type2) {
            return type1
        }
        val type1ClassReader = getClassReader(type1)
        val type2ClassReader = getClassReader(type2)
        if (type1ClassReader == null || type2ClassReader == null) {
            return OBJECT
        }
        if (isInterface(type1ClassReader)) {
            return if (isImplements(type1, type2ClassReader)) {
                type1
            } else if (isInterface(type2ClassReader) && isImplements(type2, type1ClassReader)) {
                type2
            } else {
                OBJECT
            }
        }
        if (isInterface(type2ClassReader)) {
            return if (isImplements(type2, type1ClassReader)) {
                type2
            } else {
                OBJECT
            }
        }
        return returnSuperCls(type1, type2, type1ClassReader, type2ClassReader)
    }

    private fun returnSuperCls(type1: String, type2: String, type1ClassReader: ClassReader, type2ClassReader: ClassReader): String {
        val superClassNames: MutableSet<String> = HashSet()
        superClassNames.add(type1)
        superClassNames.add(type2)
        var s1: String? = type1ClassReader.superName
        var s2: String? = type2ClassReader.superName
        if (null != s1 && !superClassNames.add(s1)) {
            return s1
        } else if (null != s2 && !superClassNames.add(s2)) {
            return s2
        }
        while (s1 != null || s2 != null) {
            s1 = getSuperClassName(s1)
            s2 = getSuperClassName(s2)
            if (null != s1 && !superClassNames.add(s1)) {
                return s1
            } else if (null != s2 && !superClassNames.add(s2)) {
                return s2
            }
        }
        return OBJECT
    }

    private fun isImplements(interfaceName: String, classReader: ClassReader): Boolean {
        var classInfo: ClassReader? = classReader
        while (classInfo != null) {
            if (checkIfImolement(classInfo, interfaceName)) {
                return true
            }
            val superClassName = classInfo.superName
            if (superClassName == null || superClassName == OBJECT) {
                break
            }
            classInfo = getClassReader(superClassName)
        }
        return false
    }

    private fun checkIfImolement(classInfo: ClassReader, interfaceName: String): Boolean {
        val interfaceNames = classInfo.interfaces
        for (name in interfaceNames) {
            if (name == interfaceName) {
                return true
            }
        }
        for (name in interfaceNames) {
            val interfaceInfo = getClassReader(name)
            if (null != interfaceInfo && isImplements(interfaceName, interfaceInfo)) {
                return true
            }
        }
        return false
    }

    private fun isInterface(classReader: ClassReader): Boolean {
        return classReader.access and Opcodes.ACC_INTERFACE != 0
    }

    private fun getSuperClassName(className: String?): String? {
        val classReader = getClassReader(className) ?: return null
        return classReader.superName
    }

    private fun getClassReader(className: String?): ClassReader? {
        val inputStream = urlClassLoader?.getResourceAsStream("$className.class")
        try {
            if (inputStream != null) {
                return ClassReader(inputStream)
            }
        } catch (e: IOException) {
            println("ioexception: " + e.message)
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (ignored: IOException) {
                }
            }
        }
        return null
    }

    companion object {
        private const val OBJECT = "java/lang/Object"
    }
}
