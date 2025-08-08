package com.game;

import com.gui.StartScreen;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class LoginManager {
    private static Player currentPlayer; //Represents the player currently logged in.
    private static ArrayList<Player> playerDatabase = new ArrayList<>(); //Stores all the players.

    //Used to load the playerDatabase.
    public static void initializeLoginManager(String fileName) {
        String path = Objects.requireNonNull(StartScreen.class.getResource("/playerDb/")).getPath()+fileName;
        File dbFile = new File(path);
        // Checks if a file named after the parameter "fileName" exists in the playDb directory.
        if(!dbFile.exists()){
            try{
                dbFile.createNewFile(); //Creates file if it doesn't exist.
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        if(dbFile.length()!=0){
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
                playerDatabase = (ArrayList<Player>) in.readObject(); //imports the stored playerDatabase.
            } catch (IOException | ClassNotFoundException e ) {
                e.printStackTrace();
            }
        }
    }

    //Used to save the playerDatabase.
    public static void savePlayerDatabase(String fileName){
        if(!playerDatabase.isEmpty()){
            String path = Objects.requireNonNull(StartScreen.class.getResource("/playerDb/" + fileName)).getPath();
            try (FileOutputStream fos = new FileOutputStream(path);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(playerDatabase);
                oos.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void exportPlayerScores(String folderPath, String fileName){
        if(!playerDatabase.isEmpty()){
            String path = folderPath+"/"+fileName;
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path));

                for(Player p : playerDatabase){
                    for(Level l : p.getScoreMap().keySet()){
                        bufferedWriter.write("Player " + p.getName() + " has achieved " + p.getScore(l) + " victories on the " + l.getLevelNum() + " level!" + System.lineSeparator());
                    }
                    bufferedWriter.write(System.lineSeparator());
                }

                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Checks if a player with the specified name exists in the playerDatabase.
    // Returns true if found, false otherwise. Returns false if playerDatabase is null.
    public static boolean playerExists(String name){
        if(playerDatabase==null){
            return false;
        }
        for(Player p : playerDatabase){
            if(p.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    //Checks if a player gave the correct username and password.
    //Returns true if he did, false otherwise.
    public static boolean isValidLogin(String username, String password) {
        if(playerDatabase==null){
            return false;
        }

        for(Player p : playerDatabase){
            if(p.getName().equals(username) && p.getPassword().equals(password)){
                return true;
            }
        }

        return false;
    }

    public static void addUser(Player p) {
        playerDatabase.add(p);
    }

    public static void addCurrentPlayer(Player p) {
        currentPlayer = p;
    }

    //Returns the player with the following username if he exists.
    //Returns null otherwise.
    public static Player getPlayer(String username) {

        if(playerDatabase==null){
            return null;
        }

        for (Player p : playerDatabase) {
            if(p.getName().equals(username)){
                return p;
            }
        }

        return null;
    }

    public static void setCurrentPlayer(Player tempPlayer) {
        currentPlayer = tempPlayer;
    }

    public static Player getCurrentPlayer() {
        return currentPlayer;
    }

    public static ArrayList<Player> getPlayerDatabase() {
        return playerDatabase;
    }
}
