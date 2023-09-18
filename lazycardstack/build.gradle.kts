@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("convention.publication")
}

val libGroup = findProperty("group") as String
val libVersion = findProperty("version") as String

version = libVersion
group = libGroup

android {
    namespace = "com.haroncode.lazycardstack"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    publishing {
        multipleVariants {
            allVariants()
            withSourcesJar()
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.material)

    implementation(libs.compose)
    implementation(libs.foundation)
}
