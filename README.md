# IVK Kotlin SDK
This repository contains a Kotlin SDK for the IVK Platform. For now, only IVK Skill is available.
The bulk of it is generated using the `openapi-generator`.

The resulting source is wrapped with a custom `SkillSdk` class to provide improved DX and extra functionality.

## Features
- Includes types & models
- Fully typed methods
- Generated from the most recent OpenAPI spec
- Configurable retry mechanism (optional)
- Configurable local fallback (COMING SOON)
- Support for coroutines / CompletableFuture / blocking

## How to use
The SDK is available on Maven Central.

### Gradle (Kotlin DSL):
```kotlin
dependencies {
    implementation("dev.ivk:ivk-skill-sdk:<version>")
}
```

### Gradle (Groovy DSL):
```groovy
dependencies {
    implementation "dev.ivk:ivk-skill-sdk:<version>"
}
```

### Maven:
```xml
<dependency>
  <groupId>dev.ivk</groupId>
  <artifactId>ivk-skill-sdk</artifactId>
  <version>VERSION</version>
</dependency>
```

Replace `<version>` or `VERSION` with the release you want to use. Check [Maven Central](https://central.sonatype.com/artifact/dev.ivk/ivk-skill-sdk) for available versions.

Alternatively, you can download a prebuilt JAR from GitHub Releases and import it into your project.

> The `example` folder contains a simple project which demonstrates how the SkillSdk class can be initialized and used.

## How to build from source
```
./gradlew :skill-sdk:build
```

## How to generate (requires docker & just)
```
just generate-sdk
```
