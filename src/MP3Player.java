import javazoom.jl.decoder.*;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

import java.io.InputStream;

public class MP3Player {

    final private Bitstream bitstream;
    final private Decoder decoder;

    private AudioDevice audioDevice;

    private boolean complete = false;

    public MP3Player(InputStream in) throws JavaLayerException {
        this.bitstream = new Bitstream(in);
        this.decoder = new Decoder();

        // Sets audio device based on system
        FactoryRegistry r = FactoryRegistry.systemRegistry();
        this.audioDevice = r.createAudioDevice();

        this.audioDevice.open(this.decoder);
    }

    public void play(PlayerState state) throws JavaLayerException {
        this.playFrames(state, Integer.MAX_VALUE);
    }

    public void playFrames(PlayerState state, int numFrames) throws JavaLayerException {
        while (numFrames > 0 && !this.complete) {
            if (state.isStopped()) {
                this.complete = true;
            } else if (state.isPaused()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }
            } else {
                this.complete = !this.decodeFrame();
                numFrames--;
            }
        }

        if (this.complete) {
            this.audioDevice.flush();
        }
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
}
