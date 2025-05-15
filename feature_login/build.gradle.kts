plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    // TBD: Deprecated
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)
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
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core_ui"))
    implementation(project(":core_localization"))
    implementation(project(":shared_navigation"))
    implementation(project(":core_network_no_auth"))
    implementation(project(":core_network_auth"))
}
