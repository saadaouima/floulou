package org.floulou.flouloushop;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.io.FileDeleteStrategy;
import org.floulou.flouloushop.model.Product;
import org.floulou.flouloushop.model.services.ProductService;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomePageController {

    @FXML
    private GridPane productGrid;

    @FXML
    private TextField searchField; // The search bar TextField

    // A list of products (static list for the example)
    private List<Product> productList = new ArrayList<>();
    private List<Product> filteredProductList = new ArrayList<>(); // Filtered list based on search query

    // Map to hold products in the cart and their respective quantities
    private Map<Product, Integer> cart = new HashMap<>();

    // This method will initialize the page
    public void initialize() {
        // Adding some static products

        ProductService productService = new ProductService();
        List<Product> productsList = productService.getAllProducts();
        // Add products to the product list
        productList.addAll(productsList);

        // Initially, the filtered product list is the same as the full list
        filteredProductList.addAll(productList);

        // Bind the search functionality to the TextField (listener on text input)
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterProducts(newValue));

        // Dynamically add products to the GridPane
        populateGrid(filteredProductList);
    }

    // Method to filter products based on the search query
    private void filterProducts(String query) {
        if (query.isEmpty()) {
            filteredProductList.clear();
            filteredProductList.addAll(productList);  // If no query, show all products
        } else {
            filteredProductList = productList.stream()
                    .filter(product -> product.getName().toLowerCase().contains(query.toLowerCase())) // Case insensitive search
                    .collect(Collectors.toList());
        }
        populateGrid(filteredProductList); // Update the grid with filtered products
    }

    // Method to populate the product grid with the given list of products
    private void populateGrid(List<Product> products) {
        productGrid.getChildren().clear(); // Clear existing grid content

        int row = 0;
        int col = 0;
        for (Product product : products) {
            // Create the product card (VBox to hold image and label)
            VBox productCard = new VBox();
            productCard.setSpacing(10);
            productCard.setStyle("-fx-padding: 10; -fx-border-color: gray; -fx-border-width: 1px; -fx-background-color: white;");

            // Image for product
            Image image = new Image(getClass().getResourceAsStream(product.getImagePath()));
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(200);  // Set the image size
            imageView.setFitWidth(200);
            imageView.setPreserveRatio(true);  // Ensure the aspect ratio is maintained
            productCard.getChildren().add(imageView);

            // Name label for product
            Label nameLabel = new Label(product.getName());
            nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");

            // Price label for product
            Label priceLabel = new Label(product.getPrice() + " DT");
            priceLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: normal; -fx-text-fill: green;");

            // Add name and price labels to the product card
            productCard.getChildren().add(nameLabel);
            productCard.getChildren().add(priceLabel);

            // Product description label (details under the product name)
            Label descriptionLabel = new Label(product.getDescription());
            descriptionLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: normal; -fx-text-fill: gray; -fx-wrap-text: true;");
            descriptionLabel.setMaxWidth(200); // Limit the width of the description for better formatting
            productCard.getChildren().add(descriptionLabel); // Add description below name and price

            // Create an "Add to Cart" button for each product
            Button addToCartButton = new Button("Add to Cart");
            addToCartButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold;");
            addToCartButton.setOnAction(e -> addToCart(product));

            // Add the button to the product card
            productCard.getChildren().add(addToCartButton);

            // Create a "Show Details" button for each product
            Button showDetailsButton = new Button("Show Details");
            showDetailsButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold;");
            showDetailsButton.setOnAction(e -> showProductDetails(product));

            // Add the "Show Details" button to the product card
            productCard.getChildren().add(showDetailsButton);

            // Add the product card to the grid (positioning it)
            productGrid.add(productCard, col, row);

            // Update grid positioning
            col++;
            if (col == 3) {  // 3 columns in a row
                col = 0;
                row++;
            }
        }

        // Set spacing between rows and columns in the grid
        productGrid.setHgap(20);  // Horizontal spacing between items
        productGrid.setVgap(20);  // Vertical spacing between items
    }

    // Method to add a product to the cart
    private void addToCart(Product product) {
        cart.put(product, cart.getOrDefault(product, 0) + 1); // Increment quantity of product in cart
        System.out.println(product.getName() + " added to cart");
    }

    // Method to show the cart in a new scene
    @FXML
    private void handleShowCart() {
        Stage cartStage = new Stage();
        VBox cartVBox = new VBox(10);
        cartVBox.setStyle("-fx-padding: 20;");

        double total = 0; // Variable to store the total price

        // Iterate over the cart to display each product with quantity input and delete button
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            // Create a HBox to hold product name, quantity input, delete button, and price
            HBox productRow = new HBox(10);
            productRow.setStyle("-fx-padding: 5;");

            // Display the product name and price
            Label productLabel = new Label(product.getName() + " - " + product.getPrice() + " DT");

            // Quantity TextField for modifying quantity
            TextField quantityField = new TextField(String.valueOf(quantity));
            quantityField.setMaxWidth(50);
            quantityField.setStyle("-fx-text-inner-color: black;");

            // Button to delete the product from the cart
            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
            deleteButton.setOnAction(e -> {
                cart.remove(product);  // Remove the product from the cart
                //handleShowCart();  // Refresh the cart UI
                refreshCart(cartStage);
            });

            // Calculate total price for this product
            double productTotal = Double.parseDouble(product.getPrice()) * quantity;
            total += productTotal;

            // Create label for displaying the total price for this product
            Label productTotalLabel = new Label("Total: " + productTotal + " DT");

            // Add the product details to the HBox
            productRow.getChildren().addAll(productLabel, quantityField, deleteButton, productTotalLabel);

            // Add the HBox (product row) to the VBox
            cartVBox.getChildren().add(productRow);

            // Update quantity in the cart when the quantity changes
            quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    int newQuantity = Integer.parseInt(newValue);
                    if (newQuantity > 0) {
                        cart.put(product, newQuantity); // Update the quantity in the cart
                        //handleShowCart();  // Refresh the cart UI
                        refreshCart(cartStage);
                    }
                } catch (NumberFormatException ex) {
                    // Handle invalid input (e.g., non-numeric input)
                    quantityField.setText(oldValue); // Reset to previous value if invalid
                }
            });
        }

        // Display the total price at the bottom of the cart
        Label totalLabel = new Label("Total: " + total + " DT");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        cartVBox.getChildren().add(totalLabel);

        // Add Submit and Pay button at the bottom
        Button submitButton = new Button("Submit and Pay");
        submitButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");

        // Disable the button if the cart is empty
        submitButton.setDisable(cart.isEmpty());

        double finalTotal = total;
        submitButton.setOnAction(e -> {
            // Handle payment action here
            handlePayment(finalTotal);
            cartStage.close();
            // You can call a payment service or show a confirmation message
//            if (!cart.isEmpty()) {
//                // Example: Show confirmation dialog
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Payment");
//                alert.setHeaderText("Payment Successful");
//                alert.setContentText("Your payment has been processed successfully.");
//                alert.showAndWait();
//
//                // Optionally, clear the cart after payment
//                cart.clear();
//                //handleShowCart(); // Refresh the cart window after clearing
//                cartStage.close();
//
//            }
        });

        // Add the Submit button to the VBox
        cartVBox.getChildren().add(submitButton);


        // Create and set the new scene for the cart
        Scene cartScene = new Scene(cartVBox, 400, 300);
        cartStage.setScene(cartScene);
        cartStage.setTitle("Shopping Cart");
        cartStage.show();
    }

    @FXML
    private void refreshCart(Stage cartStage) {

        VBox cartVBox = new VBox(10);
        cartVBox.setStyle("-fx-padding: 20;");

        double total = 0; // Variable to store the total price

        // Iterate over the cart to display each product with quantity input and delete button
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            // Create a HBox to hold product name, quantity input, delete button, and price
            HBox productRow = new HBox(10);
            productRow.setStyle("-fx-padding: 5;");

            // Display the product name and price
            Label productLabel = new Label(product.getName() + " - " + product.getPrice() + " DT");

            // Quantity TextField for modifying quantity
            TextField quantityField = new TextField(String.valueOf(quantity));
            quantityField.setMaxWidth(50);
            quantityField.setStyle("-fx-text-inner-color: black;");

            // Button to delete the product from the cart
            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
            deleteButton.setOnAction(e -> {
                cart.remove(product);  // Remove the product from the cart
                //handleShowCart();  // Refresh the cart UI
                refreshCart(cartStage);
            });

            // Calculate total price for this product
            double productTotal = Double.parseDouble(product.getPrice()) * quantity;
            total += productTotal;

            // Create label for displaying the total price for this product
            Label productTotalLabel = new Label("Total: " + productTotal + " DT");

            // Add the product details to the HBox
            productRow.getChildren().addAll(productLabel, quantityField, deleteButton, productTotalLabel);

            // Add the HBox (product row) to the VBox
            cartVBox.getChildren().add(productRow);

            // Update quantity in the cart when the quantity changes
            quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    int newQuantity = Integer.parseInt(newValue);
                    if (newQuantity > 0) {
                        cart.put(product, newQuantity); // Update the quantity in the cart
                        refreshCart(cartStage);
                    }
                } catch (NumberFormatException ex) {
                    // Handle invalid input (e.g., non-numeric input)
                    quantityField.setText(oldValue); // Reset to previous value if invalid
                }
            });
        }

        // Display the total price at the bottom of the cart
        Label totalLabel = new Label("Total: " + total + " DT");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        cartVBox.getChildren().add(totalLabel);

        // Add Submit and Pay button at the bottom
        Button submitButton = new Button("Submit and Pay");
        submitButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");

        // Disable the button if the cart is empty
        submitButton.setDisable(cart.isEmpty());

        double finalTotal = total;
        submitButton.setOnAction(e -> {
            // Handle payment action here
            handlePayment(finalTotal);
            cartStage.close();
//            // You can call a payment service or show a confirmation message
//            if (!cart.isEmpty()) {
//                // Example: Show confirmation dialog
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Payment");
//                alert.setHeaderText("Payment Successful");
//                alert.setContentText("Your payment has been processed successfully.");
//                alert.showAndWait();
//
//                // Optionally, clear the cart after payment
//                cart.clear();
//                //handleShowCart(); // Refresh the cart window after clearing
//                cartStage.close();
//                // Generate receipt
//                generateReceipt();
//
//            }
        });

        // Add the Submit button to the VBox
        cartVBox.getChildren().add(submitButton);


        // Create and set the new scene for the cart
        Scene cartScene = new Scene(cartVBox, 400, 300);
        cartStage.setScene(cartScene);
        cartStage.setTitle("Shopping Cart");
        cartStage.show();
    }

    private boolean validateBankDetails(String nameOnCard, String cardNumber, String expiryDate, double total) {
        // Simulate reading the bank file for account validation
        String bankAccountFilePath = "src/main/resources/files/bank_details.csv"; // Path to the bank file
        try {
            // Simulate reading from the file (for the example)
            BufferedReader reader = new BufferedReader(new FileReader(bankAccountFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                // Check if the line contains valid account information
                // Example format in file: accountNumber;cardNumber;expiryDate;availableCredit
                String[] bankDetails = line.split(";");
                String storedAccountNumber = bankDetails[0];
                String storedCardNumber = bankDetails[1];
                String storedExpiryDate = bankDetails[2];
                double availableCredit = Double.parseDouble(bankDetails[3]);

                // Check if the provided bank details match
                if (nameOnCard.equals(storedAccountNumber) && cardNumber.equals(storedCardNumber) && expiryDate.equals(storedExpiryDate)) {
                    // If account is found, check if there’s enough credit
                    if (availableCredit >= total) {
                        // Write the updated balance back to the file
                        updateBankDetails(storedCardNumber, availableCredit - total);
                        return true; // Payment is successful
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false; // If no match is found or insufficient credit
    }

    @FXML
    private void handlePayment(double total) {
        // Open a new window to enter bank details
        Stage paymentStage = new Stage();
        VBox paymentVBox = new VBox(10);
        paymentVBox.setStyle("-fx-padding: 20;");

        // Add bank details fields
        Label bankLabel = new Label("Enter your Bank Details:");
        Label accountNumberLabel = new Label("Name on card:");
        TextField accountNumberField = new TextField();
        accountNumberField.setPromptText("Enter your name on the card");

        Label cardNumberLabel = new Label("Card Number:");
        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("Enter your card number");

        Label expiryDateLabel = new Label("Expiry Date (MM/YY):");
        TextField expiryDateField = new TextField();
        expiryDateField.setPromptText("Enter expiry date");

        Button payButton = new Button("Pay Now");
        payButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px;");

        // Validate payment when clicked
        payButton.setOnAction(e -> {
            String accountNumber = accountNumberField.getText();
            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryDateField.getText();

            if (validateBankDetails(accountNumber, cardNumber, expiryDate, total)) {
                // If valid, show success alert and proceed with payment
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Payment Successful");
                successAlert.setHeaderText("Your payment has been processed.");
                successAlert.setContentText("Thank you for your purchase! The total amount of " + total + " DT has been deducted.");
                successAlert.showAndWait();

                // Clear the cart
                cart.clear();
                paymentStage.close();
            } else {
                // If not valid, show error alert
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Payment Failed");
                errorAlert.setHeaderText("Transaction Error");
                errorAlert.setContentText("Invalid bank details or insufficient funds.");
                errorAlert.showAndWait();
            }
        });

        paymentVBox.getChildren().addAll(bankLabel, accountNumberLabel, accountNumberField, cardNumberLabel, cardNumberField, expiryDateLabel, expiryDateField, payButton);

        // Create and show payment scene
        Scene paymentScene = new Scene(paymentVBox, 400, 300);
        paymentStage.setScene(paymentScene);
        paymentStage.setTitle("Bank Payment");
        paymentStage.show();
    }


    // Method to show product details in a new window
    private void showProductDetails(Product product) {
        Stage detailsStage = new Stage();
        VBox detailsVBox = new VBox(10);
        detailsVBox.setStyle("-fx-padding: 20;");

        // Add product details to the VBox
        Label nameLabel = new Label("Product: " + product.getName());
        Label priceLabel = new Label("Price: " + product.getPrice());
        Label descriptionLabel = new Label("Description: " + product.getDescription());

        detailsVBox.getChildren().addAll(nameLabel, priceLabel, descriptionLabel);

        // Create and set the scene for product details
        Scene detailsScene = new Scene(detailsVBox, 400, 300);
        detailsStage.setScene(detailsScene);
        detailsStage.setTitle("Product Details");
        detailsStage.show();
    }



    private void updateBankDetails(String accountNumber, double newBalance) {
        File bankFile = new File("src/main/resources/files/bank_details.csv");

        try {
            // Step 1: Read the entire file into a single string
            String content = new String(Files.readAllBytes(bankFile.toPath()));

            // Step 2: Update the content
            StringBuilder updatedContent = new StringBuilder();
            String[] lines = content.split("\n");
            boolean accountUpdated = false;

            for (String line : lines) {
                String[] columns = line.split(";");

                if (columns.length == 4 && columns[1].equals(accountNumber)) {
                    // Update the account balance (the last column, index 3)
                    columns[3] = String.valueOf(newBalance);
                    accountUpdated = true;
                }

                // Rebuild the line and append it to the updated content
                updatedContent.append(String.join(";", columns)).append("\n");
            }

            // Step 3: Write the updated content back to the file
            if (accountUpdated) {
                Files.write(bankFile.toPath(), updatedContent.toString().getBytes());
                System.out.println("Account balance updated successfully.");
            } else {
                System.err.println("Account number not found in the file.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    // Inner Product class to hold product data
//    public static class Product {
//        private String name;
//        private String imagePath;
//        private String price;
//        private String description;
//
//        public Product(String name, String imagePath, String price, String description) {
//            this.name = name;
//            this.imagePath = imagePath;
//            this.price = price;
//            this.description = description;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public String getImagePath() {
//            return imagePath;
//        }
//
//        public String getPrice() {
//            return price;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            Product product = (Product) o;
//            return name.equals(product.name) && imagePath.equals(product.imagePath);
//        }
//
//        @Override
//        public int hashCode() {
//            return name.hashCode() + imagePath.hashCode();
//        }
//    }
}
