package org.floulou.flouloushop.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {

    private final StringProperty name;
    private final StringProperty price;
    private final StringProperty imagePath;
    private final StringProperty description;  // Add this field

    public Product(String name, String imagePath, String price, String description) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleStringProperty(price);
        this.imagePath = new SimpleStringProperty(imagePath);
        this.description = new SimpleStringProperty(description);  // Initialize the description
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty priceProperty() {
        return price;
    }

    public StringProperty imagePathProperty() {
        return imagePath;
    }

    public StringProperty descriptionProperty() {
        return description;  // Getter for description property
    }

    public String getName() {
        return name.get();
    }

    public String getPrice() {
        return price.get();
    }

    public String getImagePath() {
        return imagePath.get();
    }

    public String getDescription() {
        return description.get();  // Getter for description
    }
}
