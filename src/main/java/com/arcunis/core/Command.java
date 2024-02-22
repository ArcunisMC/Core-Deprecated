package com.arcunis.core;

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

public abstract class Command extends BukkitCommand implements CommandExecutor {

    protected JavaPlugin plugin;
    private final Boolean playerOnly;

    public Command(JavaPlugin plugin, @NotNull String name, @Nullable String description, @Nullable String permission, @Nullable List<String> aliases, @Nullable Boolean playerOnly) {
        super(name);
        if (description != null) this.setDescription(description);
        if (permission != null) this.setPermission(permission);
        if (aliases != null) this.setAliases(aliases);

        this.plugin = plugin;
        this.playerOnly = playerOnly;

        try {
            Field field = plugin.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(plugin.getServer());
            commandMap.register(name, this);
        } catch (NoSuchFieldException | IllegalAccessException error) {
            error.printStackTrace();
        }

    }

    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        if (playerOnly && !(sender instanceof Player)) return true;
        return execute(sender, label, args);
    }
}