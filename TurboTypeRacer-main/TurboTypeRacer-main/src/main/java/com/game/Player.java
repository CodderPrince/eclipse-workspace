package com.game;


import com.gui.SettingsScreenController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Player implements Serializable {
    private String name;
    private String password;
    private String phoneNumber;
    private String profileImage;
    private GameSettings gameSettings;
    private Vehicle selectedCar;
    private ArrayList<Vehicle> garageVehicles = new ArrayList<>();
    private HashMap<Level, Integer> scoreMap = new HashMap<>(); //Stores user scores for each level.

    public Player(String name, String password, String phoneNumber, String profileImage, ArrayList<Level> levels){
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        InitializeScoreMap(levels);
        this.selectedCar = new Vehicle("Car1", "/assets/vehicles/car1.gif");
        garageVehicles.add(this.selectedCar);
        this.gameSettings = new GameSettings(SettingsScreenController.getGameSettings());
    }


    public void InitializeScoreMap(ArrayList<Level> levels) {
        for(Level level : levels) {
            this.scoreMap.put(level, 0);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setImage(String profileImage){
        this.profileImage = profileImage;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public String getProfileImage() {
        return this.profileImage;
    }

    public int getScore(Level level){
        return this.scoreMap.get(level);
    }

    public ArrayList<Vehicle> getGarage() {
        return garageVehicles;
    }

    public void setLevelAndScore(Level level, int score){
        this.scoreMap.put(level, score);
    }

    public HashMap<Level, Integer> getScoreMap() {
        return this.scoreMap;
    }

    public GameSettings getGameSettings() {
        return this.gameSettings;
    }

    public void setSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public Vehicle getSelectedCar() {
        return selectedCar;
    }

    public void setSelectedCar(Vehicle selectedCar) {
        this.selectedCar = selectedCar;
    }
}
