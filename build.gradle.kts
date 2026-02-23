import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import kotlin.apply

plugins {
    kotlin("multiplatform") version "2.3.10"
    id("org.jetbrains.dokka") version "2.1.0"

    `maven-publish`
    jacoco
}

val v = "0.3.5"

group = "xyz.calcugames"
version = "${if (project.hasProperty("snapshot")) "$v-SNAPSHOT" else v}${project.findProperty("suffix")?.toString()?.run { "-${this}" } ?: ""}"
description = "Kotlin Multiplatform API for the LevelZ File Format"

repositories {
    mavenCentral()
    mavenLocal()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

kotlin {
    configureSourceSets()
    applyDefaultHierarchyTemplate()
    withSourcesJar()

    jvm()
    js {
        browser {
            testTask {
                enabled = false
            }
        }
        nodejs()
        generateTypeScriptDefinitions()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            testTask {
                enabled = false
            }
        }
        nodejs {
            testTask {
                useMocha {
                    timeout = "10m"
                }
            }
        }
        generateTypeScriptDefinitions()
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmWasi {
        nodejs {
            testTask {
                useMocha {
                    timeout = "10m"
                }
            }
        }
    }

    androidNativeX64()
    androidNativeX86()
    androidNativeArm32()
    androidNativeArm64()

    mingwX64()
    linuxArm64()
    linuxX64()

    macosX64()
    macosArm64()
    iosX64()
    iosSimulatorArm64()
    iosArm64()
    tvosX64()
    tvosArm64()
    watchosX64()
    watchosSimulatorArm64()
    watchosDeviceArm64()

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

fun KotlinMultiplatformExtension.configureSourceSets() {
    sourceSets
        .matching { it.name !in listOf("main", "test") }
        .all {
            val srcDir = if ("Test" in name) "test" else "main"
            val resourcesPrefix = if (name.endsWith("Test")) "test-" else ""
            val platform = when {
                (name.endsWith("Main") || name.endsWith("Test")) && "android" !in name -> name.dropLast(4)
                else -> name.substringBefore(name.first { it.isUpperCase() })
            }

            kotlin.srcDir("src/$platform/$srcDir")
            resources.srcDir("src/$platform/${resourcesPrefix}resources")
            resources.srcDir("src/$platform/generated-resources")

            languageSettings.apply {
                progressiveMode = true
            }
        }
}

tasks {
    clean {
        delete("kotlin-js-store")
    }

    register("jvmJacocoTestReport", JacocoReport::class) {
        dependsOn("jvmTest")

        classDirectories.setFrom(layout.buildDirectory.file("classes/kotlin/jvm/"))
        sourceDirectories.setFrom("src/commonMain/kotlin/", "src/jvmMain/kotlin/")
        executionData.setFrom(layout.buildDirectory.files("jacoco/jvmTest.exec"))

        reports {
            xml.required.set(true)
            xml.outputLocation.set(layout.buildDirectory.file("jacoco.xml"))

            html.required.set(true)
            html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
        }
    }
}

publishing {
    publications {
        getByName<MavenPublication>("kotlinMultiplatform") {
            pom {
                name = "LevelZ Kotlin API"
                url = "https://levelz.calcugames.xyz"

                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/LevelZ-File/kotlin-bindings.git"
                    developerConnection = "scm:git:ssh://github.com/LevelZ-File/kotlin-bindings.git"
                    url = "https://github.com/LevelZ-File/kotlin-bindings"
                }
            }
        }
    }

    repositories {
        maven {
            credentials {
                username = System.getenv("NEXUS_USERNAME")
                password = System.getenv("NEXUS_PASSWORD")
            }

            val releases = "https://repo.calcugames.xyz/repository/maven-releases/"
            val snapshots = "https://repo.calcugames.xyz/repository/maven-snapshots/"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshots else releases)
        }
    }
}
