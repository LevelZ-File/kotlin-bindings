plugins {
    kotlin("multiplatform") version "1.9.24"
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
        browser {
            testTask {
                useKarma {
                    useFirefoxHeadless()
                }
            }
        }
        nodejs()
    }

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

tasks {
    clean {
        delete("kotlin-js-store")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "xyz.calcugames"
            artifactId = "levelz-kt"

            pom {
                name = "LevelZ Kotlin API"
                description = "The Kotlin API for LevelZ"
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

            from(components["java"])
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