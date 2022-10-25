package com.tshirts.sandstone.util.managers;

import com.google.gson.Gson;
import com.tshirts.sandstone.util.Product;
import com.tshirts.sandstone.util.Util;
import com.tshirts.sandstone.vaadin.components.ProductList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.stream.Stream;

public class ProductManager implements IManager<Product> {
    static ArrayList<Product> products;
    private static ProductManager instance = null;


    private ProductManager() {
        if (!Util.DB.tableExists(Product.class)) {
            Util.DB.createTable(Product.class);
        }
        products = new ArrayList<>();
        products.addAll(Util.DB.getAll(Product.class));
    }

    public static ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    public Product[] findBy(String type, String value) {
        Stream<Product> ProductStream = products.stream();
        return switch (type) {
            case "name" ->
                    ProductStream.filter(Product -> Product.getName().toLowerCase().contains(value.toLowerCase())).toArray(Product[]::new);
            case "description" ->
                    ProductStream.filter(Product -> Product.getDescription().toLowerCase().contains(value.toLowerCase())).toArray(Product[]::new);
            case "priceGreaterThan" ->
                    ProductStream.filter(Product -> Product.getPrice() > Double.parseDouble(value)).toArray(Product[]::new);
            case "priceLessThan" ->
                    ProductStream.filter(Product -> Product.getPrice() < Double.parseDouble(value)).toArray(Product[]::new);
            case "priceEqualTo" ->
                    ProductStream.filter(Product -> Math.abs(Product.getPrice() - Double.parseDouble(value)) < 0.005).toArray(Product[]::new);
            default -> null;
        };
    }

    public boolean export(File path) {
        Gson gson = new Gson();
        // To array
        Product[] ProductArray = new Product[products.size()];
        ProductArray = products.toArray(ProductArray);
        // To json
        return Util.write(path, gson, ProductArray);
    }

    public boolean import_(File path) {
        Gson gson = new Gson();
        Product[] productArray;
        try (FileReader file = new FileReader(path)) {
            // Read the whole file
            BufferedReader reader = new BufferedReader(file);
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            // Parse the json
            productArray = gson.fromJson(builder.toString(), Product[].class);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        for (Product product : productArray) {
            add(product);
        }
        ProductList.update();
        return true;
    }

    public boolean add(Product product) {
        products.add(product);
        return Util.DB.add(product);

    }

    public boolean remove(Product product) {
        products.remove(product);
        return Util.DB.delete(product);
    }

    @Override
    public boolean update(Product product, String field, Object value) {
        try {
            Field f = product.getClass().getDeclaredField(field);
            Util.setAccessible(f);
            f.set(product, value);
            return Util.DB.update(product, field, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Product get(String name) {
        return Util.DB.get(Product.class, "name", name);
    }

    public Product[] getAll() {
        return products.toArray(new Product[0]);
    }


}
