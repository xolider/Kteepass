plugins {
    id("application")
    kotlin("jvm")
}

dependencies {
    implementation(rootProject)
}

application {
    mainClass = "dev.vicart.kteepass.sample.jvm.MainKt"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}