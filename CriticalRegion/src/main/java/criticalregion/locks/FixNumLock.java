package criticalregion.locks;

import java.util.concurrent.locks.Lock;

public interface FixNumLock extends Lock {

    int getId();

    void register(Thread thread);

    void register(long threadId);

    void unregister(Thread thread);

    void unregister(long threadId);

    int threadsCount();
}