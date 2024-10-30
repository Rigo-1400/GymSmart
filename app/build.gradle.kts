plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
}

android {
    buildFeatures {
        buildConfig = true
    }
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
        // Read the API key from the local.properties file
//        val googleApiKey = project.rootProject.file("local.properties").inputStream().use { inputStream ->
//            val properties = Properties()
//            properties.load(inputStream)
//            properties.getProperty("GOOGLE_API_KEY") ?: ""
//        }
//        // Pass the API key to the BuildConfig
//        buildConfigField("String", "GOOGLE_API_KEY", "\"${googleApiKey}\"")
          val googleApiKey = System.getenv("GOOGLE_API_KEY")
          buildConfigField("String", "GOOGLE_API_KEY", "\"${googleApiKey}\"")
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
        kotlinCompilerExtensionVersion = "1.5.1" // Update this to your Compose version
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    // Compose UI dependencies (versions managed by the BOM)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.icons.lucide)


    // Material 3
    implementation(libs.androidx.material3)
    implementation(libs.material)
    implementation (libs.material3)
    implementation(libs.androidx.navigation.testing)
    implementation(libs.firebase.firestore.ktx)

    // Unit tests
    testImplementation(libs.junit)
    testImplementation(libs.junit.jupiter)

    // Android Instrumentation tests
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debugging tools
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))


    // When using the BoM, don't specify versions in Firebase dependencies
    // Firebase Auth and Google Sign-In
    implementation (libs.firebase.auth)
    implementation (libs.play.services.auth)
    implementation (libs.androidx.credentials)

    // YouTube API for third-party integration
    implementation(libs.core)
    implementation(libs.custom.ui)


    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    implementation(libs.coil.compose)
    implementation (libs.gson)

}