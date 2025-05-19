plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    namespace = "com.inensus.core_ui"
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

val navigationVersion: String by rootProject.extra

dependencies {
    implementation(project(":core"))
    implementation(project(":core_network"))
    implementation(project(":core_localization"))

    api("androidx.fragment:fragment-ktx:1.2.0-rc01")
    api("com.google.android.material:material:1.3.0")
    api("androidx.constraintlayout:constraintlayout:2.0.0")
    api("androidx.appcompat:appcompat:1.0.0")
    api("androidx.recyclerview:recyclerview:1.1.0")
    api("com.airbnb.android:paris:1.4.0")
    api("com.github.PhilJay:MPAndroidChart:v3.1.0")
    api("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    api("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    api("com.airbnb.android:lottie:3.3.1")
    api("com.github.amulyakhare:textdrawable:558677e")
}
