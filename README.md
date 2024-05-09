# levelz-kt

> Alternative Kotlin Bindings designed for Kotlin Multiplatform

[![JitPack](https://jitpack.io/v/LevelZ-File/kotlin-bindings.svg)](https://jitpack.io/#LevelZ-File/kotlin-bindings)
![GitHub Release](https://img.shields.io/github/v/release/LevelZ-File/kotlin-bindings)

## Why?

The official [java-bindings](https://github.com/LevelZ-File/java-bindings) are designed to be implemented on
the JVM. This project is designed to be implemented on Kotlin Multiplatform.

**They are intended to be functionally the same, and to not be used together.**

### Download

```xml
<!-- Add Calculus Games Repository -->

<repositories>
    <repository>
        <id>calculus-releases</id>
        <url>https://repo.calcugames.xyz/repository/maven-releases/</url>
    </repository>
    <repository>
        <id>calculus-snapshots</id>
        <url>https://repo.calcugames.xyz/repository/maven-snapshots/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>xyz.calcugames</groupId>
        <artifactId>levelz-kt</artifactId>
        <version>[VERSION]</version>
    </dependency>
</dependencies>
```

Gradle (Groovy)
```groovy
// Add Calculus Games Repositories
repositories {
    maven { url 'https://repo.calcugames.xyz/repository/maven-releases/' }
    maven { url 'https://repo.calcugames.xyz/repository/maven-snapshots/' }
}

dependencies {
    implementation 'xyz.calcugames:levelz-kt:[VERSION]'
}
```

Gradle (Kotlin DSL)
```kts
// Add Calculus Games Repository
repositories {
    maven("https://repo.calcugames.xyz/repository/maven-releases/")
    maven("https://repo.calcugames.xyz/repository/maven-snapshots/")
}

dependencies {
    implementation("xyz.calcugames:levelz-kt:[VERSION]")
}
```

## Usage
```kotlin
val (x, y) = Coordinate2D(1, 2)
val (x, y, z) = Coordinate3D(1, 2, 3)

println(x) // 1
println(y) // 2
```

```kotlin
val levelString = """
    @type 2\n
    @spawn [0, 1]\n
    @scroll none\n
    ---
    grass: [0, 0]*[0, 1]
"""

val level = parseLevel(levelString) as Level2D

println(level.scroll) // Scroll.NONE
```
