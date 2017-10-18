import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class REPL {

    private String input = "";
    private MP3Player mp3Player;
    private boolean exit = false;
    private BufferedReader sysin;

    REPL(MP3Player mp3Player) {
        this.mp3Player = mp3Player;
        this.sysin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to MP3test");
        System.out.print("(MP3test) >> ");

        while (!exit) {
            this.read();
        }
    }


    private void read() {
        try {
            this.input = sysin.readLine().toLowerCase();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (input.contentEquals("play")) {
            try {
                if (this.mp3Player.isPaused())
                    this.mp3Player.togglePause();
                this.mp3Player.play();
            } catch (JavaLayerException e) {
                System.err.println(e);
            }
        }
        if (input.contentEquals("stop")) {
            //this.mp3Player.close();
        }
        this.input = "";
    }
}
