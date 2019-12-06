package criticalregion.locks;

import criticalregion.locks.exceptions.FixnumLockThreadCountException;
import criticalregion.locks.exceptions.FixnumLockThreadIdException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public abstract class FixNumLockNoReset implements FixNumLock {
    protected int maxThreadsCount;
    protected int count;
    protected ThreadLocal<Integer> localId = ThreadLocal.withInitial(() -> -1);

    private FixNumLockNoReset() {
    }

    public FixNumLockNoReset(int N) {
        maxThreadsCount = N;
    }

    @Override
    public int threadsCount() {
        return count;
    }

    @Override
    public void register() {
        synchronized (this) {
            if (count < maxThreadsCount) {
                localId.set(count);
                count++;
            } else
                throw new FixnumLockThreadCountException("Expected maximum " + maxThreadsCount + " threads but got " + (count + 1));
        }
    }


    @Override
    public void unregister() {
        long currentThreadId = Thread.currentThread().getId();
        synchronized (this) {
            if (localId.get() != -1) {
                localId.set(-1);
                count--;
            } else
                throw new FixnumLockThreadIdException("Can not find thread with id: " + currentThreadId);
        }
    }

    @Override
    public int getId() {
        long currentThreadId = Thread.currentThread().getId();
        if (localId.get() != -1)
            return localId.get();
        else
            throw new FixnumLockThreadIdException("Can not find thread with id: " + currentThreadId);
    }

    public int currentlyRegisteredCount() {
        return count;
    }

    public void reset() {
        count = 0;
    }
}
