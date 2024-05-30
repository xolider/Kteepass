plugins {
    kotlin("jvm")
    alias(libs.plugins.composeDesktop)
    alias(libs.plugins.kotlinCompose)
}

dependencies {
    implementation(rootProject)
    implementation(compose.foundation)
    implementation(compose.runtime)
    implementation(compose.ui)
    implementation(compose.desktop.currentOs)
}

compose.desktop {
    application {
        mainClass = "dev.vicart.kteepass.sample.jvm.MainKt"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}