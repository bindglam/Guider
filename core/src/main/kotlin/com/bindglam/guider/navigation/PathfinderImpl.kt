package com.bindglam.guider.navigation

import com.bindglam.guider.Guider
import com.bindglam.guider.node.Vertex
import java.util.*
import java.util.concurrent.CompletableFuture


class PathfinderImpl : Pathfinder {
    override fun find(start: Vertex, end: Vertex): CompletableFuture<List<Vertex>> {
        return CompletableFuture.supplyAsync {
            // 다익스트라 알고리즘 O(ElogV)
            val distances = Array(Guider.getInstance().vertexManager.vertexCount) { i -> Int.MAX_VALUE } // 거리 배열 D
            val parents = Array(Guider.getInstance().vertexManager.vertexCount) { i -> i }

            distances[start.index] = 0

            val queue = PriorityQueue<Node> { o1, o2 -> o1.distance.compareTo(o2.distance) }
            queue.offer(Node(start.index, 0))

            while(queue.isNotEmpty()) {
                val node = queue.poll()!!
                val vertex = Guider.getInstance().vertexManager.getVertex(node.vertexIndex) ?: continue

                // 현재 누적된 가중치 값이 더 크다면, 더 이상의 탐색이 의미 없으니 건너뛰기
                if(distances[vertex.index] < node.distance)
                    continue

                // 해당 정점에서 연결된 간선 검색
                for(edge in Guider.getInstance().vertexManager.getEdges(node.vertexIndex)) {
                    val toVertex = Guider.getInstance().vertexManager.getVertex(edge.to) ?: continue

                    // 간선에 연결된 정점까지의 가중치가 현재 누적된 가중치 값보다 큰가?
                    if(distances[toVertex.index] > node.distance + edge.distance) {
                        distances[toVertex.index] = node.distance + edge.distance
                        parents[toVertex.index] = vertex.index

                        queue.offer(Node(toVertex.index, distances[toVertex.index]))
                    }
                }
            }

            // 백트레킹으로 경로 추출
            return@supplyAsync getPath(start, end, parents)
        }
    }

    private fun getPath(start: Vertex, end: Vertex, parents: Array<Int>): List<Vertex> {
        val path = ArrayList<Vertex>()

        var currentNode = end.index

        // 시작 노드에 도달할 때까지 역순으로 추적
        while (currentNode != start.index) {
            // 경로가 없는 경우 (parent가 초기화된 값인 경우)
            if (parents[currentNode] == currentNode)
                return listOf()

            path.add(Guider.getInstance().vertexManager.getVertex(currentNode)!!)

            currentNode = parents[currentNode]
        }

        path.add(start)
        path.reverse() // 경로를 시작 -> 끝 순서로 뒤집기
        return path
    }
}