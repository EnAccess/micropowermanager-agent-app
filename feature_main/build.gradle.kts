plugins {
    id(BuildPlugins.androidLibrary)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
}

android {
    compileSdkVersion(AndroidSdk.compile)

    defaultConfig {
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
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
    implementation(project(Modules.core))
    implementation(project(Modules.coreUi))
    implementation(project(Modules.coreNetworkAuth))
    implementation(project(Modules.coreLocalization))
    implementation(project(Modules.featureDashboard))
    implementation(project(Modules.featureCustomers))
    implementation(project(Modules.featurePayment))
    implementation(project(Modules.featureAppliance))
    implementation(project(Modules.featureTicket))
    implementation(project(Modules.sharedAgent))
    implementation(project(Modules.sharedNavigation))
    implementation(project(Modules.sharedMessaging))
    implementation(project(Modules.sharedSuccess))
}