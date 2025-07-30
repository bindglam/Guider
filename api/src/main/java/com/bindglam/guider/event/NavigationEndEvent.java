package com.bindglam.guider.event;

import com.bindglam.guider.navigation.Navigation;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class NavigationEndEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Navigation navigation;
    private final boolean arrived;

    public NavigationEndEvent(@NotNull Player who, Navigation navigation, boolean arrived, boolean async) {
        super(who, async);
        this.navigation = navigation;
        this.arrived = arrived;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public boolean hasArrived() {
        return arrived;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
