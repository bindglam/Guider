package com.bindglam.guider.navigation;

import com.bindglam.guider.node.Vertex;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface NavigationFactory {
    Navigation create(@NotNull Plugin plugin, Player player, List<Vertex> path, @NotNull ConfigurationSection config);
}
