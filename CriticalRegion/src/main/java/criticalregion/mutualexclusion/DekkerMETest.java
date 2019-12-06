package criticalregion.mutualexclusion;

import criticalregion.locks.DekkerLock;

public class DekkerMETest extends MutualExclusionTest<DekkerLock> {
    public DekkerMETest() {
        super();
    }

    @Override
    public void runPrephaseActions() {
        testableLock.register();
    }

    @Override
    public void runLockActions(){
        testableLock.lock();
    }

    @Override
    public void runUnlockActions(){
        testableLock.unlock();
    }
}
