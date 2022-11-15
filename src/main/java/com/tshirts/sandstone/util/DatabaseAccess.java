package com.tshirts.sandstone.util;

import com.tshirts.sandstone.util.annotations.H2FieldData;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class is used to access the H2 database
 * through the JDBC driver using simple method calls.
 */
public class DatabaseAccess implements AutoCloseable {
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:file:./src/main/resources/data";
    final String USER = "username";
    final String PASS = "password";
    private Connection conn = null;

    public DatabaseAccess() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (conn != null) {
            System.out.println("Connected to database");
        } else {
            System.out.println("Failed to connect to database");
        }
    }

    public static <T> String getTableName(T type) {
        if (type instanceof Class<?> clazz) {
            return clazz.getSimpleName().toUpperCase() + "S";
        } else {
            return type.getClass().getSimpleName().toUpperCase() + "S";
        }
    }

    public <T> boolean add(T item) {
        if (item == null || !tableExists(item.getClass()) || itemExists(item) || constrainedItemExists(item)) {
            return false;
        }
        String tableName = getTableName(item);
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " VALUES (");
        Field[] fields = item.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Util.setAccessible(fields[i]);
            appendTypeAndData(item, sql, fields[i], false);
            if (i != fields.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");

        try {
            conn.createStatement().execute(sql.toString());
        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Takes in a generic class and turns the class into a table
    public <T> boolean createTable(Class<T> type) {
        if (type == null || tableExists(type)) {
            return false;
        }
        Field[] fields = type.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
        StringBuilder sql = new StringBuilder("CREATE TABLE " + getTableName(type) + " (");

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            sql.append(field.getName()).append(" ");
            sql.append(Util.javaTypeToH2(field));
            String[] modifiers = Arrays.stream(field.getAnnotations())
                    .filter(annotation -> annotation.annotationType().getSimpleName().equals("H2FieldData"))
                    .map(annotation -> (H2FieldData) annotation)
                    .mapMulti((H2FieldData annotation, Consumer<? super String> out) -> {
                        if (annotation.primaryKey()) {
                            out.accept("PRIMARY KEY");
                        }
                        if (annotation.notNull()) {
                            out.accept("NOT NULL");
                        }
                        if (annotation.unique()) {
                            out.accept("UNIQUE");
                        }
                        if (annotation.autoIncrement()) {
                            out.accept("AUTO_INCREMENT");
                        }
                    }).toArray(String[]::new);
            sql.append(" ").append(String.join(" ", modifiers));
            if (i < fields.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");

        try {
            System.out.println(sql);
            conn.createStatement().execute(sql.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public <T> boolean dropTable(Class<T> type) {
        if (!tableExists(type)) {
            return false;
        }
        String tableName = getTableName(type);
        String sql = "DROP TABLE " + tableName;
        try {
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public <T> boolean delete(T item) {
        if (item == null || !tableExists(item.getClass())) {
            return false;
        } else if (!itemExists(item)) {
            return false;
        }
        String tableName = getTableName(item);
        StringBuilder sql = new StringBuilder("DELETE FROM " + tableName);
        h2WhereBuilder(item, sql);
        try {
            conn.createStatement().execute(sql.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private <T> void h2WhereBuilder(T item, StringBuilder sql) {
        Field[] fields = item.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
        }
        sql.append(" WHERE ");
        for (int i = 0; i < fields.length; i++) {
            appendTypeAndData(item, sql, fields[i], true);
            if (i != fields.length - 1) {
                sql.append(" AND ");
            }
        }
        sql.append(";");
    }

    private <T> void appendTypeAndData(T item, StringBuilder sql, Field field, boolean includeFieldName) {
        try {
            Object obj = field.get(item);
            if (includeFieldName) {
                sql.append(field.getName()).append(" = ");
            }
            if (field.getType().equals(String.class) || Util.isEnum(field.getType())) {
                sql.append("'").append(obj).append("'");
            } else {
                sql.append(obj);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public <T> boolean addAll(Collection<T> items) {
        for (T item : items) {
            if (itemExists(item) || constrainedItemExists(item)) {
                return false;
            }
        }
        for (T item : items) {
            add(item);
        }
        return true;
    }

    public <T> boolean tableExists(Class<T> type) {
        if (type == null) {
            return false;
        }
        String tableName = getTableName(type);
        String sql = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '" + tableName + "';";

        try {
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public <T> boolean itemExists(T item) {
        if (item == null || !tableExists(item.getClass())) {
            return false;
        }
        String tableName = getTableName(item);
        StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName);
        h2WhereBuilder(item, sql);
        try {
            return conn.createStatement().executeQuery(sql.toString()).next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public <T> boolean constrainedItemExists(T item){
        if (item == null || !tableExists(item.getClass())) {
            return false;
        }
        Field[] fields = item.getClass().getDeclaredFields();
        boolean itemExists = false;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(H2FieldData.class)) {
                H2FieldData annotation = field.getAnnotation(H2FieldData.class);
                if (annotation != null && (annotation.primaryKey() || annotation.unique())) {
                    StringBuilder sql = new StringBuilder("SELECT * FROM " + getTableName(item));
                    sql.append(" WHERE ");
                    appendTypeAndData(item, sql, field, true);
                    try {
                        itemExists |= conn.createStatement().executeQuery(sql.toString()).next();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return itemExists;

    }

    public <T> Collection<T> getAll(Class<T> type) {
        if (type == null || !tableExists(type)) {
            return null;
        }
        String tableName = getTableName(type);
        // Return a collection of all items in the table
        String sql = "SELECT * FROM " + tableName;
        try {
            ResultSet resultSet = conn.createStatement().executeQuery(sql);
            return Util.resultSetToCollection(resultSet, type);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T, U> T get(Class<T> clazz, String field, U val) {
        if (clazz == null || !tableExists(clazz)) {
            return null;
        }
        String tableName = getTableName(clazz);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(tableName).append(" WHERE ").append(field).append(" = ");
        Arrays.stream(clazz.getDeclaredFields()).
                filter(f -> f.getName().equals(field))
                .findFirst().ifPresent(f -> {
                    if (f.getType().equals(String.class) || Util.isEnum(f.getType())) {
                        sql.append("'").append(val).append("'");
                    } else {
                        sql.append(val);
                    }
                });
        sql.append(";");
        try {
            ResultSet resultSet = conn.createStatement().executeQuery(sql.toString());
            return Util.resultSetToCollection(resultSet, clazz).stream().findFirst().orElse(null);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() throws Exception {
        conn.close();
    }

    public <T, U> boolean update(T item, String type, U val) {
        if (item == null || !tableExists(item.getClass())) {
            return false;
        }
        String tableName = getTableName(item);
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(tableName).append(" SET ").append(type).append(" = ");
        Arrays.stream(item.getClass().getDeclaredFields()).
                filter(f -> f.getName().equals(type))
                .findFirst().ifPresent(f -> {
                    if (f.getType().equals(String.class) || Util.isEnum(f.getType())) {
                        sql.append("'").append(val).append("'");
                    } else {
                        sql.append(val);
                    }
                });
        sql.append(" WHERE ");
        Field[] fields = item.getClass().getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            Util.setAccessible(fields[i]);
            appendTypeAndData(item, sql, fields[i], true);
            if (i != fields.length - 1) {
                sql.append(" AND ");
            }
        }
        sql.append(";");
        try {
            System.out.println(sql);
            conn.createStatement().execute(sql.toString());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public <E> boolean update(E item, Map<String, Object> fields) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(getTableName(item)).append(" SET ");
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            sql.append(entry.getKey()).append(" = ");
            Arrays.stream(item.getClass().getDeclaredFields()).
                    filter(f -> f.getName().equals(entry.getKey()))
                    .findFirst().ifPresent(f -> {
                        if (f.getType().equals(String.class) || Util.isEnum(f.getType())) {
                            sql.append("'").append(entry.getValue()).append("'");
                        } else {
                            sql.append(entry.getValue());
                        }
                    });
            sql.append(", ");
        }
        sql.delete(sql.length() - 2, sql.length());

        sql.append(" WHERE ");
        Field[] fields1 = item.getClass().getDeclaredFields();
        for (int i = 0; i < fields1.length; i++) {
            Util.setAccessible(fields1[i]);
            appendTypeAndData(item, sql, fields1[i], true);
            if (i != fields1.length - 1) {
                sql.append(" AND ");
            }
        }
        sql.append(";");
        try {
            System.out.println(sql);
            conn.createStatement().execute(sql.toString());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
