import javazoom.jl.decoder.JavaLayerException;

public class PlayerController {
    private PlayerState playerState;

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

    public void setPaused(boolean b) {
        this.playerState.setPaused(b);
    }

    public void stop() {
        this.playerState.stop();
    }

}
