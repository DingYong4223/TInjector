buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.7.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin_version}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url = uri("https://jalenchen.bintray.com/maven/")
        }
        maven {
            url = uri("https://mirrors.tencent.com/repository/maven/thirdparty/")
        }
//        mavenLocal()
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

fun getLocalProperties(): java.util.Properties {
    val properties = java.util.Properties()
    val inputStream = project.rootProject.file("local.properties").inputStream()
    properties.load(inputStream)
    return properties
}

extra.apply {
    set("localProperties", getLocalProperties())
    set("publish_version", "1.0.3")
}

