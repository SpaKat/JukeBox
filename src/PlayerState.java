public class PlayerState {
    private boolean paused = true;
    private boolean stopped = false;

    PlayerState() { }

    public void setPaused(boolean b) {
        this.paused = b;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public boolean isStopped() {
        return this.stopped;
    }

    public void stop() {
        this.stopped = true;
    }
}
