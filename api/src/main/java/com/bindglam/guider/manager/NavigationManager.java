package com.bindglam.guider.manager;

import com.bindglam.guider.navigation.Navigation;
import com.bindglam.guider.navigation.NavigationFactory;
import com.bindglam.guider.node.Vertex;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface NavigationManager extends IManager {
    void registerFactory(@NotNull String id, @NotNull NavigationFactory factory);

    void createNavigation(Player entity, Vertex destination);

    void removeNavigation(Player entity);

    @Nullable Navigation getNavigation(Player entity);
}
