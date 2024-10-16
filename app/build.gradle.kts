plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.gymsmart"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gymsmart"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Update to latest if needed
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    lint {
        abortOnError = false
    }
}

dependencies {
    // Core Android dependencies
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")

    // Jetpack Compose BOM
    implementation(platform("androidx.compose:compose-bom:2023.05.01")) // Update version as needed

    // Compose UI dependencies (managed by BOM)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.navigation:navigation-compose:2.7.0") // Navigation for Compose

    // Material Design 3 and related dependencies
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.navigation:navigation-testing:2.7.0")


    // Compose UI dependencies (versions managed by the BOM)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.icons.lucide)

    // Image Loading with Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Gson for JSON parsing
    implementation("com.google.code.gson:gson:2.8.8")

     // Unit tests
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter

    // YouTube API for third-party integration
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.1")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:custom-ui:12.1.1")
    implementation(files("libs/core-12.1.1-javadoc.jar"))
    // Material 3
    implementation(libs.androidx.material3)
    implementation(libs.material)
    implementation (libs.material3)
    implementation(libs.androidx.navigation.testing)
    implementation(libs.firebase.firestore.ktx)


    // Android Instrumentation tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.05.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Debugging tools
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))


    // When using the BoM, don't specify versions in Firebase dependencies
    // Firebase Auth and Google Sign-In
    implementation (libs.firebase.auth)
    implementation (libs.play.services.auth)
    implementation (libs.androidx.credentials)



    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    implementation(libs.coil.compose)
    implementation (libs.gson)

}
