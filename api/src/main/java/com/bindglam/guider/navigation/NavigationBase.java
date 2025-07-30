package com.bindglam.guider.navigation;

import com.bindglam.guider.Guider;
import com.bindglam.guider.event.NavigationEndEvent;
import com.bindglam.guider.node.Vertex;
import com.bindglam.guider.util.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class NavigationBase implements Navigation, Listener {
    private final Plugin plugin;
    private final Player player;

    private final List<Vertex> path;

    private int progress = 0;

    protected NavigationBase(Plugin plugin, Player player, List<Vertex> path) {
        this.player = player;
        this.plugin = plugin;
        this.path = path;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    protected abstract void onUpdate();
    protected abstract void onDispose();

    @Override
    public void update() {
        Vertex destination = getDestination();

        if(destination == null) {
            dispose(true);
            return;
        }

        onUpdate();

        if(player.getLocation().getWorld() == destination.getLocation().getWorld() && MathUtils.distanceSquared(player.getLocation(), destination.getLocation()) <= Math.pow(plugin.getConfig().getDouble("vertex.range"), 2.0)) {
            progress++;
        }
    }

    @Override
    public void dispose(boolean arrived, boolean async) {
        new NavigationEndEvent(player, this, arrived, async).callEvent();

        onDispose();

        Guider.getInstance().getNavigationManager().removeNavigation(player);

        HandlerList.unregisterAll(this);
    }

    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    @Override
    public @Nullable Vertex getDestination() {
        if(progress >= path.size())
            return null;
        return path.get(progress);
    }

    @Override
    public @NotNull List<Vertex> getPath() {
        return path;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public int getProgress() {
        return progress;
    }
}
