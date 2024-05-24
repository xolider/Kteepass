import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.dokka)
    id("maven-publish")
}

group = "dev.vicart.keepasskt"
version = "1.0.0-SNAPSHOT"

kotlin {
    jvm {
        jvmToolchain(21)
    }

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

tasks.withType<Test>() {
    useJUnitPlatform()
}