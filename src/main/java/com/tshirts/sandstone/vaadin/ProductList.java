package com.tshirts.sandstone.vaadin;

import com.google.gson.Gson;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ProductList extends Scroller {
    ArrayList<Product> products = new ArrayList<>();

    public ProductList(String jsonFile) {


        // Generate the list of products that are displayed in the product list from the json file.
        populateProductList(jsonFile);
        // Make a VerticalLayout that contains the rows of products.AS
        VerticalLayout productList = new VerticalLayout();

        for (int i = 0; i < products.size(); i += 3) {
            // Make a row of products.
            HorizontalLayout productRow = new HorizontalLayout();
            productRow.addClassName("product-row");
            productRow.setWidth("100%");
            productRow.setAlignItems(FlexComponent.Alignment.CENTER);
            // Add the products to the row.
            for (int j = 0; j < 3; j++) {
                if (i + j < products.size()) {
                    Image productImage = new Image(products.get(i + j).image, "Product Image");
                    productImage.setWidth("200px");
                    productImage.setHeight("200px");
                    productRow.add(productImage);
                }
            }
            // Add the row to the list of products.
            productList.add(productRow);
        }
        this.setContent(productList);
    }

    public void populateProductList(String location) {
        Gson gson = new Gson();

        // Use GSON to read a JSON file and convert it to a list of products.
        try {
            FileReader reader = new FileReader(location);
            Product[] products = gson.fromJson(reader, Product[].class);
            this.products = new ArrayList<>(List.of(products));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        System.out.println("Products: " + products);
    }
}

