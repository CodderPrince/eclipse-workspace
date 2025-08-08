package com.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Level implements Serializable, Comparable<Level> {
    private final int levelNum;
    private final String levelPath; //Path of a txt file containing words that the user will need to type.
    private final ArrayList<String> wordsList = new ArrayList<>(); //ArrayList that holds all the words from the mentioned above.

    public Level(int levelNum, String levelPath){
        this.levelNum = levelNum;
        this.levelPath = levelPath;
        readFile();
    }

    //Method that reads every single line of a txt file and adds it into the wordList array.
    public void readFile(){
        if(wordsList.isEmpty()){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(levelPath));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] words = line.split("\\s+");
                    wordsList.addAll(Arrays.asList(words));
                }
                reader.close();
            } catch (IOException e) {
                System.err.println("Error reading file: " + levelPath);
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> getWordsList(){
        return this.wordsList;
    }

    public int getLevelNum(){
        return this.levelNum;
    }

    public String getLevelPath(){
        return this.levelPath;
    }

    public boolean equals(Object object) {
        if (object == null || this.getClass() != object.getClass()) return false;
        Level l = (Level) object;
        return this.levelNum == l.getLevelNum() && this.levelPath.equals(l.getLevelPath());
    }

    public int hashCode() {
        return Objects.hash(levelNum);
    }

    @Override
    public int compareTo(Level o) {
        return Integer.compare(this.levelNum, o.getLevelNum());
    }
}
