plugins {
    kotlin("jvm") version "2.3.0"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.ahmed"
version = "1.0-SNAPSHOT"

repositories {
    maven(url = "https://central.sonatype.com/repository/maven-snapshots/") {
        content { // This filtering is optional, but recommended
            includeModule("net.minestom", "minestom")
            includeModule("net.minestom", "testing")
        }
    }
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom:2026.01.08-1.21.11")
    testImplementation("net.minestom:testing:2026.01.01-1.21.11")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

kotlin {
    jvmToolchain(25)
}

application {
    mainClass.set("MainKt")
}

// Make the fat jar the main artifact
tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    archiveClassifier.set("") // output: build/libs/MinestomStuff-1.0-SNAPSHOT.jar
    manifest {
        attributes["Main-Class"] = "MainKt" // OR "me.ahmed.MainKt"
    }
}

tasks.build {
    dependsOn(tasks.named("shadowJar"))
}


