package criticalregion.locks;

public abstract class BinaryLock extends FixNumLockNoReset {
    public BinaryLock() {
        super(2);
    }
}
