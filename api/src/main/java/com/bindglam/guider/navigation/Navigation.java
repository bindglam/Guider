package com.bindglam.guider.navigation;

import com.bindglam.guider.node.Vertex;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface Navigation {
    void update();

    void dispose(boolean arrived, boolean async);

    default void dispose(boolean async) {
        dispose(true, async);
    }

    @NotNull Player getPlayer();

    @Nullable Vertex getDestination();

    @NotNull List<Vertex> getPath();
}
