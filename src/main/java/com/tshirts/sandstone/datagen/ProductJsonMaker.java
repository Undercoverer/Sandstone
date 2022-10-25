package com.tshirts.sandstone.datagen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tshirts.sandstone.util.Product;

import java.io.FileWriter;

public class ProductJsonMaker {
    public static void main(String[] args) {
        String[] productNames = {"T-Shirt", "Hoodie", "Sweatshirt", "Jacket", "Polo Shirt", "Long Sleeve Shirt",
                "Short Sleeve Shirt", "Tank Top", "Sweater", "Vest", "Pants", "Shorts", "Socks", "Underwear", "Hat",
                "Beanie", "Gloves", "Scarf", "Belt", "Backpack", "Laptop Bag", "Messenger Bag", "Wallet", "Watch",
                "Sunglasses", "Umbrella", "Phone Case", "Keychain", "Tie", "Bow Tie", "Cufflinks", "Suspenders",
                "Bottle Opener", "Tie Clip", "Tie Bar", "Money Clip", "Cigar Cutter", "Mug", "Cup", "Coaster",
                "Tumbler", "Water Bottle", "Can Cooler", "Shot Glass", "Shot Glass Set", "Shot Glass Holder", "Minecraft",
                "Fortnite", "PUBG", "Overwatch", "League of Legends", "Dota 2", "Hearthstone", "World of Warcraft",
                "Starcraft", "Diablo", "Heroes of the Storm", "Call of Duty", "Destiny", "Halo", "Gears of War",
                "Borderlands", "Fallout", "Assassin's Creed", "Far Cry", "Tom Clancy's", "Rainbow Six", "Ghost Recon",
                "Cat", "Dog", "Bird", "Fish", "Reptile", "Amphibian", "Insect", "Mammal", "Arachnid", "Mars", "Jupiter",
                "Saturn", "Uranus", "Neptune", "Pluto", "Mercury", "Venus", "Earth", "Sun", "Moon", "Asteroid", "Comet",
                "Meteor", "Star", "Galaxy", "Universe", "Black Hole", "Nebula", "Constellation", "Astronaut", "Astronomy"};

        String[] productDescriptions = new String[productNames.length];
        for (int i = 0; i < productDescriptions.length; i++) {
            productDescriptions[i] = "This is a most wonderful " + productNames[i].toLowerCase() + "." + " It is one of the best " + productNames[i] + " you will ever find.";
        }

        double[] productPrices = new double[productNames.length];
        for (int i = 0; i < productPrices.length; i++) {
            productPrices[i] = Math.random() * 1000;
        }

        String[] productImages = new String[productNames.length];
        for (int i = 0; i < productImages.length; i++) {
            productImages[i] = "https://picsum.photos/seed/" + i + "/200";
        }
        Product[] products = new Product[productNames.length];

        for (int i = 0; i < products.length; i++) {
            products[i] = new Product(productNames[i], productDescriptions[i], productPrices[i], productImages[i]);
            System.out.println(products[i]);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();


        // Write the file to the disk.
        try {
            FileWriter writer = new FileWriter("src/main/resources/products.json");
            writer.write(gson.toJson(products, Product[].class));
            writer.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }
}
