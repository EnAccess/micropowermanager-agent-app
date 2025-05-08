plugins {
    id(BuildPlugins.androidApplication)
    id(BuildPlugins.kotlinAndroid)
    id(BuildPlugins.kotlinAndroidExtensions)
    id(BuildPlugins.kotlinKapt)
    id(BuildPlugins.googleServicesBuild)
    id(BuildPlugins.firebaseCrashlyticsBuild)
    id(BuildPlugins.gradleVersionsBuild)
}

android {
    compileSdkVersion(AndroidSdk.compile)

    defaultConfig {
        applicationId = "com.inensus.merchant"
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        versionCode = 1
        versionName = "1.1.0"
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
                "proguard-rules.pro"
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
}

dependencies {
    implementation(project(Modules.core))
    implementation(project(Modules.coreUi))
    implementation(project(Modules.coreNetwork))
    implementation(project(Modules.coreNetworkAuth))
    implementation(project(Modules.coreNetworkNoAuth))
    implementation(project(Modules.coreLocalization))
    implementation(project(Modules.featureLogin))
    implementation(project(Modules.featureMain))
    implementation(project(Modules.sharedNavigation))
    implementation(project(Modules.sharedAgent))
    implementation(project(Modules.sharedSuccess))
    implementation(project(Modules.sharedCustomer))
    implementation(project(Modules.sharedMessaging))

    testImplementation(TestLibraries.junit4)
    androidTestImplementation(TestLibraries.testRunner)
    androidTestImplementation(TestLibraries.espresso)
}
