package com.arcunis.core.absracts;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class Registry {

    protected JavaPlugin plugin;

    public Registry(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void registerCommands();
    public abstract void registerTabcompleters();
    public abstract void registerEvents();

}
