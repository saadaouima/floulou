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
}
