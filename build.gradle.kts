// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.compose.compiler) apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("com.android.application") version "8.6.0" apply false
}