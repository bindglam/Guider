package com.bindglam.guider.listeners

import com.bindglam.guider.Guider
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerListener : Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player

        Guider.getInstance().playerDataManager.load(player)
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player

        Guider.getInstance().playerDataManager.save(player)
    }
}