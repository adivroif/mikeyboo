plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.services)
    id ("androidx.navigation.safeargs.kotlin") version "2.8.3" apply false


}

android {
    namespace = "com.example.mikeyboo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mikeyboo"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.protolite.well.known.types)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.gson)

    //Firebase:
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    //Autentication:
    implementation(libs.firebase.ui.auth)

    //Realtime Database:
    implementation(libs.firebase.database)

    implementation (libs.libphonenumber)

    implementation(libs.firebase.messaging)

    implementation(libs.firebase.storage)

    implementation(libs.okhttp) // או גרסה אחרת

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.glide)
    annotationProcessor(libs.compiler)
    implementation (libs.firebase.messaging.ktx)
    







}