package criticalregion.locks;

import java.util.concurrent.locks.Lock;

public interface FixNumLock extends Lock {

    int getId();

    void register();

    void unregister();

    int threadsCount();
}