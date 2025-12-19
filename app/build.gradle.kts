plugins {
    id("com.android.application")
    id("kotlin-android")
//    id("com.tencent.plugin.transform")
}

buildscript {
    repositories {
        mavenLocal()
        jcenter()
        google()
        maven { url = uri("../repo") }
    }
    dependencies {
        classpath("com.tenpay.injector:injector-logger:1.0.3")
        //classpath("upload:injector-logger:0.0.5")
    }
}

apply(plugin = "injector-logger")

android {
    compileSdk = 29

    defaultConfig {
        minSdk = 16
        targetSdk = 29
        renderscriptSupportModeEnabled = true
        applicationId = "com.tencent.hello"
        minSdk = 21
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin_version}")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.core:core-ktx:1.0.2")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("com.github.bumptech.glide:glide:4.8.0")

    implementation(project(":injector-runtime"))
    implementation(project(":injector-annotation"))
    annotationProcessor(project(":injector-compiler"))

    /***********************test**************************/
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

    testImplementation("junit:junit:4.12")
    testImplementation("org.powermock:powermock-classloading-xstream:2.0.7")
    testImplementation("org.hamcrest:hamcrest-library:1.3")
    testImplementation("org.mockito:mockito-core:2.28.2")
    testImplementation("org.powermock:powermock-core:2.0.7")
    testImplementation("org.powermock:powermock-module-junit4:2.0.7")
    testImplementation("org.powermock:powermock-module-junit4-rule:2.0.7")
    testImplementation("org.powermock:powermock-api-mockito2:2.0.7")
    testImplementation("com.android.support.test:runner:1.0.2")
    testImplementation("com.android.support.test:rules:1.0.2")
    testImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
    testImplementation("org.robolectric:robolectric:4.2.1")
}

apply(from = "../jacoco_offline.gradle")

