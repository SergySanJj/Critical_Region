package criticalregion.mutualexclusion;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@interface LockAction {
    boolean enabled() default true;
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@interface UnlockAction {
    boolean enabled() default true;
}

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@interface Prephase {
    boolean enabled() default true;
}

public abstract class MutualExclusionTest<T extends Lock> {
    protected int INC_LIMIT = 1000;
    protected int LONG_CHECK_LIMIT = 10;
    private int counter;
    private int wrongCount;
    protected T testableLock;

    private List<Method> LockMethods;
    private List<Method> UnlockMethods;
    private List<Method> PrephaseMethods;

    public final boolean providesMutualExclusion(T testableLock) {
        initAnnotated();
        counter = 0;
        wrongCount = 0;
        this.testableLock = testableLock;

        Thread upcounter = new Thread(() -> {
            int ranTimes = 0;
            runPrephaseActions();
            while (ranTimes < INC_LIMIT) {
                runLockActions();
                try {
                    Thread.currentThread().sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter++;
                runUnlockActions();
                ranTimes++;
            }
        });

        Thread writeIfEven = new Thread(() -> {
            int ranTimes = 0;
            runPrephaseActions();
            while (ranTimes < LONG_CHECK_LIMIT) {
                runLockActions();
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
                runUnlockActions();
                ranTimes++;
            }
        });

        upcounter.start();
        writeIfEven.start();

        try {
            upcounter.join();
            writeIfEven.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (writeIfEven.isAlive() || upcounter.isAlive()) {
            Logger.getLogger("ME").log(Level.INFO, "Threads not finished");
            return false;
        }

        Logger.getLogger("ME").log(Level.INFO, String.format("%s counted wrong %d", this.getClass().getName(), wrongCount));
        if (countOdd() == 0)
            return true;
        return false;
    }

    private void runLockActions() {
        runMethods(LockMethods);
    }

    private void runUnlockActions() {
        runMethods(UnlockMethods);
    }

    private void runPrephaseActions() {
        runMethods(PrephaseMethods);
    }

    private void runMethods(List<Method> methods) {
        for (Method method : methods) {
            try {
                method.invoke(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initAnnotated() {
        LockMethods = new ArrayList<>();
        UnlockMethods = new ArrayList<>();
        PrephaseMethods = new ArrayList<>();

        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(LockAction.class)) {
                LockMethods.add(method);
            }
            if (method.isAnnotationPresent(UnlockAction.class)) {
                UnlockMethods.add(method);
            }
            if (method.isAnnotationPresent(Prephase.class)) {
                PrephaseMethods.add(method);
            }
        }

    }

    private int countOdd() {
        return wrongCount;
    }
}
