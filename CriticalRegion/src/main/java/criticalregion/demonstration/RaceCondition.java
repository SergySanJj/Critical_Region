package criticalregion.demonstration;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RaceCondition implements Runnable {
    public static final int TIMES_TO_RUN = 1000;
    public static final int INC_LIMIT = 1000000;
    private int counter = 0;

    private Thread upcounter = new Thread(() -> {
        int ranTimes = 0;
        while (ranTimes < TIMES_TO_RUN) {
            int incTimes = 0;
            while (incTimes < INC_LIMIT) {
                counter++;
                incTimes++;
            }
            ranTimes++;
        }
    });

    private Thread longCheck = new Thread(() -> {
        int ranTimes = 0;
        while (ranTimes < TIMES_TO_RUN) {
            int incTimes = 0;
            while (incTimes < INC_LIMIT) {
                counter--;
                incTimes++;
            }
            ranTimes++;
        }
    });

    @Override
    public void run() {
        upcounter.start();
        longCheck.start();
        try {
            upcounter.join();
            longCheck.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Logger.getLogger("Race Condition").log(Level.INFO, String.format("Counter result: %d; Expected %d", counter, 0));
    }

    public int getCounter() {
        return counter;
    }

    public boolean isCounterResultCorrect() {
        return counter == 0;
    }

    public boolean allFinished() {
        return !longCheck.isAlive() && !upcounter.isAlive();
    }
}
