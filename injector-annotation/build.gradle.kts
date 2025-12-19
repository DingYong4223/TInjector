plugins {
    id("java-library")
    id("maven-publish")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

group = "com.tenpay.injector"
version = rootProject.extra["publish_version"] as String

// 新配置（maven-publish 插件）
publishing {
    // 配置发布的「产物」（Java 库则发布 jar 包）
    publications {
        create<MavenPublication>("mavenJava") {
            // 核心：关联模块的 Java 组件（自动包含编译后的类、资源等）
            from(components["java"])
            // 配置 Maven 坐标（与旧 pom 保持一致）
            groupId = "com.tenpay.injector"
            artifactId = "injector-annotation"
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

