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
        return users.get(name + age); // Consider using a more unique identifier
    }

    public void addUser(User user) {
        String userId = user.getName() + user.getAge(); // This might need a more unique approach
        if (!users.containsKey(userId)) {
            users.put(userId, user);
            saveUsers();
        } else {
            System.out.println("User already exists. Consider updating instead of adding.");
        }
    }

    @SuppressWarnings("unchecked")
	private synchronized void loadUsers() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("No existing data file found. Starting fresh.");
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            users = (Map<String, User>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load user data: " + e.getMessage());
        }
    }

    private synchronized void saveUsers() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            out.writeObject(users);
        } catch (IOException e) {
            System.out.println("Failed to save user data: " + e.getMessage());
        }
    }
}
