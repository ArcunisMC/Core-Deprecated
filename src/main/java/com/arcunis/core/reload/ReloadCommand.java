package com.arcunis.core.reload;

import com.arcunis.core.absracts.Command;
import com.arcunis.core.Core;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends Command {
    public ReloadCommand(JavaPlugin plugin) {
        super(plugin, "reload", false);
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        Core core = (Core) plugin;
        if (args.length < 1) {
            sender.sendMessage(Component.text("Reloading all plugins"));
            Bukkit.getLogger().info("Reloading all plugins");
            for (String plugin : core.getPlugins().keySet()) {
                sender.sendMessage(Component.text("Reloading " + plugin));
                Bukkit.getLogger().info("Reloading " + plugin);
                new ReloadEvent(plugin).callEvent();
            }
        } else {
            for (String plugin : args) {
                sender.sendMessage(Component.text("Reloading " + plugin));
                Bukkit.getLogger().info("Reloading " + plugin);
                new ReloadEvent(plugin).callEvent();
            }
        }
    }
}