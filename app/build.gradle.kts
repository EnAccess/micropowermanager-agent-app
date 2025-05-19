plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")

    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.github.ben-manes.versions")
}

android {
    namespace = "com.inensus.merchant"
    compileSdk = 31

    defaultConfig {
        applicationId = "com.inensus.merchant"
        minSdk = 21
        targetSdk = 31
        versionCode = 1
        versionName = "1.4.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("release.keystore")
            storePassword = "556211"
            keyAlias = "release"
            keyPassword = "556211"
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isZipAlignEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }

        getByName("debug") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        viewBinding = true
    }

    lint {
        textReport = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core_ui"))
    implementation(project(":core_network"))
    implementation(project(":core_network_auth"))
    implementation(project(":core_network_no_auth"))
    implementation(project(":core_localization"))
    implementation(project(":feature_login"))
    implementation(project(":feature_main"))
    implementation(project(":shared_navigation"))
    implementation(project(":shared_agent"))
    implementation(project(":shared_success"))
    implementation(project(":shared_customer"))
    implementation(project(":shared_messaging"))

    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test:runner:1.3.0-rc03")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0-rc03")
}

tasks.register("printVersionName") {
    doLast {
        println(android.defaultConfig.versionName)
    }
}
