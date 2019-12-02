package criticalregion.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;

/*
  Before using the shared variables (i.e., before entering its critical region),
  each process calls enter region with its own process number, 0 or 1, as parameter.
  This call will cause it to wait, if need be, until it is safe to enter.
  After it has finished with the shared variables,
  the process calls leave region to indicate that it is done and to allow the other process to enter, if it so desires.
  Let us see how this solution works. Initially neither process is in its critical region. Now process 0 calls enter region.
  It indicates its interest by setting its array element and sets turn to 0.
  Since process 1 is not interested, enter region returns immediately.
  If process 1 now makes a call to enter region, it will hang there until interested[0] goes to FALSE,
  an event that happens only when process 0 calls leave region to exit the critical region.
  Now consider the case that both processes call enter region almost simultaneously.
  Both will store their process number in turn. Whichever store is done last is the one that counts;
  the first one is overwritten and lost. Suppose that process 1 stores last, so turn is 1.
  When both processes come to the while statement, process 0 executes it zero times and enters its critical region.
  Process 1 loops and does not enter its critical region until process 0 exits its critical region.

  Andrew S. Tanenbaum, Herbert Bos, Modern Operating Systems p. 125
 */

public class DekkerLock extends BinaryLock {

    private AtomicInteger turn;
    private AtomicBoolean[] interested = new AtomicBoolean[2];

    public DekkerLock() {
        super();
        for (int i = 0; i < interested.length; i++) {
            interested[i] = new AtomicBoolean();
            interested[i].set(false);
        }
        turn = new AtomicInteger();
    }


    @Override
    public void lock() {
        int id = getId();
        int other = 1 - id;

        interested[id].set(true);

        while (interested[other].get()) {
            if (turn.get() == other) {
                interested[id].set(false);
                while (turn.get() == other) ;
                interested[id].set(true);
            }
        }
    }

    @Override
    public void unlock() {
        int id = getId();
        int other = 1 - id;
        turn.set(other);
        interested[id].set(false);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()){
            Thread.currentThread().interrupt();
        } else {
            lock();
        }
    }

    @Override
    public boolean tryLock() {
        int id = getId();
        int other = 1 - id;

        interested[id].set(true);
        if (!interested[other].get())
            return true;

        return false;
    }

    @Override
    public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException {
        int id = getId();
        int other = 1 - id;

        interested[id].set(true);

        long endTime = System.currentTimeMillis() + l;

        while (interested[other].get() && System.currentTimeMillis() < endTime) {
            if (turn.get() == other) {
                interested[id].set(false);
                while (turn.get() == other) ;
                interested[id].set(true);
            }
        }
        if (System.currentTimeMillis() < endTime)
            return true;
        return false;
    }

    // Left empty
    @Override
    public Condition newCondition() {
        return null;
    }
}
