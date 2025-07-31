package com.bindglam.guider.util

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.joml.Vector3f

object ConfigUtils {
    fun parseLocation(config: ConfigurationSection): Location {
        return Location(Bukkit.getWorld(config.getString("world") ?: ""), config.getDouble("x"), config.getDouble("y"), config.getDouble("z"),
            config.getDouble("yaw").toFloat(), config.getDouble("pitch").toFloat())
    }

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