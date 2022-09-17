import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springBootVersion = "2.7.3"
val springSecurityVersion = "5.7.3"

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20-RC")
    }
}

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.7.20-RC"
    id("org.springframework.boot") version "2.6.1"
    kotlin("kapt") version "1.6.10"
}

group = "eu.thelastdodo.tarock20"
version = "1.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_18

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-websocket:$springBootVersion")
    implementation("org.springframework.security:spring-security-core:$springSecurityVersion")
    implementation("org.springframework.security:spring-security-config:$springSecurityVersion")
    implementation("org.springframework.security:spring-security-web:$springSecurityVersion")
    testImplementation(kotlin("test"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "18"
    }
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "18"
}