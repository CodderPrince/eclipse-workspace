package application;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private int age;
    private Map<String, Integer> scores;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
        this.scores = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScore(String game, int score) {
        scores.put(game, Math.max(scores.getOrDefault(game, 0), score));
    }

    public int getScore(String game) {
        return scores.getOrDefault(game, 0);
    }
}
