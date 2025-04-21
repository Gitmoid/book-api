plugins {
    java
    groovy
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Core
    implementation(libs.org.springframework.boot.spring.boot.starter.data.jpa)
    implementation(libs.org.springframework.boot.spring.boot.starter.validation)
    implementation(libs.org.springframework.boot.spring.boot.starter.web)

    // Database
    implementation(libs.org.flywaydb.flyway.core)
    runtimeOnly(libs.org.flywaydb.flyway.postgresql)
    runtimeOnly(libs.org.postgresql.postgresql)

    // Development
    developmentOnly(libs.org.springframework.boot.spring.boot.devtools)
    developmentOnly(libs.org.springframework.boot.spring.boot.docker.compose)

    // Tools & Utilities
    compileOnly(libs.org.projectlombok.lombok)
    annotationProcessor(libs.org.projectlombok.lombok)

    implementation(libs.org.mapstruct.mapstruct)
    implementation(libs.org.projectlombok.lombok.mapstruct.binding)
    annotationProcessor(libs.org.mapstruct.mapstruct.processor)

    // Testing
    testImplementation(libs.org.springframework.boot.spring.boot.starter.test)
    testImplementation(libs.org.spockframework.spock.core)
    testImplementation(libs.org.spockframework.spock.spring)

    // Containers
    testImplementation(libs.org.testcontainers.junit.jupiter)
    testImplementation(libs.org.testcontainers.postgresql)
    testImplementation(libs.org.springframework.boot.spring.boot.testcontainers)
}

tasks.withType<Test> {
    useJUnitPlatform()
}