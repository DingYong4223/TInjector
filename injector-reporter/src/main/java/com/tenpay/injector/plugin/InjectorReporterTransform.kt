package com.tenpay.injector.plugin

import com.android.build.api.transform.Context
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.tenpay.injector.plugin.bytes.ReporterWeaver
import org.gradle.api.Project
import java.io.IOException

/**
 * TLogger annotation transform.
 * Created by delanding on 15/11/2020.
 */
class InjectorReporterTransform(private val project: Project) : InjectorTransformReport(project) {

    private var logExtension: ReporterExtension? = null

    init {
        project.extensions.create(EXT_KEY, ReporterExtension::class.java)
        bytecodeWeaver = ReporterWeaver()
    }

    /**
     * function will be called when transform trigged after code compile.
     * */
    @Throws(IOException::class, TransformException::class, InterruptedException::class)
    override fun transform(context: Context, inputs: Collection<TransformInput>, referencedInputs: Collection<TransformInput>, outputProvider: TransformOutputProvider, isIncremental: Boolean) {
        logExtension = project.extensions.getByName(EXT_KEY) as ReporterExtension
        super.transform(context, inputs, referencedInputs, outputProvider, isIncremental)
    }

    /**
     * get variant
     * */
    override fun getRunVariant(): RunVariant {
        return logExtension!!.variant
    }

    override fun inDuplcatedClassSafeMode(): Boolean {
        return logExtension!!.safeMode
    }

    companion object {
        private const val EXT_KEY = "InjectorExt"
    }

}
