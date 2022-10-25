package com.tshirts.sandstone.util;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * This class is used to access the H2 database
 * through the JDBC driver using simple method calls.
 */
public class DatabaseAccess implements AutoCloseable {
    final String JDBC_DRIVER = "org.h2.Driver";
    final String DB_URL = "jdbc:h2:file:./src/main/resources/data";
    final String USER = "username";
    final String PASS = "password";
    boolean debug = true;
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

    private static <T> String getTableName(T type) {
        if (type instanceof Class<?> clazz) {
            return clazz.getSimpleName().toUpperCase() + "S";
        } else {
            return type.getClass().getSimpleName().toUpperCase() + "S";
        }
    }

    public <T> boolean add(T item) {
        if (item == null || !tableExists(item.getClass()) || itemExists(item)) {
            return false;
        }
        String tableName = getTableName(item);
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " VALUES (");
        Field[] fields = item.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            appendTypeAndData(item, sql, fields[i]);
            if (i != fields.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");

        if (debug) {
            System.out.println(sql);
        }
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
            // Print all info about the field
            if (debug) {
                System.out.printf("Name: %s, Type: %s\n", field.getName(), field.getType());
            }
//            ReflectionUtils.makeAccessible(field);
        }
        StringBuilder sql = new StringBuilder("CREATE TABLE " + getTableName(type) + " (");

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            sql.append(field.getName()).append(" ");
            sql.append(Util.javaTypeToH2(field.getType()));
            if (i < fields.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");

        if (debug) {
            System.out.println(sql);
        }
        try {
            conn.createStatement().execute(sql.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public <T> boolean tableExists(Class<T> type) {
        if (type == null) {
            return false;
        }
        String tableName = getTableName(type);
        String sql = "SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = '" + tableName + "';";

        if (debug) {
            System.out.println(sql);
        }
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

    public <T> Collection<T> getAll(Class<T> type) {
        // TODO Implement
        return null;
    }

    public <T, U> T get(Class<T> clazz, String type, U val) {
        // TODO Implement
        return null;
    }

    @Override
    public void close() throws Exception {
        conn.close();
    }

    public <T, U> boolean update(T item, String type, U val) {
        // TODO Implement
        return false;
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
        sql.append(" WHERE ");
        for (int i = 0; i < fields.length; i++) {
            ReflectionUtils.makeAccessible(fields[i]);
            appendTypeAndData(item, sql, fields[i]);
            if (i != fields.length - 1) {
                sql.append(" AND ");
            }
        }
        sql.append(";");
    }

    private <T> void appendTypeAndData(T item, StringBuilder sql, Field field) {
        try {
            Object obj = field.get(item);
            sql.append(field.getName()).append(" = ");
            if (field.getType().equals(String.class) || Util.isEnum(field.getClass())) {
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
            if (itemExists(item)) {
                return false;
            }
        }
        for (T item : items) {
            add(item);
        }
        return true;
    }
}
