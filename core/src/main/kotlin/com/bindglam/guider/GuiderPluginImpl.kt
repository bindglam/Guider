package com.bindglam.guider

import com.bindglam.faker.Faker
import com.bindglam.guider.listeners.PlayerListener
import com.bindglam.guider.manager.*
import com.bindglam.guider.navigation.Pathfinder
import com.bindglam.guider.navigation.PathfinderImpl
import com.bindglam.guider.util.DatabaseUtils
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.arguments.TextArgument
import dev.jorel.commandapi.executors.CommandExecutor
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class GuiderPluginImpl : JavaPlugin(), GuiderPlugin {
    companion object {
        private val CONFIG_FILE = File("plugins/Guider/config.yml")
    }

    private lateinit var vertexManager: VertexManager
    private lateinit var pathfinder: Pathfinder
    private lateinit var navigationManager: NavigationManager
    private lateinit var playerDataManager: PlayerDataManager

    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(this))
        Faker.load(this)

        CommandAPICommand("guider")
            .withPermission(CommandPermission.OP)
            .withSubcommands(
                CommandAPICommand("reload")
                    .executes(CommandExecutor { sender, args ->
                        sender.sendMessage(Component.text("리로드중..."))

                        config.load(CONFIG_FILE)

                        vertexManager.init()
                        navigationManager.init()

                        sender.sendMessage(Component.text("리로드 완료!"))
                    }),
                CommandAPICommand("navigation")
                    .withAliases("navi")
                    .withSubcommands(
                        CommandAPICommand("add")
                            .withArguments(PlayerArgument("player"), TextArgument("destination"))
                            .executes(CommandExecutor { sender, args ->
                                val player = args["player"] as Player
                                val destinationId = args["destination"] as String

                                val destination = vertexManager.getVertex(destinationId)
                                if(destination == null) {
                                    player.sendMessage(Component.text("알 수 없는 목적지입니다.").color(NamedTextColor.RED))
                                    return@CommandExecutor
                                }

                                navigationManager.createNavigation(player, destination)
                            }),
                        CommandAPICommand("remove")
                            .withArguments(PlayerArgument("player"))
                            .executes(CommandExecutor { sender, args ->
                                val player = args["player"] as Player

                                navigationManager.getNavigation(player)?.dispose(false, false)
                            })
                    )
            )
            .register()
    }

    override fun onEnable() {
        CommandAPI.onEnable()
        Faker.enable()

        saveDefaultConfig()

        Guider.setInstance(this)

        server.pluginManager.registerEvents(PlayerListener(), this)

        vertexManager = VertexManagerImpl(logger)
        pathfinder = PathfinderImpl(vertexManager)
        navigationManager = NavigationManagerImpl(logger, this)
        playerDataManager = PlayerDataManagerImpl(this)

        DatabaseUtils.init(this)

        vertexManager.init()
        navigationManager.init()
        playerDataManager.init()
    }

    override fun onDisable() {
        CommandAPI.onDisable()
        Faker.disable()

        DatabaseUtils.close()
    }

    override fun getVertexManager(): VertexManager {
        return vertexManager
    }

    override fun getPathfinder(): Pathfinder {
        return pathfinder
    }

    override fun getNavigationManager(): NavigationManager {
        return navigationManager
    }

    override fun getPlayerDataManager(): PlayerDataManager {
        return playerDataManager
    }
}