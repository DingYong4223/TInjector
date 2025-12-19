plugins {
    id("com.android.library")
    id("maven-publish")
}

buildscript {
    repositories {
        mavenCentral()
        mavenLocal()
        google()
    }
    dependencies {
    }
}

android {
    namespace = "com.tenpay.injector.runtime"
    compileSdk = 29

    defaultConfig {
        minSdk = 16
        targetSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    testImplementation("junit:junit:4.12")
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
            artifactId = "injector-runtime"
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

