package com.bindglam.guider.navigation

import com.bindglam.guider.node.Vertex
import kr.toxicity.model.api.BetterModel
import kr.toxicity.model.api.animation.AnimationModifier
import kr.toxicity.model.api.data.renderer.ModelRenderer
import kr.toxicity.model.api.tracker.EntityHideOption
import kr.toxicity.model.api.tracker.EntityTracker
import kr.toxicity.model.api.tracker.ModelRotator
import kr.toxicity.model.api.tracker.TrackerModifier
import kr.toxicity.model.api.tracker.TrackerUpdateAction
import kr.toxicity.model.api.util.function.BonePredicate
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Display
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.plugin.Plugin
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.concurrent.TimeUnit
import java.util.function.Function
import kotlin.math.atan2
import kotlin.math.sqrt


class ModelNavigation(plugin: Plugin, player: Player, path: List<Vertex>,
                      markIcon: Component, markScale: Float,
                      private val compassModelId: String, private val compassOffset: Vector3f, private val compassScale: Float) : DefaultNavigation(plugin, player, path, markIcon, markScale) {
    companion object {
        private val DEFAULT_COMPASS_OFFSET = Vector3f(0f, 1.15f, -1.15f)
    }

    private var compass = createCompass()

    init {
    }

    override fun onUpdate() {
        super.onUpdate()
        if(!compass.isClosed) {
            if(player.world != destination?.location?.world) {
                compass.close()
                return
            }

            if (compass.pipeline.runningAnimation() == null)
                compass.animate("idle")

            compass.getPipeline().addRotationModifier(BonePredicate.TRUE) { rot -> calculateRotation() }
        }
    }

    override fun onDispose() {
        super.onDispose()

        compass.stopAnimation("idle")
        compass.animate("end", AnimationModifier.DEFAULT_WITH_PLAY_ONCE) {
            compass.close()
        }
    }

    @EventHandler
    override fun onDimensionChanged(event: PlayerTeleportEvent) {
        super.onDimensionChanged(event)

        if(event.player.uniqueId != player.uniqueId) return

        if(event.from.world != event.to.world)
            compass.close()

        if(event.to.world == destination?.location?.world) {
            Bukkit.getAsyncScheduler().runDelayed(plugin, { task ->
                compass = createCompass()
            }, 40*50L, TimeUnit.MILLISECONDS)
        }
    }

    private fun createCompass(): EntityTracker {
        return BetterModel.model(compassModelId)
            .map(Function { r: ModelRenderer? ->
                r!!.create(player, TrackerModifier.DEFAULT) { tracker ->
                    tracker.animate("start", AnimationModifier.DEFAULT_WITH_PLAY_ONCE)
                    tracker.update(TrackerUpdateAction.brightness(15, 15))
                    tracker.update(TrackerUpdateAction.billboard(Display.Billboard.CENTER))
                    tracker.getPipeline().addRotationModifier(BonePredicate.TRUE) { rot -> calculateRotation() }
                    tracker.getPipeline().addPositionModifier(BonePredicate.TRUE) { pos -> pos.add(DEFAULT_COMPASS_OFFSET).add(compassOffset) }
                    tracker.getPipeline().scale { compassScale }
                    tracker.rotator(ModelRotator.EMPTY)
                    tracker.markPlayerForSpawn(player)
                    tracker.hideOption(EntityHideOption.FALSE)
                }
            })
            .get()
    }

    // By toxicity
    private fun calculateRotation(): Quaternionf {
        val from = player.location
        val to = destination?.location ?: player.location

        if(from.world != to.world)
            return Quaternionf()

        val yaw = Math.toRadians(from.yaw.toDouble()).toFloat()
        val pitch = Math.toRadians(from.pitch.toDouble()).toFloat()
        val forward = Vector3f(DEFAULT_COMPASS_OFFSET).rotate(Quaternionf().rotateZYX(0f, -yaw, pitch))
        val dy = to.y() - from.y() - forward.y
        val dz = to.z() - from.z() - forward.z
        val dx = to.x() - from.x() - forward.x

        return Quaternionf().rotateLocalY(yaw)
            .rotateLocalX(pitch)
            .rotateZYX(0f, atan2(dx, dz).toFloat(), atan2(dy, sqrt(dz * dz + dx * dx)).toFloat())
    }
}