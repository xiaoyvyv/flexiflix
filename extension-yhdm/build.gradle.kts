plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.xiaoyv.extension.yhdm"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.xiaoyv.extension.yhdm"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = getByName("debug").signingConfig
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


configurations {
    all {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jdk8")
    }
}


dependencies {
    compileOnly(project(":lib-extension"))
    compileOnly(libs.kotlin.stdlib.jdk8)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

tasks.register("packageExtension") {
    group = "build-extension"
    dependsOn("assembleRelease")

    doLast {
        val file = project.layout.buildDirectory
            .file("outputs/apk/release/extension-yhdm-release.apk")
            .get().asFile

        val target = project.rootProject
            .file("app/src/main/assets/extension/extension-yhdm-release.apk")

        file.copyTo(target, overwrite = true)
    }
}

