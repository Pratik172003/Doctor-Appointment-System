plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.doctorappointment"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.doctorappointment"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    viewBinding{
        enable=true
    }
}


dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)

    // Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:35.2.0"))

    // Firebase Cloud Firestore
    implementation("com.google.firebase:firebase-firestore")

    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth")

    // Existing Firebase Realtime Database
    implementation(libs.firebase.database)

    // Glide Image Loading
    implementation("com.github.bumptech.glide:glide:4.13.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
