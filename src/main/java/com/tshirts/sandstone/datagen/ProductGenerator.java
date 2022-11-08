package com.tshirts.sandstone.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tshirts.sandstone.util.PermissionLevel;
import com.tshirts.sandstone.util.Product;
import com.tshirts.sandstone.util.Util;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class ProductGenerator {

    public static void main(String[] args) {
        Collection<Product> all = Util.DB.getAll(Product.class);
        if (all == null || all.size() == 0) {
            generateProducts();
        } else {
            System.out.println("Products already exist");
            Util.DB.dropTable(Product.class);
            generateProducts();
        }


    }

    private static void generateProducts() {
        System.out.println("Generating Products");
        int count = 50;
        ArrayList<Product> Products = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            final String description = ("description" + i);
            final String name = ("name" + i);
            final double price = i;
            final String photo = ("photo" + i);
            Products.add(new Product(name, description, price, photo));
        }
        // Add test Product.
        Products.add(new Product("testName", "testDesc", 0.00, "testPhoto"));
        Util.DB.createTable(Product.class);
        Util.DB.addAll(Products);
    }

}

