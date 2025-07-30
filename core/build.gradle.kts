import xyz.jpenilla.resourcefactory.paper.PaperPluginYaml

plugins {
    id("paper-conventions")
    id("xyz.jpenilla.resource-factory-paper-convention") version "1.3.0"
}

dependencies {
    implementation(project(":api"))
    implementation("dev.jorel:commandapi-bukkit-shade-mojang-mapped:10.1.2")
    implementation("com.github.Team-Arushia:Faker:v1.0.6") {
        exclude("com.github.retrooper", "packetevents-spigot")
    }
    compileOnly("com.nexomc:nexo:1.9.0")
    compileOnly("io.github.toxicity188:BetterModel:1.10.1")
    compileOnly("me.clip:placeholderapi:2.11.6")
    implementation("com.zaxxer:HikariCP:4.0.3")
}

paperPluginYaml {
    name = rootProject.name
    main = "$group.GuiderPluginImpl"
    version = project.version.toString()
    authors.add("Bindglam")
    apiVersion = "1.21"
    dependencies {
        // packetevents
        server("ProtocolLib", PaperPluginYaml.Load.BEFORE, false, true)
        server("ProtocolSupport", PaperPluginYaml.Load.BEFORE, false, true)
        server("ViaVersion", PaperPluginYaml.Load.BEFORE, false, true)
        server("ViaBackwards", PaperPluginYaml.Load.BEFORE, false, true)
        server("ViaRewind", PaperPluginYaml.Load.BEFORE, false, true)
        server("Geyser-Spigot", PaperPluginYaml.Load.BEFORE, false, true)

        server("Nexo", PaperPluginYaml.Load.BEFORE, false, true)
        server("BetterModel", PaperPluginYaml.Load.BEFORE, false, true)
        server("PlaceholderAPI", PaperPluginYaml.Load.BEFORE, false, true)
    }
}
