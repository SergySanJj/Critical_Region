package criticalregion.locks;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import static org.junit.Assert.*;

public class FixNumLockNTest {
    private FixNumLockN fixNumLockN;

    @Before
    public void initialize() {
        fixNumLockN = new FixNumLockN(2) {
            @Override
            public void lock() {
            }

            @Override
            public void lockInterruptibly() throws InterruptedException {
            }

            @Override
            public boolean tryLock() {
                return false;
            }

            @Override
            public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException {
                return false;
            }

            @Override
            public void unlock() {

            }

            @Override
            public Condition newCondition() {
                return null;
            }
        };
    }

    public class TestThread extends Thread {
        private boolean expectFail;

        public TestThread(Runnable r, boolean expectFail) {
            super(r);
            this.expectFail = expectFail;
        }

        @Override
        public void run() {
            try {
                super.run();
                if (expectFail)
                    Assert.assertTrue(false);
            } catch (Exception e) {
                if (!expectFail)
                    Assert.assertTrue(false);
            }
        }
    }

    @Test
    public void creation() {
        TestThread threadA = new TestThread(() -> fixNumLockN.register(), false);
        TestThread threadB = new TestThread(() -> fixNumLockN.register(), false);
        threadA.start();
        threadB.start();
        try {
            threadA.join();
            threadB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(2, fixNumLockN.currentlyRegisteredCount());
    }

    @Test
    public void insertNotInsertable() {
        TestThread threadA = new TestThread(() -> fixNumLockN.register(), false);
        TestThread threadB = new TestThread(() -> fixNumLockN.register(), false);
        TestThread threadC = new TestThread(() -> fixNumLockN.register(), true);
        threadA.start();
        threadB.start();
        threadC.start();
        try {
            threadA.join();
            threadB.join();
            threadC.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteNotDeletable() {
        TestThread threadA = new TestThread(() -> fixNumLockN.register(), false);
        TestThread threadB = new TestThread(() -> fixNumLockN.register(), false);
        TestThread threadC = new TestThread(() -> fixNumLockN.unregister(), true);
        threadA.start();
        threadB.start();
        threadC.start();
        try {
            threadA.join();
            threadB.join();
            threadC.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertTwice() {
        TestThread threadA = new TestThread(() -> fixNumLockN.register(), false);
        TestThread threadB = new TestThread(() -> {
            fixNumLockN.register();
            fixNumLockN.register();
        }, false);
        threadA.start();
        threadB.start();
        try {
            threadA.join();
            threadB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(2, fixNumLockN.currentlyRegisteredCount());
    }

    @Test
    public void insertDelete() {
        TestThread threadA = new TestThread(() -> fixNumLockN.register(), false);
        TestThread threadB = new TestThread(() -> {
            fixNumLockN.register();
            fixNumLockN.unregister();
        }, false);
        threadA.start();
        threadB.start();
        try {
            threadA.join();
            threadB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(1, fixNumLockN.currentlyRegisteredCount());
    }

    @Test
    public void insertDeleteTwice() {
        TestThread threadA = new TestThread(() -> fixNumLockN.register(), false);
        TestThread threadB = new TestThread(() -> {
            fixNumLockN.register();
            fixNumLockN.unregister();
            fixNumLockN.unregister();
        }, true);
        threadA.start();
        threadB.start();
        try {
            threadA.join();
            threadB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(1, fixNumLockN.currentlyRegisteredCount());
    }

    @Test
    public void getId() {
        TestThread threadA = new TestThread(() -> fixNumLockN.register(), false);
        TestThread threadB = new TestThread(() -> {
            fixNumLockN.register();
            fixNumLockN.getId();
        }, false);
        TestThread threadC = new TestThread(() -> fixNumLockN.getId(), true);
        threadA.start();
        threadB.start();
        threadC.start();
        try {
            threadA.join();
            threadB.join();
            threadC.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void reset() {
        TestThread threadA = new TestThread(() -> fixNumLockN.register(), false);
        TestThread threadB = new TestThread(() -> fixNumLockN.register(), false);
        threadA.start();
        threadB.start();
        try {
            threadA.join();
            threadB.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fixNumLockN.reset();
        Assert.assertEquals(0, fixNumLockN.currentlyRegisteredCount());
    }
}