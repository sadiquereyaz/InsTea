import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.googleGmsGoogleServices)
    id("com.google.devtools.ksp")   //room
}

android {
    /*packaging {
        resources {
            excludes += listOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0"
            )
        }
    }*/
    namespace = "in.instea.instea"
    compileSdk = 34

    defaultConfig {
        applicationId = "in.instea.instea"
        minSdk = 26
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
    implementation (libs.androidx.navigation.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)

    implementation(libs.firebase.database)
    implementation(libs.firebase.database.ktx)
    implementation(platform(libs.firebase.bom))
//    implementation("com.google.firebase:firebase-auth")
    implementation(libs.google.firebase.auth)

//    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation(libs.play.services.auth)

//    implementation("androidx.credentials:credentials:1.2.2")
//    implementation(libs.androidx.credentials.v122)
    implementation( libs.androidx.credentials)

//    implementation("androidx.credentials:credentials-play-services-auth:1.2.2")
    implementation(libs.androidx.credentials.play.services.auth.v122)
//    implementation(libs.androidx.credentials.play.services.auth)

//    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
//    implementation(libs.googleid.v111)
    implementation (libs.googleid)

    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.compose.material)
//    implementation(libs.androidx.ui.desktop)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.lifecycle.viewmodel.ktx)  // ViewModel KTX
    implementation(libs.androidx.lifecycle.viewmodel.compose)  // ViewModel Compose
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.room.runtime)
    // To use Kotlin Symbol Processing (KSP)
    ksp(libs.androidx.room.compiler)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.datastore.preferences)    // data store
    implementation(libs.androidx.constraintlayout)
    implementation (libs.android.lottie.compose)
    implementation(libs.androidx.work.runtime.ktx)  //workManager
//    implementation(libs.jsoup)      //web scraping
    implementation("net.sourceforge.htmlunit:htmlunit-android:2.63.0")  // screen scraping
//    implementation("xml-apis:xml-apis:1.4.01")
}