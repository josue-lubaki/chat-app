import com.android.build.api.variant.BuildConfigField
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
}

androidComponents {
    val properties = Properties()
    val localFile = project.rootProject.file("local.properties")
    if(localFile.exists()){
        localFile.inputStream().use { properties.load(it) }
    }

    val firebaseURL = System.getenv("FIREBASE_URL") ?: properties.getProperty("FIREBASE_URL")
    val firebaseApiKey = System.getenv("FIREBASE_API_KEY") ?: properties.getProperty("FIREBASE_API_KEY")

    onVariants(selector().withBuildType("debug")) {
        it.buildConfigFields.put("FIREBASE_URL", BuildConfigField(
            type = "String",
            value = "\"$firebaseURL\"",
            comment = "FCM URL"
        ))

        it.buildConfigFields.put("FIREBASE_API_KEY", BuildConfigField(
            type = "String",
            value = "\"$firebaseApiKey\"",
            comment = "FCM URL"
        ))
    }

    onVariants(selector().withBuildType("release")) {
        it.buildConfigFields.put("FIREBASE_URL", BuildConfigField(
            type = "String",
            value = "\"$firebaseURL\"",
            comment = "FCM URL"
        ))

        it.buildConfigFields.put("FIREBASE_API_KEY", BuildConfigField(
            type = "String",
            value = "\"$firebaseApiKey\"",
            comment = "FCM URL"
        ))
    }
}

android {
    namespace = "ca.josue_lubaki.common"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        configurations.all {
            resolutionStrategy {
                force("androidx.emoji2:emoji2-views-helper:1.3.0")
                force("androidx.emoji2:emoji2:1.3.0")
            }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material.icons.extended)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.accompanist.permissions)

    // navigation compose
    implementation(libs.androidx.navigation.compose)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)

    // coil
    implementation(libs.coil.compose)

    implementation(libs.window.size)
    implementation(libs.accompanist.adaptive)

    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)

    // coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp.logging)

    // firebase dependencies
    implementation(platform("com.google.firebase:firebase-bom:32.4.0"))
    implementation(libs.firebase.core)
    implementation("com.google.firebase:firebase-database")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-messaging")

    // DataStore Preferences
    implementation(libs.androidx.datastore.preferences)
}