package com.bindglam.guider.util

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.joml.Vector3f

object ConfigUtils {
    fun parseVector3f(config: ConfigurationSection): Vector3f {
        return Vector3f(config.getDouble("x").toFloat(), config.getDouble("y").toFloat(), config.getDouble("z").toFloat())
    }

    fun parsePlaceholders(player: Player, content: String): String {
        return if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI"))
            PlaceholderAPI.setPlaceholders(player, content)
        else
            content
    }
}