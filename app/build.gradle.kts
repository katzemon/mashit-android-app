import java.util.Properties
import com.android.build.api.dsl.ApplicationExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.2.21"
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

configure<ApplicationExtension> {
    namespace = "com.mashiverse.mashit"
    compileSdk = 36

    packaging {
        resources {
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/INDEX.LIST",
                "META-INF/*.kotlin_module",
                "META-INF/FastDoubleParser-LICENSE",
                "META-INF/FastDoubleParser-NOTICE",
                "META-INF/io.netty.versions.properties"
            )
        }
    }

    androidResources {
        @Suppress("UnstableApiUsage")
        localeFilters += "en"
    }

    defaultConfig {
        applicationId = "com.mashiverse.mashit"
        minSdk = 28
        targetSdk = 37
        versionCode = 36
        versionName = "03.06.2026"

        val localProperties = Properties()
        val localPropertiesFile = File(rootDir, "keys.properties")
        if (localPropertiesFile.exists() && localPropertiesFile.isFile) {
            localPropertiesFile.inputStream().use {
                localProperties.load(it)
            }
        }

        buildConfigField("String", "ALCHEMY_API_KEY", localProperties.getProperty("ALCHEMY_API_KEY") ?: "\"\"")
        buildConfigField("String", "MASH_IT_API_KEY", localProperties.getProperty("MASH_IT_API_KEY") ?: "\"\"")
        buildConfigField("String", "REOWN_ID", localProperties.getProperty("REOWN_ID") ?: "\"\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.litert.metadata)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.timber)

    // Json
    implementation(libs.kotlinx.serialization.json)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Coinbase
    implementation(libs.coinbase.wallet.sdk)
    implementation(libs.core)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Local DB
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.paging)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.coil.gif)
    implementation(libs.coil.svg)

    // Paging
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime)

    // Reown
    implementation(platform(libs.android.bom))
    implementation(libs.android.core)
    implementation(libs.appkit)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)

    // Workers
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    // Icons
    implementation(libs.androidx.compose.material.icons.extended)
}
