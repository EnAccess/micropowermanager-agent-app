plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.inensus.feature_main"
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":core_ui"))
    implementation(project(":core_network_auth"))
    implementation(project(":core_localization"))
    implementation(project(":feature_dashboard"))
    implementation(project(":feature_customers"))
    implementation(project(":feature_payment"))
    implementation(project(":feature_appliance"))
    implementation(project(":feature_ticket"))
    implementation(project(":shared_agent"))
    implementation(project(":shared_navigation"))
    implementation(project(":shared_messaging"))
    implementation(project(":shared_success"))
}
