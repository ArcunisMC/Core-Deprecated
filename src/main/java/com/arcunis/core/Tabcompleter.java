package com.arcunis.core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Tabcompleter implements TabCompleter {

    protected JavaPlugin plugin;

    public Tabcompleter(JavaPlugin plugin, String command) {
        this.plugin = plugin;
        plugin.getCommand(command).setTabCompleter(this);

    }

    public abstract ArrayList<String> execute(CommandSender sender, String label, String[] args);

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return execute(commandSender, s, strings);
    }
}
