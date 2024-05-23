plugins {
    kotlin("jvm")
    id("application")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(rootProject)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "dev.vicart.keepasskt.sample.MainKt"
}