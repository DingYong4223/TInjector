package com.tenpay.injector.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.util.Collections

/**
 * Injector plugin entry, function apply will be called when code compile.
 * Created by delanding on 15/11/2020.
 */
class InjectorReporterPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val appExtension = project.properties["android"] as AppExtension?
        appExtension!!.registerTransform(InjectorReporterTransform(project), Collections.EMPTY_LIST)
    }
}
