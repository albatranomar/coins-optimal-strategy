package coins.game.util;

import coins.game.App;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundPlayer {
    private static Media CLICK, COIN, START, GAMEOVER, SAD;

    public static void init() {
        System.out.println("Loading the sounds...");

        CLICK = loadSound("sounds/click.mp3");
        COIN = loadSound("sounds/coin.mp3");
        START = loadSound("sounds/game-start.mp3");
        GAMEOVER = loadSound("sounds/game-over.mp3");
        SAD = loadSound("sounds/sad.mp3");

        System.out.println("Loaded The sounds.");
    }

    private static Media loadSound(String path) {
        URL soundUrl = App.class.getResource(path);
        if (soundUrl == null) {
            throw new IllegalArgumentException("Sound file not found: " + path);
        }

        return new Media(soundUrl.toString());
    }

    private static void play(Media media) {
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(mediaPlayer::dispose);
        mediaPlayer.play();
    }

    public static void playClick() {
        play(CLICK);
    }

    public static void playCoin() {
        play(COIN);
    }

    public static void playStart() {
        play(START);
    }

    public static void playGameOver() {
        play(GAMEOVER);
    }

    public static void playSad() {
        play(SAD);
    }
}
