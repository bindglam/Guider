package com.bindglam.guider.manager

import com.bindglam.guider.event.NavigationStartEvent
import com.bindglam.guider.navigation.DefaultNavigationFactory
import com.bindglam.guider.navigation.ModelNavigationFactory
import com.bindglam.guider.navigation.Navigation
import com.bindglam.guider.navigation.NavigationFactory
import com.bindglam.guider.navigation.Pathfinder
import com.bindglam.guider.node.Vertex
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.*
import java.util.concurrent.TimeUnit

class NavigationManagerImpl(private val plugin: Plugin, private val vertexManager: VertexManager, private val pathfinder: Pathfinder) : NavigationManager {
    private val factories = HashMap<String, NavigationFactory>()

    private val navigationMap = HashMap<UUID, Navigation>()

    override fun init() {
        factories.clear()
        navigationMap.clear()

        registerFactory("default", DefaultNavigationFactory())
        registerFactory("model", ModelNavigationFactory())

        Bukkit.getAsyncScheduler().runAtFixedRate(plugin, { task ->
            navigationMap.values.forEach { navigation ->
                navigation.update()
            }
        }, 0L, 1*50L, TimeUnit.MILLISECONDS)
    }

    override fun registerFactory(id: String, factory: NavigationFactory) {
        factories[id] = factory
    }

    override fun getNavigation(player: Player): Navigation? {
        return navigationMap[player.uniqueId]
    }

    override fun createNavigation(player: Player, destination: Vertex) {
        if(navigationMap.containsKey(player.uniqueId))
            navigationMap[player.uniqueId]!!.dispose(false, false)

        val factory = factories[plugin.config.getString("navigation.type")] ?: throw RuntimeException("Failed to create navigation")

        pathfinder.find(vertexManager.findNearestVertex(player.location), destination).thenAccept { path ->
            val navigation = factory.create(plugin, player, path, plugin.config.getConfigurationSection("navigation.${plugin.config.getString("navigation.type")}")!!)

            navigationMap[player.uniqueId] = navigation

            NavigationStartEvent(player, navigation).callEvent()
        }
    }

    override fun removeNavigation(player: Player) {
        navigationMap.remove(player.uniqueId)
    }
}