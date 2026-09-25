import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    // AGP 9 has built-in Kotlin support; applying org.jetbrains.kotlin.android now fails the build.
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "com.github.eltohamy.materialhijricalendarview"
    compileSdk = 37

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_18)
        }
    }
    buildFeatures {
        compose = true
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        "io.github.eltohamy",
        "material-hijri-calendarview",
        "2.0.1"
    )

    // Keep the existing pom configuration.
}

dependencies {
    implementation(libs.ummalqura.calendar)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.core.ktx)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}