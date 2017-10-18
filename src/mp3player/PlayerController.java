package mp3player;

import javazoom.jl.decoder.JavaLayerException;

/**
 * Controls an MP3Player object.
 */
public class PlayerController {
    // used to signal the player to pause or stop
    private PlayerState playerState;

    /**
     * Creates a new player controller for an MP3Player object.
     *
     * @param player The MP3Player object to be controlled.
     */
    public PlayerController(MP3Player player) {
        this.playerState = new PlayerState();

        Thread playerThread = new Thread(() -> {
            try {
                player.play(playerState);
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        });

        playerThread.start();
    }

    /**
     * When set to false, sound data is sent to the audio device.
     *
     * @param b pause flag
     */
    public void setPaused(boolean b) {
        this.playerState.setPaused(b);
    }

    /**
     * Stops the mp3 player. The mp3 player won't be able to play any more, so to reset the song
     * a new one must be created.
     * TODO allow reset
     */
    public void stop() {
        this.playerState.stop();
    }

}
