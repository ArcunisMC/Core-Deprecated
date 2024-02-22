package com.arcunis.core.reload;

import net.kyori.adventure.text.Component;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ReloadEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final Component plugin;

    public ReloadEvent(String plugin) {
        this.plugin = Component.text(plugin);
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }


    public Component getPlugin() {
        return this.plugin;
    }


}
