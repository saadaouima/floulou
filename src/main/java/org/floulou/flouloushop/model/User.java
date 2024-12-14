package org.floulou.flouloushop.model;

public class User {
    private String username;
    private String password;


    // Constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }

    // Setter for password
    public void setPassword(String password) {
        this.password = password;

    }


    @Override
    public String toString() {
        return "Username: " + username + ", Password: " + password;
    }

    // Method to display user info (for testing purposes)
    public void displayUserInfo() {
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }

    // Main method for testing
    public static void main(String[] args) {
        // Create a new User object
        User user = new User("alice", "securePassword123");

        // Display user information
        user.displayUserInfo();

        // Change password
        user.setPassword("newPassword123");

        // Display updated user information
        System.out.println("\nAfter password update:");
        user.displayUserInfo();
    }
}
