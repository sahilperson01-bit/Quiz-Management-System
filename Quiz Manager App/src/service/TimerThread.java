package service;

public class TimerThread extends Thread {
    private int seconds;
    private volatile boolean running = true;
    private final TimerListener listener;

    public interface TimerListener {
        void onTick(int secondsLeft);
        void onFinish();
    }

    public TimerThread(int seconds, TimerListener listener) {
        this.seconds = seconds;
        this.listener = listener;
    }

    @Override
    public void run() {
        while (running && seconds > 0) {
            try {
                Thread.sleep(1000);
                seconds--;
                if (listener != null) listener.onTick(seconds);
            } catch (InterruptedException e) {
                running = false;
                break;
            }
        }
        if (running && seconds <= 0 && listener != null) listener.onFinish();
    }

    public void stopTimer() {
        running = false;
        interrupt();
    }
}
