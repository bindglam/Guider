package com.bindglam.guider.listeners

import com.bindglam.guider.Guider
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListener : Listener {
    @EventHandler
    fun PlayerJoinEvent.onJoin() {
        Guider.getInstance().playerDataManager.load(player)
    }

    @EventHandler
    fun PlayerQuitEvent.onQuit() {
        Guider.getInstance().playerDataManager.save(player)
    }
}