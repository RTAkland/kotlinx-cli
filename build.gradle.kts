plugins {
    kotlin("multiplatform") version "2.5.0-Beta2" apply false
    id("maven-publish")
}

allprojects {
    group = "cn.rtast.kotlinx"
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
        publications.withType<MavenPublication> {
            pom {
                name.set("kotlin-cli")
                description.set("Kotlin CLI Library")
                url.set("https://repo.rtast.cn/RTAkland/kotlinx-cli")
                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                developers {
                    developer {
                        id.set("rtakland")
                        name.set("RTAkland")
                    }
                }
            }
        }
    }
}