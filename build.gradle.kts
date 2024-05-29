import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.dokka)
    alias(libs.plugins.androidLibrary)
    id("maven-publish")
}

group = "dev.vicart.kteepass"
version = "1.0.0-SNAPSHOT"

kotlin {
    jvmToolchain(21)
    jvm()
    androidTarget {
        publishLibraryVariants("release")
    }

    sourceSets {
        val commonMain by getting

        val commonJvmMain by creating {
            dependsOn(commonMain)
        }

        val androidMain by getting {
            dependsOn(commonJvmMain)
        }
        val jvmMain by getting {
            dependsOn(commonJvmMain)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        commonJvmMain.dependencies {
            implementation(libs.bouncycastle)
        }
    }
}

android {
    compileSdk = libs.versions.androidCompileSdk.get().toInt()
    namespace = "dev.vicart.kteepass"

    defaultConfig {
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

publishing {
    repositories {
        maven {
            val snapshotUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots"
            val releaseUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            url = uri(if(version.toString().endsWith("-SNAPSHOT")) snapshotUrl else releaseUrl)
            credentials {
                username = System.getProperty("maven.user")
                password = System.getProperty("maven.password")
            }
        }
    }
}