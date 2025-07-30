plugins {
    id("standard-conventions")
    id("com.gradleup.shadow") version "9.0.0-rc2"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

dependencies {
    implementation(project(":core"))
}

val groupString = group.toString()

tasks {
    runServer {
        version("1.21.4")

        downloadPlugins {
            modrinth("bettermodel", "1.10.1")
        }
    }

    jar {
        finalizedBy(shadowJar)
    }

    shadowJar {
        archiveClassifier = ""
        dependencies {
            exclude(dependency("org.jetbrains:annotations:26.0.2"))
        }
        fun prefix(pattern: String) {
            relocate(pattern, "$groupString.shaded.$pattern")
        }
        prefix("kotlin")
        prefix("dev.jorel.commandapi")
        prefix("com.github.retrooper.packetevents")
        prefix("io.github.retrooper.packetevents")
        prefix("com.zaxxer.hikari")
    }
}