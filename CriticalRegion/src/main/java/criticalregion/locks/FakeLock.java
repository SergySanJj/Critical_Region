package criticalregion.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class FakeLock implements Lock {
    @Override
    public void lock() { // No action on lock
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
    public boolean tryLock() { // No action on tryLock
        return true;
    }

    @Override
    public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException { // No action on tryLock
        return true;
    }

    @Override
    public void unlock() { // No action on unlock
    }

    @Override
    public Condition newCondition() { // Left empty
        return null;
    }
}
