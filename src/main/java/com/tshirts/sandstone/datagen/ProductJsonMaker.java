package com.tshirts.sandstone.datagen;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.tshirts.sandstone.util.Product;
import elemental.json.Json;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProductJsonMaker {
    public static void main(String[] args) throws IOException {
//        String[] productNames = {"T-Shirt", "Hoodie", "Sweatshirt", "Jacket", "Polo Shirt", "Long Sleeve Shirt",
//                "Short Sleeve Shirt", "Tank Top", "Sweater", "Vest", "Pants", "Shorts", "Socks", "Underwear", "Hat",
//                "Beanie", "Gloves", "Scarf", "Belt", "Backpack", "Laptop Bag", "Messenger Bag", "Wallet", "Watch",
//                "Sunglasses", "Umbrella", "Phone Case", "Keychain", "Tie", "Bow Tie", "Cufflinks", "Suspenders",
//                "Bottle Opener", "Tie Clip", "Tie Bar", "Money Clip", "Cigar Cutter", "Mug", "Cup", "Coaster",
//                "Tumbler", "Water Bottle", "Can Cooler", "Shot Glass", "Shot Glass Set", "Shot Glass Holder", "Minecraft",
//                "Fortnite", "PUBG", "Overwatch", "League of Legends", "Dota 2", "Hearthstone", "World of Warcraft",
//                "Starcraft", "Diablo", "Heroes of the Storm", "Call of Duty", "Destiny", "Halo", "Gears of War",
//                "Borderlands", "Fallout", "Assassin's Creed", "Far Cry", "Tom Clancy's", "Rainbow Six", "Ghost Recon",
//                "Cat", "Dog", "Bird", "Fish", "Reptile", "Amphibian", "Insect", "Mammal", "Arachnid", "Mars", "Jupiter",
//                "Saturn", "Uranus", "Neptune", "Pluto", "Mercury", "Venus", "Earth", "Sun", "Moon", "Asteroid", "Comet",
//                "Meteor", "Star", "Galaxy", "Universe", "Black Hole", "Nebula", "Constellation", "Astronaut", "Astronomy"};

        // Use wordnet to get a list of nouns and adjectives to use as product names
        // The definition of the word will be the description
        // The price will be a random number between 5 and 100
        // The image will be a random image from the internet

        // Create 100 connections to https://random-words-api.vercel.app/word and take out the word and description
        // Put the word in productNames and the description in productDescriptions

        String[] productNames = new String[100];
        String[] productDescriptions = new String[100];


        IntStream.range(0, 100).parallel().forEach(i -> {
            try {
                System.out.println("Getting word " + i);
                URL url = new URL("https://random-words-api.vercel.app/word");
                HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                JsonElement element = JsonParser.parseString(content.toString());
                JsonObject object = element.getAsJsonArray().get(0).getAsJsonObject();
                productNames[i] = object.get("word").getAsString().replace("'", "").replace("\"", "");
                productDescriptions[i] = object.get("definition").getAsString().replace("'", "").replace("\"", "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        double[] productPrices = new double[productNames.length];
        for (int i = 0; i < productPrices.length; i++) {
            productPrices[i] = Math.random() * 100;
        }

        String[] productImages = new String[productNames.length];
        for (int i = 0; i < productImages.length; i++) {
            productImages[i] = "https://picsum.photos/seed/" + (i + 1) + "/200";
        }
        Product[] products = new Product[productNames.length];

        for (int i = 0; i < products.length; i++) {
            products[i] = new Product(productNames[i], productDescriptions[i], productPrices[i], productImages[i]);
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
