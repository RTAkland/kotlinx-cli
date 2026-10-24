plugins {
    kotlin("multiplatform") version "2.4.21" apply false
    id("maven-publish")
}

allprojects {
    group = "org.jetbrains.kotlinx"
    version = property("version")!!

    repositories {
        mavenCentral()
    }
}

subprojects {
    pluginManager.apply("org.jetbrains.kotlin.multiplatform")
    pluginManager.apply("maven-publish")

    publishing {
        repositories {
            maven("https://repo.rtast.cn/packages") {
                credentials(HttpHeaderCredentials::class) {
                    name = "Authorization"
                    val token = providers.gradleProperty("PUBLISH_TOKEN")
                        .orElse(providers.environmentVariable("PUBLISH_TOKEN"))
                        .orNull
                    value = "Bearer $token"
                }
                authentication {
                    create<HttpHeaderAuthentication>("header")
                }
            }
        }
    }
}