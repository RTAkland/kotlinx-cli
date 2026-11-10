import org.jetbrains.kotlin.gradle.dsl.JvmTarget

kotlin {
    linuxX64()
    linuxArm64()
    macosArm64()
    mingwX64()
    jvm { compilerOptions.jvmTarget = JvmTarget.JVM_1_8 }

    explicitApi()

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}