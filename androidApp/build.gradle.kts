import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

val appId = "uk.co.lidbit.pusher.kmp.example.android"

dependencies {
    implementation(projects.example)
}

kotlin {
    android {
        namespace = appId
        compileSdk =
            libs.versions.android.compileSdk
                .get()
                .toInt()

        defaultConfig {
            applicationId = appId
            minSdk =
                libs.versions.android.minSdk
                    .get()
                    .toInt()
            targetSdk =
                libs.versions.android.targetSdk
                    .get()
                    .toInt()
            versionCode = 1
            versionName = "1.0"
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
        buildFeatures {
            buildConfig = true
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        lint {
            checkReleaseBuilds = false
        }
    }
}
