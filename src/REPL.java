import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class REPL {

    private String input = "";
    private Player mp3Player;
    private boolean exit = false;

    REPL(Player mp3Player) {
        this.mp3Player = mp3Player;
        System.out.println("Welcome to MP3test");
        System.out.print("(MP3test) >> ");

        while (!exit) {
            this.read();
        }
    }


    private void read() {
        this.input = input.toLowerCase();
        if (input.contentEquals("play")) {
            try {
                this.mp3Player.play();
            } catch (JavaLayerException e) {
                System.err.println(e);
            }
        }
        if (input.contentEquals("stop")) {
            this.mp3Player.close();
        }
        this.input = "";
    }
}
