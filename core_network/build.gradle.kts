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
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            isMinifyEnabled = false
        }
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(project(":core"))

    api("com.squareup.retrofit2:retrofit:2.7.1")
    api("com.squareup.retrofit2:converter-gson:2.7.1")
    api("com.squareup.retrofit2:adapter-rxjava2:2.7.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.3.0")
}
