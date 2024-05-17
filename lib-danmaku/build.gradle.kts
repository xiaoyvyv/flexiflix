plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.kuaishou.akdanmaku"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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

    buildFeatures {
        buildConfig = true
    }
}

configurations {
    create("natives")
}

dependencies {
    // LibGdx-Core
    implementation(libs.gdx)

    // LibGdx-Android
    implementation(libs.gdx.backend.android)

    val gdxVersion = "1.12.1"
    add("natives", "com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-armeabi-v7a")
    add("natives", "com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-arm64-v8a")
    add("natives", "com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86")
    add("natives", "com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-x86_64")

    // LibGdx-Ashley(ECS)
    implementation(libs.ashley)

    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}


// 每次执行 Gradle 时调用，采用 natives 配置，并将它们解压缩到正确的 jniLibs
tasks.create("copyAndroidNatives") {
    doFirst {
        file("src/main/jniLibs/armeabi-v7a/").mkdirs()
        file("src/main/jniLibs/arm64-v8a/").mkdirs()
        file("src/main/jniLibs/x86_64/").mkdirs()
        file("src/main/jniLibs/x86/").mkdirs()

        configurations["natives"].files.forEach { jar ->
            var outputDir: File? = null
            if (jar.name.endsWith("natives-arm64-v8a.jar")) {
                outputDir = file("src/main/jniLibs/arm64-v8a")
            }
            if (jar.name.endsWith("natives-armeabi-v7a.jar")) {
                outputDir = file("src/main/jniLibs/armeabi-v7a")
            }
            if (jar.name.endsWith("natives-x86_64.jar")) {
                outputDir = file("src/main/jniLibs/x86_64")
            }
            if (jar.name.endsWith("natives-x86.jar")) {
                outputDir = file("src/main/jniLibs/x86")
            }

            if (outputDir != null) {
                copy {
                    from(zipTree(jar))
                    into(outputDir)
                    include("*.so")
                }
            }
        }
    }
}

tasks.whenTaskAdded {
    if (name.contains("package")) {
        dependsOn("copyAndroidNatives")
    }
}
