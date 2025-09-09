# IVK Kotlin SDK
This repository contains a Kotlin SDK for the IVK Platform. For now, only IVK Skill is available.
The bulk of it is generated using the `openapi-generator`.

The resulting source is wrapped with a custom `SkillSdk` class to provide improved DX and extra functionality.

## Features
- Types & Models
- Fully typed methods
- Generated from the most recent OpenAPI spec
- Built-in configurable retry mechanism (optional)
- Built-in Configurable local fallback (optional)
- Support for coroutines

## How to use
You can find a prebuilt JAR under our Github Releases.
Download it and import it in your project.

Alternatively, you can build from source and import your locally build JAR.

The `example` folder contains a simple example project which demonstrates how the SkillSdk class can be initialized and used.

## How to build from source
```
./gradlew :skill-sdk:build
```

## How to generate (requires docker & just)
```
just generate-sdk
```


