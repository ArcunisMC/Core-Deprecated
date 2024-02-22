package com.arcunis.core.database;

import com.arcunis.core.Core;
import com.google.gson.Gson;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private final Core core;
    private final Connection connection;
    private final String name;

    public Database(Core core, Connection connection, String name) {
        this.core = core;
        this.connection = connection;
        this.name = name;
        if (getDatabase() == null) saveDatabase();
    }

    public void newTable(String name, Column[] columns) {
        try {
            ArrayList<String> columStrings = new ArrayList<>();
            for (Column column : columns) columStrings.add(column.getString());
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + name + " (" + String.join(", ", columStrings) + ")");
            statement.execute();
            saveTable(name, columns);
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    public boolean insert(String table, Object[] values) {
        try {
            ArrayList<String> columStrings = new ArrayList<>();
            ArrayList<String> valueStrings = new ArrayList<>();
            Column[] columnValues = (Column[]) getTable(table).values().toArray();
            for (Object column : values) {
                int i = Arrays.asList(values).indexOf(column);
                if (columnValues[i].type == DataType.text && column instanceof String) {
                    valueStrings.add("\"" + column + "\"");
                    columStrings.add(columnValues[i].name);
                } else if (columnValues[i].type == DataType.integer && column instanceof Integer) {
                    valueStrings.add(column.toString());
                    columStrings.add(columnValues[i].name);
                } else if (columnValues[i].type == DataType.blob) {
                    valueStrings.add(column.toString());
                    columStrings.add(columnValues[i].name);
                } else {
                    System.out.println("Could not insert into '" + name + "/" + table + "' as values do not match types");
                    return true;
                }
            }
            PreparedStatement statement = connection.prepareStatement("INSERT INTO " + table + "( " + String.join(", ", columStrings) + " ) VALUES (" + String.join(", ", valueStrings) + ")");
            statement.execute();
        } catch (SQLException error) {
            error.printStackTrace();
        }
        return false;
    }

    public boolean update(String table, String where, String[] columns, Object[] values) {
        try {
            if (columns.length != values.length) {
                System.out.println("Could not update '" + table + "' as 'colums' and 'values' are not the same length");
                return true;
            }
            Map<String, Column> columnMap = getTable(table);
            ArrayList<String> strings = new ArrayList<>();
            for (Object value : values) {
                int i = Arrays.asList(values).indexOf(value);
                if (columnMap.get(table).type == DataType.text && value instanceof String) {
                    strings.add(columns[i] + " = \"" + value + "\"");
                } else if (columnMap.get(table).type == DataType.integer && value instanceof Integer) {
                    strings.add(columns[i] + " = " + value);
                } else if (columnMap.get(table).type == DataType.blob) {
                    strings.add(columns[i] + " = " + value);
                } else {
                    System.out.println("Could not update '" + name + "/" + table + "' as values do not match types");
                    return true;
                }
            }
            PreparedStatement statement = connection.prepareStatement("UPDATE " + table + " SET " + String.join(", ", strings) + " WHERE " + where);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void delete(String table, String where) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + table + " WHERE " + where);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet select(String table, String where) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + table + " WHERE " + where);
            return statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException error) {
            error.printStackTrace();
        }
    }

    private void saveTable(String name, Column[] columns) {
        try {
            File file = new File(core.getDataFolder(), "databases.json");
            Writer writer = new FileWriter(file, false);
            if (!file.exists()) {
                file.createNewFile();
                new Gson().toJson(new DatabaseFile(new HashMap<>()), writer);
            }
            Map<String, Map<String, Column>> tableMap = getDatabase();
            Map<String, Column> columnMap = new HashMap<>();
            for (Column column : columns) columnMap.put(column.name, column);
            tableMap.put(name, columnMap);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    private Map<String, Column> getTable(String name) {
        try {
            File file = new File(core.getDataFolder(), "databases.json");
            if (!file.exists()) {
                file.createNewFile();
                Writer writer = new FileWriter(file, false);
                new Gson().toJson(new DatabaseFile(new HashMap<>()), writer);
            }
            Reader reader = new FileReader(file);
            return new Gson().fromJson(reader, DatabaseFile.class).databases.get(this.name).get(name);
        } catch (IOException error) {
            error.printStackTrace();
        }
        return null;
    }

    private void saveDatabase() {
        try {
            File file = new File(core.getDataFolder(), "databases.json");
            Writer writer = new FileWriter(file, false);
            if (!file.exists()) {
                file.createNewFile();
                new Gson().toJson(new DatabaseFile(new HashMap<>()), writer);
            }
            Map<String, Map<String, Map<String, Column>>> dbMap = new HashMap<>();
            dbMap.put(name, new HashMap<>());
            new Gson().toJson(new DatabaseFile(dbMap), writer);
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    private Map<String, Map<String, Column>> getDatabase() {
        try {
            File file = new File(core.getDataFolder(), "databases.json");
            if (!file.exists()) {
                file.createNewFile();
                Writer writer = new FileWriter(file, false);
                new Gson().toJson(new DatabaseFile(new HashMap<>()), writer);
            }
            Reader reader = new FileReader(file);
            return new Gson().fromJson(reader, DatabaseFile.class).databases.get(this.name);
        } catch (IOException error) {
            error.printStackTrace();
        }
        return null;
    }

}

