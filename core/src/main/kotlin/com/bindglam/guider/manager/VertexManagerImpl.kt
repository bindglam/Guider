package com.bindglam.guider.manager

import com.bindglam.guider.node.Edge
import com.bindglam.guider.node.Vertex
import com.bindglam.guider.util.ConfigUtils
import com.bindglam.guider.util.FileUtils
import com.bindglam.guider.util.MathUtils
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.jetbrains.annotations.Unmodifiable
import java.io.File
import java.util.logging.Logger
import kotlin.math.max

class VertexManagerImpl(private val logger: Logger) : VertexManager {
    companion object {
        private val VERTEXES_FOLDER = File("plugins/Guider/vertexes/")
    }

    private val vertexes = ArrayList<Vertex>()
    private val vertexesById = HashMap<String, Vertex>()

    override fun init() {
        vertexesById.clear()
        vertexes.clear()

        if(!VERTEXES_FOLDER.exists())
            VERTEXES_FOLDER.mkdirs()

        loadVertexes()

        updateEdgesDistance()

        logger.info("Loaded $vertexCount vertexes")
    }

    private fun loadVertexes() {
        var loadedVertexes = 0

        FileUtils.getAllFiles(VERTEXES_FOLDER).forEach { configFile ->
            val config = YamlConfiguration.loadConfiguration(configFile)

            config.getKeys(false).forEach { id ->
                if(config.get("$id.location") == null) return@forEach

                val type = if(config.getString("$id.type") != null)
                    Vertex.Type.valueOf(config.getString("$id.type")!!)
                else
                    Vertex.Type.DEFAULT

                val vertex = Vertex(loadedVertexes, id, ConfigUtils.parseLocation(config.getConfigurationSection("$id.location")!!), type).apply {
                    config.getStringList("$id.to").forEach { to ->
                        addEdge(Edge(id, to))
                    }

                    if(type == Vertex.Type.PORTAL) {
                        config.getStringList("$id.teleport").forEach { to ->
                            addEdge(Edge(id, to).apply { distance = 1 })
                        }
                    }
                }

                vertexes.add(vertex)
                vertexesById[id] = vertex

                loadedVertexes++
            }
        }
    }

    private fun updateEdgesDistance() {
        vertexes.forEach { vertex ->
            vertex.edges.forEach { edge ->
                if(edge.distance == 0) {
                    val toLoc = getVertex(edge.to)!!.location

                    // 다익스트라 알고리즘은 간선의 가중치가 양수여야함
                    edge.distance = max(MathUtils.distanceSquared(vertex.location, toLoc).toInt(), 1)
                }
            }
        }
    }

    override fun findNearestVertex(location: Location): Vertex {
        var nearestVertex = vertexes[0]
        var nearestVertexDistance = Double.MAX_VALUE

        vertexes.forEach { vertex ->
            if(vertex.location.world != location.world) return@forEach
            val distance = MathUtils.distanceSquared(vertex.location, location)

            if(distance < nearestVertexDistance) {
                nearestVertex = vertex
                nearestVertexDistance = distance
            }
        }

        return nearestVertex // nearestVertexDistance의 초기 값이 double의 최대 값이기 때문에 null이 발생할 수 없음
    }

    override fun getVertex(index: Int): Vertex? {
        return vertexes[index]
    }

    override fun getVertex(id: String): Vertex? {
        return vertexesById[id]
    }

    override fun getVertexCount(): Int {
        return vertexesById.size
    }

    override fun getEdges(index: Int): @Unmodifiable List<Edge> {
        return getVertex(index)?.edges ?: listOf()
    }

    override fun getEdges(id: String): @Unmodifiable List<Edge> {
        return getVertex(id)?.edges ?: listOf()
    }
}