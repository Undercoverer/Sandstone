package com.tshirts.sandstone.util;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;

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
        String simpleName = type.getClass().getSimpleName();
        if (type.getClass().isEnum()) {
            return "VARCHAR(255)";
        }
        return switch (simpleName) {
            case "Boolean" -> "BOOLEAN";
            case "Byte" -> "TINYINT";
            case "Short" -> "SMALLINT";
            case "Integer" -> "INT";
            case "Long" -> "BIGINT";
            case "Float" -> "REAL";
            case "Double" -> "DOUBLE";
            case "String" -> "VARCHAR";
            case "Character" -> "CHAR";
            case "Date" -> "DATE";
            case "Time" -> "TIME";

            default -> null;
        };
    }

    public static boolean setAccessible(Field field) {
        try {
            field.setAccessible(true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static <T> boolean isEnum(Class<T> type){
        System.out.println(type);
        return (type != null) && type.isEnum();
    }
}
