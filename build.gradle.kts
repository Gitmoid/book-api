plugins {
    java
    id("org.springframework.boot") version "3.3.5"
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
    implementation(libs.org.springframework.boot.spring.boot.starter.data.jpa)
    implementation(libs.org.springframework.boot.spring.boot.starter.validation)
    implementation(libs.org.springframework.boot.spring.boot.starter.web)
    compileOnly(libs.org.projectlombok.lombok)
    implementation(libs.org.mapstruct.mapstruct)
    implementation(libs.org.projectlombok.lombok.mapstruct.binding)
    implementation(libs.org.flywaydb.flyway.core)
    implementation(libs.org.springdoc.springdoc.openapi.starter.webmvc.ui)
    runtimeOnly(libs.org.springframework.boot.spring.boot.devtools)
    runtimeOnly(libs.org.springframework.boot.spring.boot.docker.compose)
    runtimeOnly(libs.org.postgresql.postgresql)
    runtimeOnly(libs.org.flywaydb.flyway.postgresql)
    annotationProcessor(libs.org.projectlombok.lombok)
    annotationProcessor(libs.org.mapstruct.mapstruct.processor)
    testImplementation(libs.org.springframework.boot.spring.boot.starter.test)
    testImplementation(libs.org.springframework.boot.spring.boot.testcontainers)
    testImplementation(libs.org.testcontainers.junit.jupiter)
    testImplementation(libs.org.testcontainers.postgresql)
}

tasks.withType<Test> {
    useJUnitPlatform()
}