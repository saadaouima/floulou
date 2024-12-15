package org.floulou.flouloushop.model.services;

import org.floulou.flouloushop.model.Product;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private final String PRODUCTS_FILE_PATH = "src/main/resources/files/products.csv";


    public List<Product> getAllProducts(){
        return loadProductsFromCSV(PRODUCTS_FILE_PATH);
    }


    private List<Product> loadProductsFromCSV(String filePath) {
        List<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip header line
            reader.readLine();

            // Read each product line from CSV
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 4) {
                    String name = data[0];
                    String imagePath = data[1];
                    String price = data[2];
                    String description = data.length > 3 ? data[3] : ""; // Default to empty string if no description
                    products.add(new Product(name, imagePath, price, description));
                }
            }

            // Add the products to the ObservableList
            //productList.addAll(products);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    public void saveProductsToFile(List<Product> productList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PRODUCTS_FILE_PATH))) {
            // Write the header
            writer.write("Product Name;Price;Image URL;Description\n");

            // Write product data
            for (Product product : productList) {
                writer.write(product.getName() + ";" + product.getPrice() + ";" +
                        product.getImagePath() + ";" + product.getDescription() + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
