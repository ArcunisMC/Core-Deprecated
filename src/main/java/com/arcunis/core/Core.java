package com.arcunis.core;

import com.arcunis.core.reload.ReloadCommand;
import com.arcunis.core.reload.ReloadEvent;
import com.arcunis.core.reload.ReloadTabcompleter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class Core extends JavaPlugin {

    public Logger logger = getLogger();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new ReloadCommand(this);
        new ReloadTabcompleter(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Command Command;
    public static Tabcompleter Tabcompleter;
    public static ReloadEvent ReloadEvent;

    public Map<String, Plugin> getPlugins() {
        Map<String, Plugin> plugins = new HashMap<>();
        Plugin[] bukkitPlugins = Bukkit.getPluginManager().getPlugins();
        for (Plugin plugin : bukkitPlugins) {
            if (plugin.getPluginMeta().getPluginDependencies().contains(getName())) {
                plugins.put(plugin.getName(), plugin);
            }
        }
        return plugins;
    }

}
