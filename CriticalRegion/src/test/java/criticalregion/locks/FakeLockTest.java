package criticalregion.locks;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class FakeLockTest {
    FakeLock fakeLock;

    @Before
    public void before() {
        fakeLock = new FakeLock();
    }

    @Test
    public void lock() {
        Assert.assertEquals(0, fakeLock.getLockCount());
        Assert.assertEquals(0, fakeLock.getUnlockCount());
        fakeLock.lock();
        Assert.assertEquals(1, fakeLock.getLockCount());
        Assert.assertEquals(0, fakeLock.getUnlockCount());
    }

    @Test
    public void lockInterruptibly() {
        Thread th = new Thread(() -> {
            try {
                fakeLock.lockInterruptibly();
                Thread.currentThread().interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tryLock() {
        Assert.assertEquals(0, fakeLock.getLockCount());
        Assert.assertEquals(0, fakeLock.getUnlockCount());
        boolean res = fakeLock.tryLock();
        Assert.assertTrue(res);
        Assert.assertEquals(1, fakeLock.getLockCount());
        Assert.assertEquals(0, fakeLock.getUnlockCount());
    }

    @Test
    public void testTryLock() {
        Assert.assertEquals(0, fakeLock.getLockCount());
        Assert.assertEquals(0, fakeLock.getUnlockCount());
        boolean res = false;
        try {
            res = fakeLock.tryLock(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(res);
        Assert.assertEquals(1, fakeLock.getLockCount());
        Assert.assertEquals(0, fakeLock.getUnlockCount());
    }

    @Test
    public void unlock() {
        Assert.assertEquals(0, fakeLock.getLockCount());
        Assert.assertEquals(0, fakeLock.getUnlockCount());
        fakeLock.unlock();
        Assert.assertEquals(0, fakeLock.getLockCount());
        Assert.assertEquals(1, fakeLock.getUnlockCount());
    }
}