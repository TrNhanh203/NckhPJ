plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.facilitiesmanagementpj"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.facilitiesmanagementpj"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation (libs.androidx.compose.material3.material3)

    implementation (libs.androidx.activity.compose.v161)
    implementation (libs.androidx.lifecycle.runtime.ktx.v251)

    // Coil để hiển thị hình ảnh
    implementation (libs.coil.compose.v210) // Để hiển thị ảnh
    // Accompanist để cấp quyền
    implementation (libs.accompanist.permissions)

    //google services
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Room Database
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler) // Required for annotation processing
    implementation(libs.androidx.room.ktx) // Hỗ trợ coroutine

    // Lifecycle - ViewModel & LiveData
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // nhan du lieu tra nguoc ve o file themyeucaumoi
    implementation (libs.androidx.runtime.livedata)

    // Coroutine support
    implementation(libs.kotlinx.coroutines.android)


    implementation(platform(libs.androidx.compose.bom.v20231001))

    // Core Compose
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.foundation)
    implementation(libs.material3)
    implementation(libs.androidx.activity.compose.v182)

    // ViewModel trong Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Debugging
    debugImplementation(libs.ui.tooling)

    //hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)


    //navigation
    implementation(libs.androidx.navigation.compose)

    //bộ lọc ngang
    implementation (libs.accompanist.flowlayout)
    

}