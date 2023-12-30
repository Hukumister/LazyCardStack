plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlinCocoapods)
}

group = "com.haroncode.sample.ui"

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
@Suppress("unused")
kotlin {
    applyDefaultHierarchyTemplate()
    jvmToolchain(17)

    androidTarget {
        publishLibraryVariants("release")
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Lazy Card Stack - Jetpack compose tinder like card stack."
        homepage = "https://github.com/Hukumister/LazyCardStack"
        version = "1.0"
        ios.deploymentTarget = "12.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = project.name
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.lazycardstack)

            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(libs.kamel.image)
        }
        androidMain.dependencies {
            implementation(libs.activity.compose)
        }
    }

    targets.all {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs += listOf("-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi")
            }
        }
    }
}

android {
    namespace = group.toString()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
}

