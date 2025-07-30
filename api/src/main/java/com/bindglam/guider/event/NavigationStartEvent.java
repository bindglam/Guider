package com.bindglam.guider.event;

import com.bindglam.guider.navigation.Navigation;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class NavigationStartEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Navigation navigation;

    public NavigationStartEvent(@NotNull Player who, Navigation navigation) {
        super(who);
        this.navigation = navigation;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
