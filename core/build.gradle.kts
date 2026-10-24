@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

kotlin {
    linuxX64()
    linuxArm64()
    macosArm64()
    mingwX64()
    jvm { compilerOptions.jvmTarget = JvmTarget.JVM_1_8 }
    js {
        browser()
        nodejs()
        binaries.library()
    }
    wasmJs {
        browser()
        nodejs()
        binaries.library()
    }

    explicitApi()

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }

    compilerOptions.freeCompilerArgs.addAll("-Xcompanion-blocks-and-extensions")
}