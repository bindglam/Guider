package com.bindglam.guider.navigation

import com.bindglam.faker.entity.display.FakeTextDisplayEntity
import com.bindglam.guider.node.Vertex
import com.bindglam.guider.util.MathUtils
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.entity.Display
import org.bukkit.entity.Player
import org.bukkit.entity.TextDisplay
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.plugin.Plugin
import org.bukkit.util.Transformation
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.concurrent.TimeUnit


open class DefaultNavigation(plugin: Plugin, player: Player, path: List<Vertex>,
                             private val markIcon: Component, private val markScale: Float) : NavigationBase(plugin, player, path) {
    companion object {
        private const val MARK_REFERENCE_DISTANCE = 5f // 기준 거리 (이 거리에서 스케일이 1.0이 됨)
    }

    private var mark = createMark()

    private var markTransformation = Transformation(Vector3f(), Quaternionf(), Vector3f(markScale), Quaternionf())

    init {
        mark.spawn(player)
    }

    override fun onUpdate() {
        calculateMarkTransformation()

        mark.location = MathUtils.lerp(mark.location, destination!!.location, 0.1f)
        mark.setTransformation(markTransformation)

        mark.update(player)
    }

    override fun onDispose() {
        mark.remove(player)
    }

    @EventHandler
    open fun onDimensionChanged(event: PlayerTeleportEvent) {
        if(event.player.uniqueId != player.uniqueId) return

        if(event.from.world != event.to.world && event.to.world == mark.location.world) {
            mark.remove(player)

            Bukkit.getAsyncScheduler().runDelayed(plugin, { task ->
                mark = createMark()
                mark.spawn(player)
            }, 40*50L, TimeUnit.MILLISECONDS)
        }
    }

    private fun createMark(): FakeTextDisplayEntity {
        return FakeTextDisplayEntity(player.location).apply {
            setInterpolationDelay(0)
            setInterpolationDuration(1)
            setMoveDuration(1)
            setBillboard(Display.Billboard.CENTER)
            setBrightness(15)
            setViewRange(Float.MAX_VALUE)
            setBackgroundColor(Color.fromARGB(0, 0, 0, 0))
            setProperties(false, true, false, TextDisplay.TextAlignment.CENTER)
            setText(markIcon)
        }
    }

    private fun calculateMarkTransformation() {
        val from = player.location
        val to = mark.location

        val distance = MathUtils.distance(from, to).toFloat()

        // 거리에 비례하는 스케일 팩터 계산
        // 물체의 크기는 물체의 위치와 카메라 위치 사이의 거리에 반비례함
        val scaleFactor = distance / MARK_REFERENCE_DISTANCE

        markTransformation = Transformation(Vector3f(), Quaternionf(), Vector3f(scaleFactor * markScale), Quaternionf())
    }
}