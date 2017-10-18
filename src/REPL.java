import mp3player.MP3Player;
import mp3player.PlayerController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A command line interface to the MP3 player.
 */
public class REPL {

    // interactions with the MP3 player should be through this.
    private PlayerController playerController;
    private boolean exit = false;
    private BufferedReader sysin;

    /**
     * Creates and runs the REPL for the mp3 to be played.
     *
     * TODO overload without a mp3player.MP3Player, and make not depend on one.
     *
     * @param mp3Player The player for the mp3 to be played first.
     */
    public REPL(MP3Player mp3Player) {
        this.sysin = new BufferedReader(new InputStreamReader(System.in));
        // TODO there should be a controller for these controllers to ensure only one playes at
        // a time. Will be very important when songs are being queued.
        this.playerController = new PlayerController(mp3Player);
        System.out.println("Welcome to MP3test");

        while (!exit) {
            this.read();
        }
    }

    private void read() {
        String input;
        printLeader();
        try {
            input = sysin.readLine().toLowerCase();
        } catch (IOException e) {
            System.err.println("Error reading stdin.");
            e.printStackTrace();
            return;
        }
        if (input.contentEquals("play")) {
            this.playerController.setPaused(false);
        } else if (input.contentEquals("stop")) {
            this.playerController.stop();
        } else if (input.contentEquals("pause")) {
            this.playerController.setPaused(true);
        } else if (input.contentEquals("exit")) {
            // if not stopped the audio thread won't return until the song is over.
            this.playerController.stop();
            this.exit = true;
        }
    }

    private void printLeader() {
        System.out.print("(MP3test) >> ");
    }
}
