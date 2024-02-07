plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.featuresapper"
    compileSdk = 34

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    ksp("com.google.dagger:dagger-compiler:2.50")   // Dagger compiler
    ksp("com.google.dagger:hilt-compiler:2.50")   // Hilt compiler
    implementation("com.google.dagger:hilt-android:2.50")
}