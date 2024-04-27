plugins {
    kotlin("multiplatform") version "1.9.23"
    id("org.jetbrains.dokka") version "1.9.20"

    java
    `maven-publish`
    jacoco
}

group = "xyz.calcugames"
version = "0.1.0"

repositories {
    mavenCentral()
    mavenLocal()
}

kotlin {
    jvm()
    js {
        browser()
        nodejs()
    }
}