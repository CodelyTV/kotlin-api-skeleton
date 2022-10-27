<p align="center">
  <a href="https://codely.com">
    <img src="https://user-images.githubusercontent.com/10558907/170513882-a09eee57-7765-4ca4-b2dd-3c2e061fdad0.png" width="300px" height="92px" alt="Codely logo"/>
  </a>
</p>

<h1 align="center">
  💎 Kotlin HTTP API Skeleton
</h1>

<p align="center">
    <a href="https://github.com/CodelyTV/kotlin-api-skeleton/actions/workflows/ci.yml"><img src="https://github.com/CodelyTV/kotlin-api-skeleton/actions/workflows/ci.yml/badge.svg" alt="Build status"/></a>
    <a href="https://github.com/CodelyTV"><img src="https://img.shields.io/badge/CodelyTV-OS-green.svg?style=flat-square" alt="Codely Open Source"/></a>
    <a href="https://pro.codely.com"><img src="https://img.shields.io/badge/CodelyTV-PRO-black.svg?style=flat-square" alt="CodelyTV Courses"/></a>
</p>

<p align="center">
  Template intended to serve as a starting point if you want to <strong>bootstrap a Kotlin HTTP API</strong>.
  <br />
  <br />
  Take a look, play and have fun with this.
  <a href="https://github.com/CodelyTV/kotlin-api-skeleton/stargazers">Stars are welcome 😊</a>
</p>

This is a repository intended to serve as a starting point if you want to bootstrap an API in Kotlin.

## ☝️ Introduction

It could be useful if you want to start from scratch a kata or a little exercise or project. The idea is that you don't have to worry about the boilerplate
* Latest stable kotlin version
* Latest stable Springboot version
* Latest stable Springboot version
* Latest stable java version
* Best practices applied
* Some useful resources to start coding

## 🌎 How To Start

You could manually clone [this repo](https://github.com/CodelyTV/kotlin-api-skeleton) or just us it as a template

### Cloning the repository

We recommend to follow the next step by step process in order to avoid adding the bootstrap project commits to your project Git history:

1. [Use this repositoy template](https://github.com/CodelyTV/kotlin-api-skeleton/generate)
2. Clone your project
3. Move to the project directory: `cd your-project-name`
4. Build the project for the first time: `./gradlew build`
5. Run all the checks: `./gradlew check`. This will do some checks that you can perform with isolated commands:
    1. [Klint](https://ktlint.github.io/) using [Spotless](https://github.com/diffplug/spotless): `./gradlew spotlessCheck`. If you want to fix style issues automatically: `./gradlew spotlessApply`.
    2. [Kotlin test](https://kotlinlang.org/api/latest/kotlin.test/): `./gradlew test`.
6. To just run the project execute: `./gradlew run`
7. Start coding!

## 👌 Helpful resources

### Kotlin

* [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
* [Comparison between Kotlin and Java](https://kotlinlang.org/docs/comparison-to-java.html)

### Kotlin test

* [Test code using JUnit in JVM - tutorial](https://kotlinlang.org/docs/jvm-test-using-junit.html)
* [JUnit5 assertions](https://junit.org/junit5/docs/5.0.1/api/org/junit/jupiter/api/Assertions.html)

## 👌 Codely Code Quality Standards

Publishing this package we are committing ourselves to the following code quality standards:

- 🤝 Respect **Semantic Versioning**: No breaking changes in patch or minor versions
- 🤏 No surprises in transitive dependencies: Use the **bare minimum dependencies** needed to meet the purpose
- 🎯 **One specific purpose** to meet without having to carry a bunch of unnecessary other utilities
- ✅ **Tests** as documentation and usage examples
- 📖 **Well documented ReadMe** showing how to install and use
- ⚖️ **License favoring Open Source** and collaboration

## 🔀 Related skeleton templates

Opinionated TypeScript skeletons ready for different purposes:

- [🔷🌱 TypeScript Basic Skeleton](https://github.com/CodelyTV/typescript-basic-skeleton)
- [🔷🕸️ TypeScript Web Skeleton](https://github.com/CodelyTV/typescript-web-skeleton)
- [🔷🌍 TypeScript API Skeleton](https://github.com/CodelyTV/typescript-api-skeleton)
- [🔷✨ TypeScript DDD Skeleton](https://github.com/CodelyTV/typescript-ddd-skeleton)

This very same basic skeleton philosophy implemented in other programming languages:

- [✨ JavaScript Basic Skeleton](https://github.com/CodelyTV/javascript-basic-skeleton)
- [☕ Java Basic Skeleton](https://github.com/CodelyTV/java-basic-skeleton)
- [📍 Kotlin Basic Skeleton](https://github.com/CodelyTV/kotlin-basic-skeleton)
- [🧬 Scala Basic Skeleton](https://github.com/CodelyTV/scala-basic-skeleton)
- [🦈 C# Basic Skeleton](https://github.com/CodelyTV/csharp-basic-skeleton)
- [🐘 PHP Basic Skeleton](https://github.com/CodelyTV/php-basic-skeleton)
