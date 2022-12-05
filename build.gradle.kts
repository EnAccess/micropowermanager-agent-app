// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
        maven("https://oss.jfrog.org/libs-snapshot")
    }
    dependencies {
        classpath(BuildPlugins.androidGradlePlugin)
        classpath(BuildPlugins.kotlinGradlePlugin)
        classpath(BuildPlugins.gradleVersionsPlugin)
        classpath(BuildPlugins.safeArgsPlugin)
        classpath(BuildPlugins.googleServices)
        classpath(BuildPlugins.firebaseCrashlytics)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
        maven("https://oss.jfrog.org/libs-snapshot")
        maven("http://dl.bintray.com/amulyakhare/maven")
    }
}

tasks.register("clean").configure {
    delete("build")
}