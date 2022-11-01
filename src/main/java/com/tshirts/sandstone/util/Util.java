package com.tshirts.sandstone.util;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class Util {
    public static final DatabaseAccess DB = new DatabaseAccess();

    public static <T> boolean write(File path, Gson gson, T[] typeArray) {
        String json = gson.toJson(typeArray);
        // Write to file
        try (FileWriter file = new FileWriter(path)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static <T> String javaTypeToH2(T type) {
        if (type instanceof Class<?> s) {
            String className = s.getSimpleName();
            System.out.println(className);
            if (Util.isEnum(s)) {
                return "VARCHAR(255)";
            }
            return switch (className.toLowerCase()) {
                case "boolean" -> "BOOLEAN";
                case "byte" -> "TINYINT";
                case "short" -> "SMALLINT";
                case "integer", "int" -> "INT";
                case "long" -> "BIGINT";
                case "float" -> "REAL";
                case "double" -> "DOUBLE";
                case "string" -> "VARCHAR";
                case "character", "char" -> "CHAR";
                case "date" -> "DATE";
                case "time" -> "TIME";

                default -> null;
            };
        } else if (type instanceof Field f) {
            return javaTypeToH2(f.getType());
        }
        return null;
    }
    public static Class H2TypeToJava(String s){
        if (s == null) {
            return null;
        }
        return switch (s.toLowerCase()) {
            case "boolean" -> Boolean.class;
            case "tinyint" -> Byte.class;
            case "smallint" -> Short.class;
            case "int" -> Integer.class;
            case "bigint" -> Long.class;
            case "real" -> Float.class;
            case "double" -> Double.class;
            case "varchar" -> String.class;
            case "char" -> Character.class;
            case "date" -> java.sql.Date.class;
            case "time" -> java.sql.Time.class;
            default -> null;
        };
    }

    public static Field setAccessible(Field field) {
        try {
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> boolean isEnum(Class<T> type) {
        return (type != null) && type.isEnum();
    }

    public static <T> Collection<T> resultSetToCollection(ResultSet resultSet, Class<T> type) {
        Collection<T> collection = new ArrayList<>();
        try {
            while (resultSet.next()) {
                T item = type.getConstructor().newInstance();
                Field[] fields = type.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    String fieldType = field.getType().getSimpleName();
                    Object value = resultSetGetForObject(resultSet, fieldType, fieldName);
                    if (Util.isEnum(field.getType())) {
                        value = Enum.valueOf((Class<Enum>) field.getType(), (String) value);
                    }
                    field.set(item, value);
                }
                collection.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return collection;
    }

    public static Object resultSetGetForObject(ResultSet set, String fieldType, String fieldName){
        try {
            return switch (fieldType.toLowerCase()) {
                case "boolean" -> set.getBoolean(fieldName);
                case "byte" -> set.getByte(fieldName);
                case "short" -> set.getShort(fieldName);
                case "integer", "int" -> set.getInt(fieldName);
                case "long" -> set.getLong(fieldName);
                case "float" -> set.getFloat(fieldName);
                case "double" -> set.getDouble(fieldName);
                case "string" -> set.getString(fieldName);
                case "character", "char" -> set.getString(1).charAt(0);
                case "date" -> set.getDate(fieldName);
                case "time" -> set.getTime(fieldName);
                default -> set.getObject(fieldName);
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}


