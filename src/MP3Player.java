import javazoom.jl.decoder.*;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MP3Player {

    // NOTE future use
    // private int currentFrame = 0;

    final private Bitstream bitstream;
    final private Decoder decoder;

    private AudioDevice audioDevice;

    // NOTE future use
    // private boolean closed = false;
    private boolean complete = false;
    private boolean paused = true;

    public MP3Player(InputStream in) throws JavaLayerException {
        this.bitstream = new Bitstream(in);
        this.decoder = new Decoder();

        // Sets audio device based on system
        FactoryRegistry r = FactoryRegistry.systemRegistry();
        this.audioDevice = r.createAudioDevice();

        this.audioDevice.open(this.decoder);
    }

    public void play() throws JavaLayerException {
        this.playFrames(Integer.MAX_VALUE);
    }

    public boolean playFrames(int numFrames) throws JavaLayerException {
        while (numFrames > 0 && !this.complete) {
            if (this.paused) {
                // TODO pause
            } else {
                this.complete = !this.decodeFrame();
            }
        }

        if (this.complete) {
            this.audioDevice.flush();
        }

        return true;
    }

    public void togglePause() {
        this.paused = !this.paused;
    }

    public boolean isPaused() {
        return this.paused;
    }

    private boolean decodeFrame() throws JavaLayerException {
        try {
            if (this.audioDevice == null) {
                return false;
            }

            Header header = this.bitstream.readFrame();
            if (header == null) {
                return false;
            }

            short[] buffer = ((SampleBuffer) decoder.decodeFrame(header, this.bitstream)).getBuffer();

            this.audioDevice.write(buffer, 0, buffer.length);

            this.bitstream.closeFrame();
        } catch (RuntimeException e) {
            throw new JavaLayerException("Error decoding audio frame: " + e);
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            MP3Player player = new MP3Player(new FileInputStream("mp3/" + args[0]));


        } catch (JavaLayerException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
