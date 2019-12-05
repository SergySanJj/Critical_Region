package criticalregion.locks;

import criticalregion.locks.exceptions.FixnumLockThreadCountException;
import criticalregion.locks.exceptions.FixnumLockThreadIdException;

import java.util.ArrayList;
import java.util.List;

public abstract class FixNumLockN implements FixNumLock {
    protected int maxThreadsCount;
    protected List<Long> threads;

    protected final Object operationMutex = new Object();

    private FixNumLockN() {
    }

    public FixNumLockN(int N) {
        maxThreadsCount = N;
        threads = new ArrayList<>();
    }

    @Override
    public int threadsCount() {
        return threads.size();
    }

    @Override
    public void register(Thread thread) {
        register(thread.getId());
    }

    @Override
    public void register(long threadId) {
        synchronized (operationMutex) {
            if (threads.size() + 1 > maxThreadsCount)
                throw new FixnumLockThreadCountException("Expected maximum " + maxThreadsCount + " threads but got " + (threads.size() + 1));

            threads.add(threadId);
        }
    }

    @Override
    public void unregister(Thread thread) {
        unregister(thread.getId());
    }

    @Override
    public void unregister(long threadId) {
        Long pendingToBeUnregistered = null;
        synchronized (operationMutex) {
            for (long thID : threads) {
                if (thID == threadId) {
                    pendingToBeUnregistered = thID;
                    break;
                }
            }
            if (pendingToBeUnregistered != null)
                threads.remove(pendingToBeUnregistered);
            else
                throw new FixnumLockThreadIdException("Can not find thread with id: " + threadId + " in " + threads.toString());
        }
    }

    @Override
    public int getId() {
        long currentThreadId = Thread.currentThread().getId();
        for (int i = 0; i < threads.size(); i++) {
            if (threads.get(i) == currentThreadId) {
                return i;
            }
        }
        throw new FixnumLockThreadIdException("Can not find thread with id: " + currentThreadId + " in " + threads.toString());
    }

    public int currentlyRegisteredCount() {
        return threads.size();
    }

    public void reset() {
        synchronized (operationMutex) {
            threads.clear();
        }
    }
}
