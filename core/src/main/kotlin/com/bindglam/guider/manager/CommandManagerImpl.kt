package com.bindglam.guider.manager

import com.bindglam.guider.GuiderPluginImpl.Companion.CONFIG_FILE
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

class CommandManagerImpl(
    private val plugin: JavaPlugin,
    private val vertexManager: VertexManager,
    private val navigationManager: NavigationManager
) : CommandManager {
    override fun init() {
        CommandAPI.onLoad(CommandAPIBukkitConfig(plugin))

        CommandAPICommand("guider")
            .withPermission(CommandPermission.OP)
            .withSubcommands(
                CommandAPICommand("reload")
                    .executes(CommandExecutor { sender, args ->
                        sender.sendMessage(Component.text("리로드중..."))

                        plugin.config.load(CONFIG_FILE)

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

        CommandAPI.onEnable()
    }

    override fun dispose() {
        CommandAPI.onDisable()
    }
}