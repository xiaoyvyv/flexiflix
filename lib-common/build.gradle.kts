plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.googleKsp)
    id("kotlin-parcelize")
}

android {
    namespace = "com.xiaoyv.player.common"
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

        freeCompilerArgs = listOf(
            "-opt-in=androidx.paging.ExperimentalPagingApi",
        )
    }
}

dependencies {
    api(project(":lib-extension"))
    api(project(":lib-i18n"))

    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.viewmodel.compose)
    api(libs.androidx.lifecycle.runtime.compose)
    api(libs.androidx.constraintlayout.compose)
    api(libs.androidx.paging.compose)
    api(libs.androidx.navigation.compose)
    api(libs.accompanist.permissions)

    api(libs.glide.compose)
    api(libs.glide.okhttp3.integration)
    ksp(libs.glide.ksp)

    api(libs.androidx.media3.exoplayer)
    api(libs.androidx.media3.exoplayer.dash)
    api(libs.androidx.media3.exoplayer.hls)
    api(libs.androidx.media3.exoplayer.smoothstreaming)
    api(libs.androidx.media3.datasource.okhttp)
    api(libs.androidx.media3.ui)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}