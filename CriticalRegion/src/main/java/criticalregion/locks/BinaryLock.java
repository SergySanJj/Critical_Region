package criticalregion.locks;

public abstract class BinaryLock extends FixNumLockN {
    public BinaryLock() {
        super(2);
    }
}
