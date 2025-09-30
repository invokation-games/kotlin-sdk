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
You can consume the SDK from GitHub Packages.

Gradle (Groovy DSL):
```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/invokation-games/kotlin-sdk")
        credentials {
            // Prefer environment variables; do not hardcode secrets
            username = System.getenv("GITHUB_ACTOR") ?: "<github-username>"
            password = System.getenv("GITHUB_TOKEN") ?: "<github-token-or-classic-pat>"
        }
    }
}
dependencies {
    implementation "dev.ivk:ivk-skill-sdk:<version>"
}
```

Maven:

Add credentials in your ~/.m2/settings.xml:
```xml
<settings>
  <servers>
    <server>
      <id>github</id>
      <username>${env.GITHUB_ACTOR}</username>
      <password>${env.GITHUB_TOKEN}</password>
    </server>
  </servers>
</settings>
```

Add repository and dependency in your pom.xml:
```xml
<project>
  <repositories>
    <repository>
      <id>github</id>
      <name>GitHub Packages</name>
      <url>https://maven.pkg.github.com/invokation-games/kotlin-sdk</url>
    </repository>
  </repositories>

  <dependencies>
    <dependency>
      <groupId>dev.ivk</groupId>
      <artifactId>ivk-skill-sdk</artifactId>
      <version>VERSION</version>
    </dependency>
  </dependencies>
</project>
```

Replace VERSION to the release you want to use.

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
