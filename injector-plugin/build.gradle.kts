plugins {
    id("java-gradle-plugin")
    id("kotlin")
    id("maven-publish")
}

dependencies {
    compileOnly(gradleApi())

    implementation("com.android.tools.build:gradle:3.3.0")
    implementation("com.squareup:javapoet:1.10.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin_version}")

    testImplementation("junit:junit:4.12")
    testImplementation("com.google.truth:truth:0.42")
    testImplementation("androidx.annotation:annotation:1.0.0")
    testImplementation("com.google.testing.compile:compile-testing:0.15")
}

group = "com.tenpay.injector"
version = rootProject.extra["publish_version"] as String

// Maven 发布配置
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = "com.tenpay.injector"
            artifactId = "injector-plugin"
        }
    }

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

