package com.bindglam.guider.manager;

import com.bindglam.guider.node.Edge;
import com.bindglam.guider.node.Vertex;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public interface VertexManager extends IManager {
    @NotNull Vertex findNearestVertex(@NotNull Location location);

    @Nullable Vertex getVertex(int index);

    @Nullable Vertex getVertex(String id);

    int getVertexCount();

    @Unmodifiable List<Edge> getEdges(int index);

    @Unmodifiable List<Edge> getEdges(String id);
}
