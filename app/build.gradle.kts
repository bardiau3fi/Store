plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.navigationsafeargs)
    alias(libs.plugins.daggerhiltandroid)
    id("kotlin-kapt")
    id("kotlin-parcelize")
   alias(libs.plugins.googlegms)
}

android {
    namespace = "com.example.storeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.storeapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        viewBinding=true
        dataBinding=true
        buildConfig = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
           // buildConfigField("String","VERSION_NAME","\"${defaultConfig.versionName}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    //Navigation
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)

    //Dagger - Hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)

    //DataStore
    implementation (libs.androidx.datastore.preferences)

    //Retrofit
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    //OkHTTP client
    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)
    //Receive OTP
    implementation (libs.play.services.base.v1820)
    implementation (libs.play.services.auth.api.phone.v1801)
    //Coroutines
    implementation (libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)

    //Lifecycle
    implementation (libs.androidx.lifecycle.extensions)
    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)

    //Image Loading
    implementation (libs.coil)
    //Gson
    implementation (libs.gson)

    //Calligraphy
    implementation (libs.calligraphy3)
    implementation (libs.viewpump)

    //Glide
    implementation (libs.glide)

    //Receive OTP
    implementation (libs.play.services.base)
    implementation (libs.play.services.auth.api.phone)

    //Other
    implementation (libs.shimmer)
    implementation (libs.shimmer.recyclerview)
    implementation (libs.lottie)
    implementation (libs.dynamicSizes)
    implementation (libs.pinview)
    implementation (libs.circleindicator)
    implementation (libs.readmore.textview)
    implementation (libs.mpAndroidChart)
    implementation (libs.persian.date.picker.dialog)
    implementation (libs.ssImagePicker)
    implementation (libs.carouselrecyclerview)




}