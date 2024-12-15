package org.floulou.flouloushop.model.services;

import org.floulou.flouloushop.model.User;

import java.io.*;
import java.util.*;

public class UserService {

    private final String USERS_FILE_PATH = "src/main/resources/files/users.csv";

    public User findUser(String username , String password){
         List<User> usersList = parseCSV(USERS_FILE_PATH);
         User user = new User(username , password);
        // Method to check if a user with the given username and password exists
        if (contains(username,password,usersList)) return user ;
        else return null;


    }
    // Method to check if a user with the given username and password exists
    public boolean contains(String username, String password , List<User> userList) {
        for (User user : userList) {
            // Compare username and password for each user in the list
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true; // User found
            }
        }
        return false; // User not found
    }

    public List<User> parseCSV(String filePath) {
        List<User> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                String username = fields[0];
                String password = fields[1];

                // Create User object and add to list
                users.add(new User(username, password));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    // Method to register a user and write to CSV
    public boolean registerUser(String username, String password) {
        // Check if the username already exists
        if (isUserExist(username)) {
            System.out.println("Error: Username already exists.");
            return false;
        } else {
            // If the username doesn't exist, save it to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE_PATH, true))) {
                // Write the username and password to the CSV file
                writer.write(username + "," + password);
                writer.newLine();  // Add a new line after each entry
                System.out.println("User registered successfully!");
                return true;
            } catch (IOException e) {
                System.out.println("An error occurred while saving the user.");
                e.printStackTrace();
                return false;
            }
        }
    }

    // Method to check if the username already exists in the CSV file
    private boolean isUserExist(String username) {
        File file = new File(USERS_FILE_PATH);
        if (file.exists()) {
            try (Scanner fileScanner = new Scanner(file)) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    String existingUsername = line.split(",")[0]; // Extract the username part
                    if (existingUsername.equals(username)) {
                        return true; // Username already exists
                    }
                }
            } catch (IOException e) {
                System.out.println("An error occurred while checking the user.");
                e.printStackTrace();
            }
        }
        return false; // Username does not exist
    }
}
