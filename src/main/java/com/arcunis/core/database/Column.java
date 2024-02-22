package com.arcunis.core.database;

public class Column {

    public boolean primary;
    public String name;
    public DataType type;
    public Boolean notNull;
    public Object defaultValue;

    public Column(String name, DataType type, Boolean notNull, Object defaultValue) {

    }

    public String getString() {
        String str = "";
        str += name;
        str += " " + type.toString();
        if (primary) str += " PRIMARY KEY";
        if (notNull) str += " NOT NULL";
        if (defaultValue != null) {
            if (type == DataType.text) {
                str += " DEFAULT \"" + defaultValue.toString() + "\"";
            } else {
                str += " DEFAULT " + defaultValue.toString();
            }
        }
        return str;
    }

}