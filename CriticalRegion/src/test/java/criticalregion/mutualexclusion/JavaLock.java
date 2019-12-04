package criticalregion.mutualexclusion;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class JavaLock {
    @Test
    public void mutualExclusionTest(){
        JavaLockTest javaLockTest = new JavaLockTest();

        Assert.assertTrue(javaLockTest.providesMutualExclusion(new ReentrantLock()));
    }
}