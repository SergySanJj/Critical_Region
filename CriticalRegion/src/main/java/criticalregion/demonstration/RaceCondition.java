package criticalregion.demonstration;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RaceCondition implements Runnable {
    public static final int INC_LIMIT = 1000;
    public static final int LONG_CHECK_LIMIT = 10;
    private int counter = 0;
    private int wrongCount = 0;

    private Thread upcounter = new Thread(() -> {
        int ranTimes = 0;
        while (ranTimes < INC_LIMIT) {
            try {
                Thread.currentThread().sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // critical region start
            counter++;
            // critical region end
            ranTimes++;
        }
    });

    private Thread longCheck = new Thread(() -> {
        int ranTimes = 0;
        while (ranTimes < LONG_CHECK_LIMIT) {
            // critical region start
            int counterCopy = counter;
            try {
                Thread.currentThread().sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (counterCopy != counter)
            {
                wrongCount++;
            }
            // critical region end
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
        Logger.getLogger("Race Condition").log(Level.INFO, String.format("Wrong count %d", wrongCount));
    }

    public int countWrong() {
        return wrongCount;
    }

    public boolean allFinished() {
        return !longCheck.isAlive() && !upcounter.isAlive();
    }
}
