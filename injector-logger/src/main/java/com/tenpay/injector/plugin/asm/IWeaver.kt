package com.tenpay.injector.plugin.asm

import java.io.IOException
import java.io.InputStream

interface IWeaver {
    /**
     * Check a certain file is weavable
     */
    @Throws(IOException::class)
    fun isWeavableClass(filePath: String): Boolean

    /**
     * Weave single class to byte array
     */
    @Throws(IOException::class)
    fun weaveSingleClassToByteArray(inputStream: InputStream): ByteArray
}
