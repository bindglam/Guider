package com.bindglam.guider;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public final class Guider {
    private static GuiderPlugin instance;

    private Guider() {
    }

    public static @NotNull GuiderPlugin getInstance() {
        return instance;
    }

    @ApiStatus.Internal
    static void setInstance(@NotNull GuiderPlugin instance) {
        Guider.instance = instance;
    }
}
