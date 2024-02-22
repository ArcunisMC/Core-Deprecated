package com.arcunis.core.reload;

import com.arcunis.core.Core;
import com.arcunis.core.Tabcompleter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Map;

public class ReloadTabcompleter extends Tabcompleter {

    public ReloadTabcompleter(JavaPlugin plugin) {
        super(plugin, "reload");
    }

    @Override
    public ArrayList<String> execute(CommandSender sender, String label, String[] args) {
        Core core = (Core) plugin;
        ArrayList<String> pluginNames = new ArrayList<>();
        for (Map.Entry<String, Plugin> plugin : core.getPlugins().entrySet()) {
            if (plugin.getValue().getPluginMeta().getPluginDependencies().contains(core.getName())) {
                pluginNames.add(plugin.getKey());
            }
        }
        return pluginNames;
    }

}
