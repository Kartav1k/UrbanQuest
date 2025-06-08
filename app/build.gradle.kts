import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0"
    alias(libs.plugins.googleFirebaseFirebasePerf)
}

android {
    namespace = "com.example.urbanquest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.urbanquest"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"



        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "MAPKIT_API_KEY", "\"${getMapkitApiKey()}\"")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${layout.buildDirectory.get().asFile}/compose_metrics",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${layout.buildDirectory.get().asFile}/compose_metrics"
        )
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions{
        unitTests.isIncludeAndroidResources = true
    }

}


fun getMapkitApiKey(): String {
    val properties = Properties()
    project.file("../local.properties").inputStream().use { properties.load(it) }
    val value = properties.getProperty("MAPKIT_API_KEY", "")
    if (value.isEmpty()) {
        throw InvalidUserDataException("MapKit API key is not provided. Set your API key in the project's local.properties file: `MAPKIT_API_KEY=<your-api-key-value>`.")
    }
    return value
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
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.material3.android)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.perf)
    implementation(libs.jetbrains.kotlinx.collections.immutable)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.arch.core.testing)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.arch.core.testing)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.firebase.analytics)
    implementation (libs.androidx.runtime.livedata)
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.picasso)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation (libs.gson)
    implementation (libs.coil.compose)


    implementation(libs.map.kit)
    testImplementation(kotlin("test"))
}

