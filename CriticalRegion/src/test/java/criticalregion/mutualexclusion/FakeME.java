package criticalregion.mutualexclusion;

import criticalregion.locks.FakeLock;
import org.junit.Assert;
import org.junit.Test;

public class FakeME {
    @Test
    public void fakeLockDoesNotProvideME() {
        FakeMETest fakeMETest = new FakeMETest();

        Assert.assertFalse(fakeMETest.providesMutualExclusion(new FakeLock()));
    }
}
