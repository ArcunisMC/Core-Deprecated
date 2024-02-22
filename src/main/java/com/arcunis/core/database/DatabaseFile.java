package com.arcunis.core.database;

import java.util.Map;

public class DatabaseFile {

    public Map<String, Map<String, Map<String, Column>>> databases;

    public DatabaseFile(Map<String, Map<String, Map<String, Column>>> databases) {
        this.databases = databases;
    }

}
