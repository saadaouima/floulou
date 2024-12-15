package org.floulou.flouloushop;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.cell.TextFieldTableCell;
import org.floulou.flouloushop.model.Product;
import org.floulou.flouloushop.model.services.ProductService;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardController {

    @FXML
    private TextField productNameField;
    @FXML
    private TextField productPriceField;
    @FXML
    private TextField productImageField;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, String> priceColumn;
    @FXML
    private TableColumn<Product, String> imageColumn;
    @FXML
    private TableColumn<Product, String> descriptionColumn;

    private ObservableList<Product> productList;


    @FXML
    private void initialize() {
        System.out.println("Initializing the Admin Dashboard Controller...");


        productTable.setEditable(true);
        // Initialize product list
        ProductService productService = new ProductService();
        List<Product> productsList = productService.getAllProducts();

        // Ensure the product list is fetched
        System.out.println("Fetched products: " + productsList.size());

        // Initialize ObservableList with the fetched products
        productList = FXCollections.observableArrayList(productsList);

        // Check if the ObservableList has products
        System.out.println("ObservableList size: " + productList.size());

        // Set up TableColumns bindings
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        imageColumn.setCellValueFactory(cellData -> cellData.getValue().imagePathProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        // Make columns editable by setting cell factories
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        imageColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Listen for changes to the columns and update the list
        nameColumn.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.nameProperty().set(event.getNewValue());
        });

        priceColumn.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.priceProperty().set(event.getNewValue());
        });

        imageColumn.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.imagePathProperty().set(event.getNewValue());
        });

        descriptionColumn.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.descriptionProperty().set(event.getNewValue());
        });

        productTable.getColumns().add(nameColumn);
        productTable.getColumns().add(priceColumn);
        productTable.getColumns().add(imageColumn);
        productTable.getColumns().add(descriptionColumn);

        // Set the ObservableList to the TableView
        productTable.setItems(productList);
        System.out.println("-------------------- "+productTable.getColumns().size());

        // Debug: Print out the items in the table
        productList.forEach(product -> System.out.println(product.getName() + ", " + product.getPrice() + ", " + product.getDescription()));
        System.out.println("nameColumn: " + nameColumn);
        System.out.println("priceColumn: " + priceColumn);
        System.out.println("imageColumn: " + imageColumn);
        System.out.println("descriptionColumn: " + descriptionColumn);
    }


//    @FXML
//    private void initialize() {
//        // Initialize the product list
//        ProductService productService = new ProductService();
//        List<Product> productsList = productService.getAllProducts();
//
//        // Debugging: Check if productsList is valid
//        System.out.println("Fetched products: " + productsList.size());
//
//        // Convert list to ObservableList
//        productList = FXCollections.observableArrayList(productsList);
//        System.out.println("ObservableList size: " + productList.size());  // Debugging
//
//        // Set up columns for the TableView
//        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
//        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
//        imageColumn.setCellValueFactory(cellData -> cellData.getValue().imagePathProperty());
//
//        // Set the items in the TableView
//        productTable.setItems(productList);
//
//        // Optionally, debug to ensure TableView is populated
//        System.out.println("Product table size: " + productTable.getItems().size());  // Debugging
//    }

    @FXML
    private void handleSaveChanges() {
        ProductService productService = new ProductService();
        productService.saveProductsToFile(new ArrayList<>(productList));
        System.out.println("Products saved to file!");
    }

    @FXML
    private void handleDeleteProduct() {
        // Get the selected product
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();

        // Check if a product is selected
        if (selectedProduct != null) {
            // Remove the selected product from the ObservableList
            productList.remove(selectedProduct);

            // Optional: Save the updated list to the file after deletion
            ProductService productService = new ProductService();
            productService.saveProductsToFile(new ArrayList<>(productList));

            // Notify user about successful deletion
            showAlert("Product Deleted", "The product has been deleted successfully.", Alert.AlertType.INFORMATION);
        } else {
            // If no product is selected, show an alert
            showAlert("No Product Selected", "Please select a product to delete.", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Handle adding a product
    @FXML
    public void handleAddProduct() {
        String name = productNameField.getText();
        String price = productPriceField.getText();
        String image = productImageField.getText();

        if (name.isEmpty() || price.isEmpty() || image.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        // Create a new Product object and add it to the list
        Product newProduct = new Product(name, image, price, "No description available.");
        productList.add(newProduct);

        // Clear input fields
        productNameField.clear();
        productPriceField.clear();
        productImageField.clear();

    }
}