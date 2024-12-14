package org.floulou.flouloushop;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class HomePageController {

    @FXML
    private GridPane productGrid;

    // A list of products (static list for the example)
    private List<Product> productList = new ArrayList<>();

    // This method will initialize the page
    public void initialize() {
        // Adding some static products
        productList.add(new Product("Product 1", "/images/laptop.jpeg"));
        productList.add(new Product("Product 2", "/images/laptop.jpeg"));
        productList.add(new Product("Product 3", "/images/laptop.jpeg"));

        // Dynamically add products to the GridPane
        int row = 0;
        for (Product product : productList) {
            // Create the label for the product name
            Label nameLabel = new Label(product.getName());
            nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            // Create the ImageView for the product image
            Image image = new Image(getClass().getResourceAsStream(product.getImagePath()));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(100);  // Set the image size
            imageView.setFitWidth(100);

            // Add the name label and image to the grid at the correct position
            productGrid.add(nameLabel, 0, row);
            productGrid.add(imageView, 1, row);

            // Move to the next row for the next product
            row++;
        }
    }

    // Inner Product class to hold product data
    public static class Product {
        private String name;
        private String imagePath;

        public Product(String name, String imagePath) {
            this.name = name;
            this.imagePath = imagePath;
        }

        public String getName() {
            return name;
        }

        public String getImagePath() {
            return imagePath;
        }
    }
}

