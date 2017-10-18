import javazoom.jl.decoder.JavaLayerException;
import mp3player.MP3Player;

import java.io.FileInputStream;
import java.io.IOException;

public class mp3test {


    public static void main(String args[]) {
        String mp3Name = "mp3/" + args[0];
        FileInputStream mp3Stream;
        MP3Player mp3Player;
        REPL repl;

        try {
            mp3Stream = new FileInputStream(mp3Name);
            try {
                mp3Player = new MP3Player(mp3Stream);
                //mp3Player.play();
                // TODO make interactive
                repl = new REPL(mp3Player);
            } catch (JavaLayerException e) {
                System.err.println(e);
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
