package com.arcunis.core.absracts;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public abstract class Command implements CommandExecutor {

    protected JavaPlugin plugin;
    private final Boolean playerOnly;

    public Command(JavaPlugin plugin, @NotNull String name, @Nullable Boolean playerOnly) {
        this.plugin = plugin;
        this.playerOnly = playerOnly;
        plugin.getCommand(name).setExecutor(this);
    }

    public abstract void execute(@NotNull CommandSender sender, @NotNull String label, String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (playerOnly && !(sender instanceof Player)) return true;
        execute(sender, label, args);
        return false;
    }
    
}