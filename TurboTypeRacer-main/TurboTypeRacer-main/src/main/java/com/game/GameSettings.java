package com.game;

import java.io.Serializable;

public class GameSettings implements Serializable {
    public boolean fullScreen;
    public boolean musicSound;
    public boolean soundEfectsSound;

    public GameSettings(boolean fullScreen, boolean musicSound, boolean soundEfectsSound){
        this.fullScreen = fullScreen;
        this.musicSound = musicSound;
        this.soundEfectsSound = soundEfectsSound;
    }

    public GameSettings(GameSettings gameSettings){
        this.fullScreen = gameSettings.fullScreen;
        this.musicSound = gameSettings.musicSound;
        this.soundEfectsSound = gameSettings.soundEfectsSound;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;
    }

    public boolean isMusicSound() {
        return musicSound;
    }

    public void setMusicSound(boolean musicSound) {
        this.musicSound = musicSound;
    }

    public boolean isSoundEfectsSound() {
        return this.soundEfectsSound;
    }

    public  void setSoundEfectsSound(boolean soundEfectsSound) {
        this.soundEfectsSound = soundEfectsSound;
    }
}
