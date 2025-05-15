plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    // TBD: Deprecated
    id("kotlin-android-extensions")
}

android {
    compileSdk = 29

    defaultConfig {
        minSdk = 21
        targetSdk = 29
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
            )
        }
    }

    kotlinOptions { jvmTarget = "1.8" }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core_ui"))
    implementation(project(":core_network_auth"))
    implementation(project(":core_localization"))
    implementation(project(":shared_success"))
    implementation(project(":shared_customer"))
}
