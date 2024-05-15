plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
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


    val media3_version = "1.3.1"

    // For media playback using ExoPlayer
    api("androidx.media3:media3-exoplayer:$media3_version")
    api("androidx.media3:media3-exoplayer-dash:$media3_version")
    api("androidx.media3:media3-exoplayer-hls:$media3_version")
    api("androidx.media3:media3-exoplayer-smoothstreaming:$media3_version")
    api("androidx.media3:media3-datasource-okhttp:$media3_version")
    api("androidx.media3:media3-ui:$media3_version")

/*

    // For exposing and controlling media sessions
    implementation("androidx.media3:media3-session:$media3_version")

    // For extracting data from media containers
    implementation("androidx.media3:media3-extractor:$media3_version")

    // For integrating with Cast
    implementation("androidx.media3:media3-cast:$media3_version")

    // For scheduling background operations using Jetpack Work's WorkManager with ExoPlayer
    implementation("androidx.media3:media3-exoplayer-workmanager:$media3_version")

    // For transforming media files
    implementation("androidx.media3:media3-transformer:$media3_version")

    // For applying effects on video frames
    implementation("androidx.media3:media3-effect:$media3_version")

    // For muxing media files
    implementation("androidx.media3:media3-muxer:$media3_version")

    // Utilities for testing media components (including ExoPlayer components)
    implementation("androidx.media3:media3-test-utils:$media3_version")
    // Utilities for testing media components (including ExoPlayer components) via Robolectric
    implementation("androidx.media3:media3-test-utils-robolectric:$media3_version")

    // Common functionality for reading and writing media containers
    implementation("androidx.media3:media3-container:$media3_version")
    // Common functionality for media database components
    implementation("androidx.media3:media3-database:$media3_version")
    // Common functionality for media decoders
    implementation("androidx.media3:media3-decoder:$media3_version")
    // Common functionality for loading data
    implementation("androidx.media3:media3-datasource:$media3_version")
    // Common functionality used across multiple media libraries
    implementation("androidx.media3:media3-common:$media3_version")
*/


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}