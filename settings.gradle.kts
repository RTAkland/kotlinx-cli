pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "Kotlinx-Cli-Library"

include(":core")
project(":core").name="kotlinx-cli"
