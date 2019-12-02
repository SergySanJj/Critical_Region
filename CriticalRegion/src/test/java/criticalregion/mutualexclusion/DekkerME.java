package criticalregion.mutualexclusion;

import criticalregion.locks.DekkerLock;
import org.junit.Assert;
import org.junit.Test;

public class DekkerME {
    @Test
    public void mutualExclusionTest(){
        DekkerMETest dekkerMETest = new DekkerMETest();

        Assert.assertTrue(dekkerMETest.providesMutualExclusion(new DekkerLock()));
    }
}