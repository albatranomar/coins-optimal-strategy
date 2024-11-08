package algo.pro1.util;

import algo.pro1.App;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundPlayer {
    private static MediaPlayer loadSound(String path) {
        URL soundUrl = App.class.getResource(path);
        if (soundUrl == null) {
            throw new IllegalArgumentException("Sound file not found: " + path);
        }

        Media sound = new Media(soundUrl.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);

        mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose);

        return mediaPlayer;
    }

    public static void gameLoop() {
        MediaPlayer mediaPlayer = loadSound("sounds/game-loop.mp3");

        // Set the media player to loop indefinitely
        mediaPlayer.setCycleCount(3);

        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();
    }

    public static void playClick() {
        loadSound("sounds/click.mp3").play();
    }

    public static void playCoin() {
        loadSound("sounds/coin.mp3").play();
    }

    public static void playStart() {
        loadSound("sounds/game-start.mp3").play();
    }

    public static void playGameOver() {
        loadSound("sounds/game-over.mp3").play();
    }

    public static void playSad() {
        loadSound("sounds/sad.mp3").play();
    }
}
