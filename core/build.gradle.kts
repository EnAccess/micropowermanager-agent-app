plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
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
}

val kotlinVersion: String by rootProject.extra

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    api("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    api("androidx.core:core-ktx:1.1.0-alpha05")

    // Koin
    api("io.insert-koin:koin-androidx-scope:2.2.3")
    api("io.insert-koin:koin-androidx-viewmodel:2.2.3")
    api("io.insert-koin:koin-core:2.2.3")
    api("io.insert-koin:koin-core-ext:2.2.3")

    api("io.reactivex.rxjava2:rxjava:2.2.9")
    api("io.reactivex.rxjava2:rxkotlin:2.3.0")
    api("io.reactivex.rxjava2:rxandroid:2.1.1")
    api("com.jakewharton.rxbinding2:rxbinding:2.2.0")

    api("com.jakewharton.timber:timber:4.7.1")
    api("androidx.preference:preference-ktx:1.1.1")

    api("com.google.firebase:firebase-messaging:20.2.3")
    api("com.google.firebase:firebase-analytics-ktx:17.4.4")
    api("com.google.firebase:firebase-crashlytics:17.1.1")
}
