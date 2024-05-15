// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.googleKsp) apply false
    alias(libs.plugins.androidHilt) apply false
    id("com.chaquo.python") version "15.0.1" apply false
}

