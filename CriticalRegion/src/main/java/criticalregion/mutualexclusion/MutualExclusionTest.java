package criticalregion.mutualexclusion;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class MutualExclusionTest<T extends Lock> {
    protected int WAITING_LIMIT = 1000; // millis
    public static final int TIMES_TO_RUN = 1000;
    public static final int INC_LIMIT = 1000000;
    private int counter;
    protected T testableLock;


    public final boolean providesMutualExclusion(T testableLock) {
        counter = 0;
        this.testableLock = testableLock;

        Thread upcounter = new Thread(() -> {
            runPrephaseActions();
            int ranTimes = 0;
            while (ranTimes < TIMES_TO_RUN) {
                int incTimes = 0;
                testableLock.lock();
                while (incTimes < INC_LIMIT) {
                    counter++;
                    incTimes++;
                }
                ranTimes++;
                testableLock.unlock();
            }
        });

        Thread writeIfEven = new Thread(() -> {
            runPrephaseActions();
            int ranTimes = 0;
            while (ranTimes < TIMES_TO_RUN) {
                int incTimes = 0;
                testableLock.lock();
                while (incTimes < INC_LIMIT) {
                    counter--;
                    incTimes++;
                }
                ranTimes++;
                testableLock.unlock();
            }
        });

        upcounter.start();
        writeIfEven.start();

        try {
            upcounter.join(WAITING_LIMIT);
            writeIfEven.join(WAITING_LIMIT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Logger.getLogger("ME").log(Level.INFO, String.format("%s counted %d, expected %d", this.getClass().getName(), counter, 0));

        if (writeIfEven.isAlive() || upcounter.isAlive()) {
            Logger.getLogger("ME").log(Level.INFO, "Threads not finished");
            return false;
        }


        if (getCount() == 0)
            return true;
        return false;
    }

    protected abstract void runLockActions();
    protected abstract void runUnlockActions();
    protected abstract void runPrephaseActions();


    private int getCount() {
        return counter;
    }

    public boolean checkIfCounterCorrect() {
        return counter == 0;
    }
}
