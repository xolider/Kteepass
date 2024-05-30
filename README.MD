![Static Badge](https://img.shields.io/badge/Kotlin-2.0.0-7F52FF?logo=kotlin)
![Maven Central Version](https://img.shields.io/maven-central/v/dev.vicart.keepasskt/keepass-kt)
![GitHub last commit](https://img.shields.io/github/last-commit/xolider/keepass-kt)
![Static Badge](https://img.shields.io/badge/JVM-supported-green)
![Static Badge](https://img.shields.io/badge/Android-supported-green)
![Static Badge](https://img.shields.io/badge/Windows-not_supported-red)
![Static Badge](https://img.shields.io/badge/Linux-not_supported-red)
![Static Badge](https://img.shields.io/badge/MacOS-not_supported-red)
![Static Badge](https://img.shields.io/badge/JS-not_supported-red)

# Kteepass (WIP)

Kteepass is a new library written in Kotlin for manipulating KDBX files. The goal of this library is to add full support of KDBX file
format and taking advantage of all of its features and possibilities. It will also add some useful features 
out-of-the-box like loading databases from FTP, HTTP, WebDAV or Cloud providers.

The currently available features are:
* Reading, parsing and decrypting KDBX file (ver 4.x)

Features that are in progress:
* Implementing AES and Argon2id KDF
* Implementing ChaCha20 decryption
* Implementing [process memory protection](https://keepass.info/help/base/security.html#secmemprot)
* KDBX file saving
* Database initialization

## Multiplatform

Kteepass uses Kotlin Multiplatform plugin to write a single codebase and making it accessible for most platform as possible. 
Currently, it only supports Java/JVM and Android. More platforms will be added once the library becomes stable.

### Currently supported platforms:

- [x] JVM
- [x] Android

## Setting up the library in your project

Depending on your project's structure and targets, you may need different dependencies to implement the Kteepass library. 
Kteepass follows the KMP naming convention for artifacts (e.g: kteepass-jvm for the JVM target).

### Kotlin Multiplatform Project

when working with KMP, you can directly include the base artifact in your `commonMain` source set. KMP will resolve itself 
the right dependencies to use for each of your targets:

```kotlin
commonMain.dependencies {
    implementation("dev.vicart.kteepass:kteepass:<version>")
}
```

### Java/Kotlin (JVM)

```kotlin
dependencies {
    implementation("dev.vicart.kteepass:kteepass-jvm:<version>")
}
```

### Android

```kotlin
dependencies {
    implementation("dev.vicart.kteepass:kteepass-android:<version>")
}
```

## How to use
/

## Contributing

Please read out [contributing guide](https://github.com/xolider/Kteepass/blob/main/CONTRIBUTING.MD)