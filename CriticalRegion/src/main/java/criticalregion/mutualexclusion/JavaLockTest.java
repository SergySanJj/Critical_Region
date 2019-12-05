package criticalregion.mutualexclusion;

import criticalregion.locks.DekkerLock;

import java.util.concurrent.locks.Lock;

public class JavaLockTest extends MutualExclusionTest<Lock> {
    public JavaLockTest() {
        super();
    }

    @Override
    public void runPrephaseActions() {

    }

    @Override
    public void runLockActions() {
        testableLock.lock();
    }

    @Override
    public void runUnlockActions() {
        testableLock.unlock();
    }
}
