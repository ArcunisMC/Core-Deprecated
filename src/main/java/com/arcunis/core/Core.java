package com.arcunis.core;

import com.arcunis.core.database.Column;
import com.arcunis.core.database.DataType;
import com.arcunis.core.database.Database;
import com.arcunis.core.reload.ReloadCommand;
import com.arcunis.core.reload.ReloadEvent;
import com.arcunis.core.reload.ReloadTabcompleter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

    public static Database Database;
    public static Column Column;
    public static DataType DataType;
    public Database getDatabase(JavaPlugin plugin) {
        String databaseName = plugin.getName();
        try {
            if (getConfig().getBoolean("use_mysql")) {
                Connection connection = DriverManager.getConnection("jdbc:mysql://" + getConfig().getString("mysql_url") + ":" + getConfig().getString("mysql_port") + "/" + databaseName + "?useSSL=false", getConfig().getString("mysql_username"), getConfig().getString("mysql_password"));
                return new Database(this, connection, databaseName);
            } else {
                Class.forName("org.sqlite.JDBC");
                Connection connection = DriverManager.getConnection("jdbc:sqlite:plugins/" + this.getName() + "/" + databaseName + ".db");
                return new Database(this, connection, databaseName);
            }
        } catch (ClassNotFoundException | SQLException error) {
            error.printStackTrace();
            return null;
        }
    }

}
