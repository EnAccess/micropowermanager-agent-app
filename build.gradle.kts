// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.3.0-alpha03")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.2.0")
    }
}

plugins {

    /**
     * Use `apply false` in the top-level build.gradle file to add a Gradle
     * plugin as a build dependency but not apply it to the current (root)
     * project. Don't use `apply false` in sub-projects. For more information,
     * see Applying external plugins with same version to subprojects.
     */

    id("com.android.application") version "4.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.5.32" apply false
    id("org.jetbrains.kotlin.kapt") version "1.5.32" apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "1.5.32" apply false

    id("com.github.ben-manes.versions") version "0.27.0" apply false

    id("com.google.gms.google-services") version "4.3.3" apply false
}

extra["kotlinVersion"] = "1.5.32"
extra["navigationVersion"] = "2.3.0-alpha03"

tasks.register("clean").configure {
    delete("build")
}
