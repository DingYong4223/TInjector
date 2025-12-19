plugins {
    id("groovy")
    id("java-gradle-plugin")
    id("kotlin")
    id("maven-publish")
}

repositories {
    mavenLocal()
    google()
    jcenter()
    mavenCentral()
}

buildscript {
    repositories {
        mavenLocal()
        google()
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${property("kotlin_version")}")
    }
}

dependencies {
    implementation(gradleApi())
    implementation(localGroovy())
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${property("kotlin_version")}")
    implementation(project(":injector-annotation"))
//    implementation("com.tenpay.injector:injector-annotation:1.0.3")
    implementation("com.android.tools.build:gradle:3.1.4")
    implementation("com.android.tools.build:gradle-api:3.1.4")
    implementation("commons-io:commons-io:2.6")
    implementation("commons-codec:commons-codec:1.10")
    implementation("org.ow2.asm:asm:5.1")
    implementation("org.ow2.asm:asm-util:5.1")
    implementation("org.ow2.asm:asm-commons:5.1")
}

// 扩展属性
extra.apply {
    set("bintrayRepo", "maven")
    set("bintrayName", "injector-reporter")

    set("publishedGroupId", "com.tenpay.injector")
    set("libraryName", "injector-reporter")
    set("artifact", "injector-reporter")

    set("libraryDescription", "A transform plugin for android debug.")

    set("libraryVersion", "0.0.5")

    set("developerId", "delanding")
    set("developerName", "delanding")

    set("licenseName", "The Apache Software License, Version 2.0")
    set("licenseUrl", "http://www.apache.org/licenses/LICENSE-2.0.txt")
    set("allLicenses", listOf("Apache-2.0"))
}

group = "com.tenpay.injector"
version = rootProject.extra["publish_version"] as String

// 新配置（maven-publish 插件）
publishing {
    // 配置发布的「产物」（Java 库则发布 jar 包）
    publications {
        create<MavenPublication>("maven") {
            // 配置 Maven 坐标（与旧 pom 保持一致）
            groupId = "com.tenpay.injector"
            artifactId = "injector-reporter"
        }
    }

    // 配置发布仓库（本地 Maven 仓库，与旧配置路径一致）
    repositories {
        // maven {
        //     url = uri("https://your-nexus-repo.com/repository/maven-releases/")
        //     credentials {
        //         username = "your-username"
        //         password = "your-password"
        //     }
        // }
    }
}

