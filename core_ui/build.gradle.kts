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

val navigationVersion: String by rootProject.extra

dependencies {
    implementation(project(":core"))
    implementation(project(":core_network"))
    implementation(project(":core_localization"))

    api("androidx.fragment:fragment-ktx:1.2.0-rc01")
    api("com.google.android.material:material:1.2.0-alpha05")
    api("androidx.constraintlayout:constraintlayout:2.0.0-beta4")
    api("androidx.appcompat:appcompat:1.0.0-beta01")
    api("androidx.recyclerview:recyclerview:1.1.0")
    api("com.airbnb.android:paris:1.4.0")
    api("com.github.PhilJay:MPAndroidChart:v3.1.0")
    api("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    api("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    api("com.airbnb.android:lottie:3.3.1")
    api("com.github.amulyakhare:textdrawable:558677e")
}
