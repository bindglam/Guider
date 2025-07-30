package com.bindglam.guider.navigation

import com.bindglam.guider.node.Vertex
import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin


class DefaultNavigationFactory : NavigationFactory {
    override fun create(plugin: Plugin, player: Player, path: List<Vertex>, config: ConfigurationSection): Navigation {
        return DefaultNavigation(plugin, player, path,
            MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(player, config.getString("mark.icon") ?: "Here!")), config.getDouble("mark.scale").toFloat())
    }
}