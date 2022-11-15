package com.tshirts.sandstone.vaadin.components;

import com.tshirts.sandstone.util.Product;
import com.tshirts.sandstone.util.Util;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//TODO Fix static shit
public class ProductList extends VerticalLayout {
    static Grid<Product> grid = new Grid<>(Product.class);
    static List<Product> products;

    public ProductList() {
        Collection<Product> dbAll = Util.DB.getAll(Product.class);
        if (dbAll != null) {
            products = new ArrayList<>(dbAll);
        } else {
            products = new ArrayList<>();
        }

        grid.setClassName("product-grid");
        grid.setWidth("100%");
        grid.setHeight("100%");

        grid.addComponentColumn(product -> {
            Image image = new Image(product.image, "Product Image");
            image.setHeight("100px");
            return image;
        }).setHeader("Image");
        grid.setColumns("name", "price", "description");
        grid.getColumnByKey("name").setHeader("Name");
        grid.getColumnByKey("price").setHeader("Price");
        grid.getColumnByKey("description").setHeader("Description");
        grid.setItems(products);

        add(grid);
    }

    public static void update() {
        products = new ArrayList<>(Util.DB.getAll(Product.class));
        grid.setItems(products);
    }
}

