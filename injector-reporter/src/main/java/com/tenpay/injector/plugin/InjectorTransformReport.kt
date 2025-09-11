package com.tenpay.injector.plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.Context
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.api.transform.Status
import com.android.build.api.transform.Format
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.ide.common.internal.WaitableExecutor
import com.google.common.io.Files
import com.tenpay.injector.plugin.asm.BaseWeaver
import com.tenpay.injector.plugin.asm.ClsHelper.getClassLoader
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import java.io.File
import java.io.IOException

/**
 * Created by Quinn on 26/02/2017.
 * Transform to modify bytecode
 * Created by delanding on 15/11/2020.
 */
open class InjectorTransformReport(private val project: Project) : Transform() {

    companion object {
        val SCOPES: MutableSet<QualifiedContent.Scope> = HashSet()

        init {
            SCOPES.add(QualifiedContent.Scope.PROJECT)
            SCOPES.add(QualifiedContent.Scope.SUB_PROJECTS)
            SCOPES.add(QualifiedContent.Scope.EXTERNAL_LIBRARIES)
        }
    }

    protected var bytecodeWeaver: BaseWeaver? = null
    private val waitableExecutor: WaitableExecutor = WaitableExecutor.useGlobalSharedThreadPool()
    private var emptyRun = false
    override fun getName(): String {
        return javaClass.simpleName
    }

    override fun getInputTypes(): Set<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope>? {
        return SCOPES
    }

    override fun isIncremental(): Boolean {
        return true
    }

    @Throws(IOException::class, TransformException::class, InterruptedException::class)
    override fun transform(
        context: Context,
        inputs: Collection<TransformInput>,
        referencedInputs: Collection<TransformInput>,
        outputProvider: TransformOutputProvider,
        isIncremental: Boolean
    ) {
        val runVariant = getRunVariant()
        if (context.variantName.equals("debug", ignoreCase = true)) {
            emptyRun = runVariant === RunVariant.RELEASE || runVariant === RunVariant.NEVER
        } else if (context.variantName.equals("release", ignoreCase = true)) {
            emptyRun = runVariant === RunVariant.DEBUG || runVariant === RunVariant.NEVER
        }
        if (!isIncremental) {
            outputProvider.deleteAll()
        }
        bytecodeWeaver!!.classLoader = getClassLoader(inputs, referencedInputs, project)
        var cleanDBFolder = false
        for (input in inputs) {
            cleanDBFolder = transformPros(input, outputProvider, cleanDBFolder)
            for (directoryInput in input.directoryInputs) {
                val dest = outputProvider.getContentLocation(
                    directoryInput.name, directoryInput.contentTypes,
                    directoryInput.scopes, Format.DIRECTORY
                )
                FileUtils.forceMkdir(dest)
                if (isIncremental && !emptyRun) {
                    val srcDirPath = directoryInput.file.absolutePath
                    val destDirPath = dest.absolutePath
                    val fileStatusMap = directoryInput.changedFiles
                    for ((inputFile, status) in fileStatusMap) {
                        val destFilePath = inputFile.absolutePath.replace(srcDirPath, destDirPath)
                        val destFile = File(destFilePath)
                        when (status) {
                            Status.NOTCHANGED -> {
                            }
                            Status.REMOVED -> if (destFile.exists()) {
                                destFile.delete()
                            }
                            Status.ADDED, Status.CHANGED -> {
                                try {
                                    FileUtils.touch(destFile)
                                } catch (e: IOException) {
                                    Files.createParentDirs(destFile)
                                }
                                transformSingleFile(inputFile, destFile, srcDirPath)
                            }
                        }
                    }
                } else {
                    transformDir(directoryInput.file, dest)
                }
            }
        }
        waitableExecutor.waitForTasksWithQuickFail<Any>(true)
    }

    private fun transformPros(
        input: TransformInput,
        outputProvider: TransformOutputProvider,
        cleanDBFolder: Boolean
    ): Boolean {
        var temp = cleanDBFolder
        for (jarInput in input.jarInputs) {
            val status = jarInput.status
            val dest = outputProvider.getContentLocation(
                jarInput.file.absolutePath,
                jarInput.contentTypes, jarInput.scopes, Format.JAR
            )
            if (isIncremental && !emptyRun) {
                when (status) {
                    Status.NOTCHANGED -> {
                    }
                    Status.ADDED, Status.CHANGED -> transformJar(jarInput.file, dest, status)
                    Status.REMOVED -> if (dest.exists()) {
                        FileUtils.forceDelete(dest)
                    }
                }
            } else {
                if (inDuplcatedClassSafeMode() and !isIncremental && !cleanDBFolder) {
                    cleanDexBuilderFolder(dest)
                    temp = true
                }
                transformJar(jarInput.file, dest, status)
            }
        }
        return temp
    }

    private fun transformSingleFile(inputFile: File, outputFile: File, srcBaseDir: String) {
        waitableExecutor.execute<Any?> {
            bytecodeWeaver!!.weaveSingleClassToFile(inputFile, outputFile, srcBaseDir)
            null
        }
    }

    @Throws(IOException::class)
    private fun transformDir(inputDir: File, outputDir: File) {
        if (emptyRun) {
            FileUtils.copyDirectory(inputDir, outputDir)
            return
        }
        val inputDirPath = inputDir.absolutePath
        val outputDirPath = outputDir.absolutePath
        if (inputDir.isDirectory) {
            for (file in com.android.utils.FileUtils.getAllFiles(inputDir)) {
                waitableExecutor.execute<Any?> {
                    val filePath = file.absolutePath
                    val outputFile = File(filePath.replace(inputDirPath, outputDirPath))
                    bytecodeWeaver!!.weaveSingleClassToFile(file, outputFile, inputDirPath)
                    null
                }
            }
        }
    }

    private fun transformJar(srcJar: File, destJar: File, status: Status) {
        waitableExecutor.execute<Any?> {
            if (emptyRun) {
                FileUtils.copyFile(srcJar, destJar)
                return@execute null
            }
            bytecodeWeaver!!.weaveJar(srcJar, destJar)
            null
        }
    }

    private fun cleanDexBuilderFolder(dest: File) {
        waitableExecutor.execute<Any?> {
            val dexBuilderDir = replaceLastPart(dest.absolutePath, name, "dexBuilder")
            // intermediates/transforms/dexBuilder/debug
            val file = File(dexBuilderDir).parentFile
            project.logger.warn("clean folder: " + file.absolutePath)
            if (file.exists() && file.isDirectory) {
                com.android.utils.FileUtils.deleteDirectoryContents(file)
            }
        }
    }

    private fun replaceLastPart(
        originString: String,
        replacement: String,
        toreplace: String
    ): String {
        val start = originString.lastIndexOf(replacement)
        return originString.substring(0, start) +
                toreplace +
                originString.substring(start + replacement.length)
    }

    override fun isCacheable(): Boolean {
        return true
    }

    open fun getRunVariant(): RunVariant {
        return RunVariant.ALWAYS
    }

    protected open fun inDuplcatedClassSafeMode(): Boolean {
        return false
    }
}
