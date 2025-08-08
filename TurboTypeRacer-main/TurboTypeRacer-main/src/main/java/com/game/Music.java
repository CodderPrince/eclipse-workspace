package com.game;
import com.gui.SettingsScreenController;
import com.gui.StartScreen;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music {
    private static MediaPlayer mediaPlayer;
    private static final String startScreenSong = "/sounds/startScreenSong.mp3";
    private static String gameScreenSong = "/sounds/gameScreenSong.mp3";
    private static String buttonSong =  "/sounds/buttonSound.mp3";
    private static String popSong =  "/sounds/poppingSound.mp3";

    private static String winSong =  "/sounds/winSound.mp3";

    private static String loseSong =  "/sounds/loseSound.mp3";


    public Music() {
        changeSong(startScreenSong);
    }

    public static void changeSong(String song) {
        if (mediaPlayer!= null)
            mediaPlayer.dispose();
        Media media = new Media(StartScreen.class.getResource(song).toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.4);
        mediaPlayer.setMute(!SettingsScreenController.getGameSettings().isMusicSound());
        mediaPlayer.play();
    }

    public static Boolean muteMediaPlayer() {
        mediaPlayer.setMute(!mediaPlayer.isMute());
        return mediaPlayer.isMute();
    }

    public static void playButtonSound () {
        if(SettingsScreenController.getGameSettings().isSoundEfectsSound()) {
            Media song = new Media(StartScreen.class.getResource(buttonSong).toExternalForm());
            new MediaPlayer(song).play();
        }
    }

    public static void playPopSound () {
        if(SettingsScreenController.getGameSettings().isSoundEfectsSound()) {
            Media song = new Media(StartScreen.class.getResource(popSong).toExternalForm());
            new MediaPlayer(song).play();
        }
    }

    public static void playWinSound () {
        if(SettingsScreenController.getGameSettings().isSoundEfectsSound()) {
            Media song = new Media(StartScreen.class.getResource(winSong).toExternalForm());
            new MediaPlayer(song).play();
        }
    }

    public static void playLoseSound () {
        if(SettingsScreenController.getGameSettings().isSoundEfectsSound()) {
            Media song = new Media(StartScreen.class.getResource(loseSong).toExternalForm());
            new MediaPlayer(song).play();
        }
    }

    public static void setVolume(double volume){
        mediaPlayer.setVolume(volume);
    }

    public static String getStartScreenSong(){
        return startScreenSong;
    }

    public static String getGameScreenSong(){
        return gameScreenSong;
    }

}
