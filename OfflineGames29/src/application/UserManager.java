package application;

import java.io.*;
import java.util.*;

public class UserManager {
    private static final String DATA_FILE = "users.dat";
    private Map<String, User> users = new HashMap<>();

    public UserManager() {
        loadUsers();
    }

    public User getUser(String name, int age) {
        return users.get(name + age);
    }

    public void addUser(User user) {
        users.put(user.getName() + user.getAge(), user);
        saveUsers();
    }

    private void loadUsers() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            users = (Map<String, User>) in.readObject();
        } catch (Exception e) {
            System.out.println("No existing user data found.");
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(users);
        } catch (IOException e) {
            System.out.println("Failed to save user data.");
        }
    }
}
