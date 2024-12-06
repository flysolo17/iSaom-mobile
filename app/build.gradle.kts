plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.ketchupzzz.isaom"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ketchupzzz.isaom"
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
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    //courotines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    //Google fonts
    implementation("androidx.compose.ui:ui-text-google-fonts:1.6.7")

    //navagation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    //viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")


    //hilt
    implementation("com.google.dagger:hilt-android:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("com.google.dagger:hilt-compiler:2.48")


    //window size
    implementation ("androidx.compose.material3:material3-window-size-class:1.2.1")
    implementation("io.coil-kt:coil-compose:2.6.0")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")



    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")


    //rich text editor
    implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-rc06")


    implementation("androidx.compose.material:material-icons-extended:1.5.3")



    implementation("de.charlex.compose.material3:material3-html-text:2.0.0-beta01")

    //ml kit
    implementation ("com.google.mlkit:text-recognition:16.0.1")

    //charts
    implementation ("io.github.ehsannarmani:compose-charts:0.0.13")


    // Paging
    implementation ("androidx.paging:paging-runtime-ktx:3.1.1")
    implementation ("androidx.paging:paging-compose:1.0.0-alpha18")


//    // Room
//    implementation ("androidx.room:room-ktx:2.5.1")
//    kapt ("androidx.room:room-compiler:2.5.1")
//    implementation ("androidx.room:room-paging:2.5.1")
    implementation("io.github.grizzi91:bouquet:1.1.2")
}


kapt {
    correctErrorTypes = true
}