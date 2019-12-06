package criticalregion.locks;

import criticalregion.locks.exceptions.FixnumLockThreadCountException;
import criticalregion.locks.exceptions.FixnumLockThreadIdException;

import java.util.ArrayList;
import java.util.List;


public abstract class FixNumLockN implements FixNumLock {
    protected int maxThreadsCount;
    protected int count;
    protected List<Long> threads;
    protected ThreadLocal<Integer> localId = ThreadLocal.withInitial(() -> -1);

    private FixNumLockN() {
    }

    public FixNumLockN(int N) {
        maxThreadsCount = N;
        threads = new ArrayList<>();
    }

    @Override
    public int threadsCount() {
        return count;
    }

    @Override
    public void register() {
        long currentThreadId = Thread.currentThread().getId();
        synchronized (this) {
            if (localId.get() == -1) {
                if (count < maxThreadsCount) {
                    localId.set(count);
                    count++;
                    boolean done = false;
                    for (int i = 0; i < threads.size() && !done; ++i) {
                        if (threads.get(i) == -1) {
                            localId.set(i);
                            threads.set(i, currentThreadId);
                            done = true;
                        }
                    }
                    if (!done) {
                        localId.set(threads.size());
                        threads.add(currentThreadId);
                    }
                } else
                    throw new FixnumLockThreadCountException("Expected maximum " + maxThreadsCount + " threads but got " + (count + 1));
            }
        }
    }


    @Override
    public void unregister() {
        long currentThreadId = Thread.currentThread().getId();
        synchronized (this) {
            if (localId.get() != -1 && threads.get(localId.get()) == currentThreadId) {
                threads.set(localId.get(), -1L);
                localId.set(-1);
                count--;
            } else {
                throw new FixnumLockThreadIdException("Can not find thread with id: " + currentThreadId);
            }
        }
    }

    @Override
    public int getId() {
        long currentThreadId = Thread.currentThread().getId();
        if (localId.get() != -1 && threads.get(localId.get()) == currentThreadId)
            return localId.get();
        else
            throw new FixnumLockThreadIdException("Can not find thread with id: " + currentThreadId);
    }

    public int currentlyRegisteredCount() {
        return count;
    }

    public void reset() {
        synchronized (this) {
            count = 0;
            for (int i = 0; i < threads.size(); ++i) {
                threads.set(i, -1L);
            }
        }
    }
}