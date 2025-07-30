package com.bindglam.guider;

import com.bindglam.guider.manager.NavigationManager;
import com.bindglam.guider.manager.PlayerDataManager;
import com.bindglam.guider.manager.VertexManager;
import com.bindglam.guider.navigation.Pathfinder;
import org.jetbrains.annotations.NotNull;

public interface GuiderPlugin {
    @NotNull VertexManager getVertexManager();

    @NotNull Pathfinder getPathfinder();

    @NotNull NavigationManager getNavigationManager();

    @NotNull PlayerDataManager getPlayerDataManager();
}
