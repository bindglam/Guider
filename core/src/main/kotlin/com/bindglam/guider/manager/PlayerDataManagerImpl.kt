package com.bindglam.guider.manager

import com.bindglam.guider.util.DatabaseUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.concurrent.TimeUnit

class PlayerDataManagerImpl(private val plugin: Plugin, private val vertexManager: VertexManager, private val navigationManager: NavigationManager) : PlayerDataManager {
    override fun init() {
        DatabaseUtils.connection.use { connection ->
            connection.createStatement().apply {
                execute("CREATE TABLE IF NOT EXISTS guider_playerdata(uuid VARCHAR(36) PRIMARY KEY, destination TINYTEXT)")
                close()
            }
        }
    }

    override fun load(player: Player) {
        Bukkit.getAsyncScheduler().runDelayed(plugin, { task ->
            DatabaseUtils.connection.use { connection ->
                val statement = connection.prepareStatement("SELECT * FROM guider_playerdata WHERE uuid = ?").apply {
                    setString(1, player.uniqueId.toString())
                }
                val rs = statement.executeQuery()

                if(rs.next()) {
                    val destination = vertexManager.getVertex(rs.getString("destination"))

                    if(destination != null)
                        navigationManager.createNavigation(player, destination)
                }

                rs.close()
                statement.close()
            }
        }, 1L, TimeUnit.SECONDS)
    }

    override fun save(player: Player) {
        val navigation = navigationManager.getNavigation(player)
        val destination = navigation?.path?.lastOrNull()?.id

        navigation?.dispose(false, false)

        Bukkit.getAsyncScheduler().runNow(plugin) { task ->
            DatabaseUtils.connection.use { connection ->
                val statement1 = connection.prepareStatement("SELECT * FROM guider_playerdata WHERE uuid = ?").apply {
                    setString(1, player.uniqueId.toString())
                }
                val rs = statement1.executeQuery()

                if (destination == null && rs.next()) {
                    val statement2 = connection.prepareStatement("DELETE FROM guider_playerdata WHERE uuid = ?").apply {
                        setString(1, player.uniqueId.toString())
                    }
                    statement2.executeUpdate()
                    statement2.close()
                }

                if (destination != null) {
                    val statement2 = if(rs.next()) {
                        connection.prepareStatement("UPDATE guider_playerdata SET destination = ? WHERE uuid = ?").apply {
                            setString(1, destination)
                            setString(2, player.uniqueId.toString())
                        }
                    } else {
                        connection.prepareStatement("INSERT INTO guider_playerdata(uuid, destination) VALUES (?, ?)").apply {
                            setString(1, player.uniqueId.toString())
                            setString(2, destination)
                        }
                    }

                    statement2.executeUpdate()
                    statement2.close()
                }

                rs.close()
                statement1.close()
            }
        }
    }
}