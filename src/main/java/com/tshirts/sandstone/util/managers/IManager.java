package com.tshirts.sandstone.util.managers;

import java.io.File;
import java.util.Map;

public interface IManager<T> {
    boolean export(File path);
    boolean import_(File path);
    boolean add(T item);
    boolean remove(T item);
    boolean update(T item, Map<String, Object> fields);

    T[] getAll();
    T[] findBy(String type, String value);

}
