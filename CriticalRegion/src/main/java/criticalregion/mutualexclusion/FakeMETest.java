package criticalregion.mutualexclusion;

import criticalregion.locks.FakeLock;

public class FakeMETest extends MutualExclusionTest<FakeLock> {
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
