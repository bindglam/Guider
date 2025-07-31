package com.bindglam.guider

import com.bindglam.faker.Faker
import com.bindglam.guider.listeners.PlayerListener
import com.bindglam.guider.manager.*
import com.bindglam.guider.navigation.Pathfinder
import com.bindglam.guider.navigation.PathfinderImpl
import com.bindglam.guider.util.DatabaseUtils
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class GuiderPluginImpl : JavaPlugin(), GuiderPlugin {
    companion object {
        val CONFIG_FILE = File("plugins/Guider/config.yml")
    }

    private val vertexManager by lazy { VertexManagerImpl(this) }
    private val pathfinder by lazy { PathfinderImpl(vertexManager) }
    private val navigationManager by lazy { NavigationManagerImpl(this, vertexManager, pathfinder) }
    private val playerDataManager by lazy { PlayerDataManagerImpl(this, vertexManager, navigationManager) }
    private val commandManager by lazy { CommandManagerImpl(this, vertexManager, navigationManager) }

    override fun onLoad() {
        Faker.load(this)
    }

    override fun onEnable() {
        saveDefaultConfig()

        Faker.enable()

        Guider.setInstance(this)

        server.pluginManager.registerEvents(PlayerListener(), this)

        DatabaseUtils.init(this)

        vertexManager.init()
        navigationManager.init()
        playerDataManager.init()
        commandManager.init()
    }

    override fun onDisable() {
        Faker.disable()

        commandManager.dispose()

        DatabaseUtils.close()
    }

    override fun getVertexManager(): VertexManager = vertexManager
    override fun getPathfinder(): Pathfinder = pathfinder
    override fun getNavigationManager(): NavigationManager = navigationManager
    override fun getPlayerDataManager(): PlayerDataManager = playerDataManager
}