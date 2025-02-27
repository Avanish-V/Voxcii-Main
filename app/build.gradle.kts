plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    kotlin("plugin.serialization") version "1.9.0"

}

android {
    namespace = "com.byteapps.voxcii"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.byteapps.voxcii"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {



        release {

            isMinifyEnabled = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.core.splashscreen)

    implementation("io.coil-kt:coil-compose:2.5.0")

    implementation ("com.google.android.gms:play-services-auth:21.3.0")

//    implementation("io.ktor:ktor-client-core:2.3.12")
//    implementation("io.ktor:ktor-client-cio:2.3.12")
//    implementation ("io.ktor:ktor-client-content-negotiation:2.3.12")
//    implementation ("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
//    implementation("io.ktor:ktor-client-logging:2.3.12")


    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation ("io.insert-koin:koin-androidx-compose:4.0.0")

    implementation ("io.agora.rtc:voice-sdk:4.5.0")
    implementation("commons-codec:commons-codec:1.9")

    implementation("com.airbnb.android:lottie-compose:4.0.0")



}