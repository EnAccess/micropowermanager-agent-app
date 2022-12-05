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
}

dependencies {
    implementation(project(Modules.core))
    implementation(project(Modules.coreNetwork))
    implementation(project(Modules.coreLocalization))

    api(Libraries.fragmentKtx)
    api(Libraries.material)
    api(Libraries.constraintLayout)
    api(Libraries.appCompat)
    api(Libraries.recyclerView)
    api(Libraries.paris)
    api(Libraries.mpAndroidChart)
    api(Libraries.navigationFragment)
    api(Libraries.navigationUi)
    api(Libraries.lottie)
    api(Libraries.textDrawable)
}