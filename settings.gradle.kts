pluginManagement {
    repositories {
        google() // ✅ Required for Compose Compiler and Android plugins
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google() // ✅ This is essential for resolving Compose Compiler and AndroidX
        mavenCentral()
        maven("https://jitpack.io") // Optional, for any GitHub-hosted dependencies
    }
}

rootProject.name = "EDCLoggerJETPACK"
include(":app")
