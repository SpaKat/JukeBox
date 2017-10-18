package mp3player;

class PlayerState {
    private boolean paused = true;
    private boolean stopped = false;

    PlayerState() { }

    void setPaused(boolean b) {
        this.paused = b;
    }

    boolean isPaused() {
        return this.paused;
    }

    boolean isStopped() {
        return this.stopped;
    }

    void stop() {
        this.stopped = true;
    }
}
