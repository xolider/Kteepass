plugins {
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation(rootProject)
}

kotlin {
    jvmToolchain(21)
}

android {
    namespace = "dev.vicart.kteepass.sample.android"

    defaultConfig {
        targetSdk = libs.versions.androidTargetSdk.get().toInt()
        compileSdk = libs.versions.androidCompileSdk.get().toInt()
        minSdk = libs.versions.androidMinSdk.get().toInt()

        applicationId = "dev.vicart.kteepass.sample.android"
    }

    sourceSets["main"].manifest.srcFile("src/main/resources/AndroidManifest.xml")
}