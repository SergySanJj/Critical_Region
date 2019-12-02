package criticalregion.mutualexclusion;

import criticalregion.locks.FakeLock;

public class FakeMETest extends MutualExclusionTest<FakeLock> {
    @Prephase
    public void emptyPrephase() {
    }

    @LockAction
    public void lock() {
        testableLock.lock();
    }

    @UnlockAction
    public void unlock() {
        testableLock.unlock();
    }
}
