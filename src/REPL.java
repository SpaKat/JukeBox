import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class REPL {

    private PlayerController playerController;
    private boolean exit = false;
    private BufferedReader sysin;

    public REPL(MP3Player mp3Player) {
        this.sysin = new BufferedReader(new InputStreamReader(System.in));
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
            this.playerController.stop();
            this.exit = true;
        }
    }

    private void printLeader() {
        System.out.print("(MP3test) >> ");
    }
}
