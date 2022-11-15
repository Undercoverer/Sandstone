package com.tshirts.sandstone.util;

import com.tshirts.sandstone.util.annotations.H2FieldData;

public class Product {

    @H2FieldData(primaryKey = true, notNull = true)
    public String name;
    @H2FieldData(notNull = true)
    public String description;
    @H2FieldData(notNull = true)
    public double price;
    @H2FieldData(notNull = true)
    public String image;

    public Product(String name, String description, double price, String image) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
    }

    public Product() {
    }

    @Override
    public String toString() {
        return "Product[" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ']';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
