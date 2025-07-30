plugins {
    java
    kotlin("jvm")
}

group = "com.bindglam.guider"
version = property("plugin_version").toString()

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://jitpack.io")
    maven("https://repo.nexomc.com/releases")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

kotlin {
    jvmToolchain(21)
}