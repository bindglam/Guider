package com.bindglam.guider.manager;

import org.bukkit.entity.Player;

public interface PlayerDataManager extends IManager {
    void load(Player player);

    void save(Player player);
}
