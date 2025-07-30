package com.bindglam.guider.navigation

import com.bindglam.guider.node.Vertex
import com.bindglam.guider.util.ConfigUtils
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class ModelNavigationFactory : NavigationFactory {
    override fun create(plugin: Plugin, player: Player, path: List<Vertex>, config: ConfigurationSection): Navigation {
        return ModelNavigation(plugin, player, path,
            MiniMessage.miniMessage().deserialize(ConfigUtils.parsePlaceholders(player, config.getString("mark.icon") ?: "Here!")), config.getDouble("mark.scale").toFloat(),

            config.getString("compass.model-id")!!,
            ConfigUtils.parseVector3f(config.getConfigurationSection("compass.offset")!!),
            config.getDouble("compass.scale").toFloat())
    }
}