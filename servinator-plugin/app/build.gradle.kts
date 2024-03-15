plugins {
    application
    id("xyz.jpenilla.run-paper") version "2.2.2"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}


repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(libs.guava)
    implementation(platform("software.amazon.awssdk:bom:2.25.10"))
    implementation("software.amazon.awssdk:ecs")

    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    runServer {
        minecraftVersion("1.20.4")
    }
}

application {
    mainClass.set("com.github.sharp378.paper.servinator.ServinatorPlugin")
}
