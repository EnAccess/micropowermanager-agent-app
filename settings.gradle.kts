pluginManagement {

    /**
     * The pluginManagement.repositories block configures the
     * repositories Gradle uses to search or download the Gradle plugins and
     * their transitive dependencies. Gradle pre-configures support for remote
     * repositories such as JCenter, Maven Central, and Ivy. You can also use
     * local repositories or define your own remote repositories. Here we
     * define the Gradle Plugin Portal, Google's Maven repository,
     * and the Maven Central Repository as the repositories Gradle should use to look for its
     * dependencies.
     */

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {

    /**
     * The dependencyResolutionManagement.repositories
     * block is where you configure the repositories and dependencies used by
     * all modules in your project, such as libraries that you are using to
     * create your application. However, you should configure module-specific
     * dependencies in each module-level build.gradle file. For new projects,
     * Android Studio includes Google's Maven repository and the Maven Central
     * Repository by default, but it does not configure any dependencies (unless
     * you select a template that requires some).
     */

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

include(":app")
include(":core")
include(":core_ui")
include(":core_network")
include(":core_network_auth")
include(":core_network_no_auth")
include(":core_localization")
include(":feature_login")
include(":feature_main")
include(":feature_dashboard")
include(":feature_customers")
include(":feature_payment")
include(":feature_appliance")
include(":feature_ticket")
include(":shared_navigation")
include(":shared_agent")
include(":shared_success")
include(":shared_customer")
include(":shared_messaging")
