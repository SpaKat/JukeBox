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

    public MP3Player(InputStream in) throws JavaLayerException {
        this.bitstream = new Bitstream(in);
        this.decoder = new Decoder();

        // Sets audio device based on system
        FactoryRegistry r = FactoryRegistry.systemRegistry();
        this.audioDevice = r.createAudioDevice();

        this.audioDevice.open(this.decoder);
    }

    public boolean playFrames(int numFrames) throws JavaLayerException {
        while (numFrames-- > 0 && !this.complete) {
            this.complete = !this.decodeFrame();
        }

        this.audioDevice.flush();

        return true;
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

            SampleBuffer out = (SampleBuffer) this.decoder.decodeFrame(header, this.bitstream);

            this.audioDevice.write(out.getBuffer(), 0, out.getBufferLength());

            this.bitstream.closeFrame();
        } catch (RuntimeException e) {
            throw new JavaLayerException("Error decoding audio frame: ", e);
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            MP3Player player = new MP3Player(new FileInputStream("mp3/" + args[0]));
            int counter = 0;
            while (true) {
                player.playFrames(100);
                counter += 100;
            }
        } catch (JavaLayerException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
