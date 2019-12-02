package criticalregion.mutualexclusion;

import criticalregion.locks.DekkerLock;

public class DekkerMETest extends MutualExclusionTest<DekkerLock> {
    public DekkerMETest() {
        super();
    }

    @Prephase
    public void registerCurrentThread() {
        testableLock.register(Thread.currentThread());
    }

    @LockAction
    public void onLock(){
        testableLock.lock();
    }

    @UnlockAction
    public void onUnLock(){
        testableLock.unlock();
    }
}
