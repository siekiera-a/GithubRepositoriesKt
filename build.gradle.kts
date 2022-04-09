import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
}

object Versions {
    const val ktorVersion = "1.6.8"
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
    testImplementation(
        group = "org.springframework.boot",
        name = "spring-boot-starter-test"
    )
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
