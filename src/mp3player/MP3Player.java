package mp3player;

import javazoom.jl.decoder.*;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

import java.io.InputStream;

/**
 * Plays an MP3 file from start to finish. Pausing and stopping should be done through the
 * mp3player.PlayerController class. Each MP3 must be played with a different mp3player.MP3Player. If a song is to
 * be repeated a new mp3player.MP3Player must be created.
 */
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

    /**
     * Plays a song until it is over or is stopped through mp3player.PlayerController. Intended to be run
     * on a different thread than its mp3player.PlayerController.
     *
     * @param state mp3player.PlayerState which is changed through a mp3player.PlayerController object.
     */
    void play(PlayerState state) throws JavaLayerException {
        this.playFrames(state, Integer.MAX_VALUE);
    }

    /**
     * Plays an mp3 until either a number of frames have been played, the mp3 is complete, or it
     * is stopped through its mp3player.PlayerController object.
     *
     * @param state mp3player.PlayerState which is changed through a mp3player.PlayerController object.
     * @param numFrames The number of frames to play.
     */
    private void playFrames(PlayerState state, int numFrames) throws JavaLayerException {
        while (numFrames > 0 && !this.complete) {
            if (state.isStopped()) {
                this.complete = true;
            } else if (state.isPaused()) {
                try {
                    // To keep cycles down.
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // do nothing
                }
            } else {
                this.complete = !this.decodeFrame();
                numFrames--;
            }
        }

        if (this.complete) {
            // ensure all audio is played.
            this.audioDevice.flush();
        }
    }

    /**
     * decodes a single frame from the mp3 file and sends it to the audio device to be played
     *
     * @return Whether data was successfully written to the audio device.
     */
    private boolean decodeFrame() throws JavaLayerException {
        try {
            if (this.audioDevice == null) {
                return false;
            }

            Header header = this.bitstream.readFrame();
            if (header == null) {
                return false;
            }

            // Does not allocate. Passes decoder's internal buffer's data array.
            short[] buffer = ((SampleBuffer) decoder.decodeFrame(header, this.bitstream)).getBuffer();

            this.audioDevice.write(buffer, 0, buffer.length);

            this.bitstream.closeFrame();
        } catch (RuntimeException e) {
            throw new JavaLayerException("Error decoding audio frame: " + e);
        }
        return true;
    }
}
