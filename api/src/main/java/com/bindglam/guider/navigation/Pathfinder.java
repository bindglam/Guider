package com.bindglam.guider.navigation;

import com.bindglam.guider.node.Vertex;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Pathfinder {
    @NotNull CompletableFuture<List<Vertex>> find(@NotNull Vertex start, @NotNull Vertex end);
}
