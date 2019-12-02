package criticalregion.locks;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class FakeLock implements Lock {
    private int lockCount = 0;
    private int unlockCount = 0;
    @Override
    public void lock() { // No action on lock
        lockCount++;
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
        lock();
        return true;
    }

    @Override
    public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException {
        lock();
        return true;
    }

    @Override
    public void unlock() { // No action on unlock
        unlockCount++;
    }

    @Override
    public Condition newCondition() { // Left empty
        return null;
    }

    public int getLockCount() {
        return lockCount;
    }

    public int getUnlockCount() {
        return unlockCount;
    }
}
