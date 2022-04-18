import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("idea")
    id("org.springframework.boot") version "2.6.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
}

object Versions {
    const val ktorVersion = "1.6.8"
    const val kotestVersion = "5.2.3"
}

idea {
    module {
        isDownloadJavadoc = true
    }
}

group = "pl.siekiera.arkadiusz"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation(
        group = "org.springframework.boot",
        name = "spring-boot-starter-web"
    )
    implementation(
        group = "com.fasterxml.jackson.module",
        name = "jackson-module-kotlin"
    )
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-reflect")
    implementation(group = "org.jetbrains.kotlin", name = "kotlin-stdlib-jdk8")
    implementation(
        group = "io.ktor",
        name = "ktor-client-core",
        version = Versions.ktorVersion
    )
    implementation(
        group = "io.ktor",
        name = "ktor-client-cio",
        version = Versions.ktorVersion
    )
    implementation(
        group = "io.ktor",
        name = "ktor-client-jackson",
        version = Versions.ktorVersion
    )
    implementation(
        group = "org.jetbrains.kotlinx",
        name = "kotlinx-coroutines-core-jvm",
        version = "1.6.1"
    )
    runtimeOnly(
        group = "org.jetbrains.kotlinx",
        name = "kotlinx-coroutines-reactor",
        version = "1.6.1"
    )

    testImplementation(
        group = "org.springframework.boot",
        name = "spring-boot-starter-test"
    )

    testImplementation(
        group = "io.kotest",
        name = "kotest-runner-junit5",
        version = Versions.kotestVersion
    )
    testImplementation(
        group = "io.kotest",
        name = "kotest-assertions-core",
        version = Versions.kotestVersion
    )
    testImplementation(
        group = "io.kotest",
        name = "kotest-property",
        version = Versions.kotestVersion
    )
    testImplementation(
        group = "org.jetbrains.kotlinx",
        name = "kotlinx-coroutines-test",
        version = "1.6.1"
    )
    testImplementation(
        group = "com.github.tomakehurst",
        name = "wiremock-jre8",
        version = "2.33.1"
    )
    testImplementation(
        group = "org.amshove.kluent",
        name = "kluent",
        version = "1.68"
    )
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-opt-in=kotlin.RequiresOptIn")
        jvmTarget = "${JavaVersion.VERSION_17}"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    filter {
        includeTestsMatching("integration.*")
        includeTestsMatching("unit.*")
    }
}