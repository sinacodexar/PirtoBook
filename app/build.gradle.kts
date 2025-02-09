import io.netty.util.ReferenceCountUtil.release

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {

    namespace = "ir.sina.pirtobook"
    compileSdk = 35

    defaultConfig {
        applicationId = "ir.sina.pirtobook"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.2"



        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = file("E:\\ir.pirto.jks") // مسیر Keystore
            storePassword = "sinagamer502" // پسورد Keystore
            keyAlias = "key13" // نام alias
            keyPassword = "sinagamer502" // پسورد alias
        }
    }


    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")

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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    implementation("com.google.firebase:firebase-database-ktx")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    // Lottie
    //noinspection UseTomlInstead
    implementation("com.airbnb.android:lottie-compose:6.1.0")

    //Koin
    implementation("dev.burnoo:cokoin:1.0.0")
    implementation("dev.burnoo:cokoin-android-viewmodel:1.0.0")
    implementation("dev.burnoo:cokoin-android-navigation:1.0.0")

    // Compose
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui:1.5.0")
    implementation("androidx.compose.material:material:1.5.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.6.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coil
    implementation("io.coil-kt:coil-compose:2.2.2")

    // system ui controller
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.1")

    implementation("com.google.firebase:firebase-storage-ktx")

    implementation ("com.squareup.okhttp3:okhttp:4.9.3")


}