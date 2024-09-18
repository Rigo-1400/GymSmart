plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.anew"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.anew"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    // Enable Jetpack Compose
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get() // Use Compose version from version catalog
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    lint {
        abortOnError = false
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.browser:browser:1.2.0")

    // Jetpack Compose dependencies
    implementation(libs.compose.ui) // Core Compose UI library
    implementation(libs.compose.material3) // Material3 Design system
    implementation(libs.compose.ui.tooling.preview) // UI tooling preview in IDE
    implementation(libs.compose.activity) // For Compose integration with Activity

    // Debugging and testing dependencies for Compose
    debugImplementation(libs.compose.ui.tooling)
    androidTestImplementation(libs.compose.test.junit)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
