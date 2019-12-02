package criticalregion.locks;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

import static org.junit.Assert.*;

public class FixNumLockNTest {
    @Test
    public void creationTest(){
        List<Thread> threads = new ArrayList<>();
        threads.add(new Thread());
        threads.add(new Thread());
        threads.add(new Thread());
        int threadsCount = threads.size();

        // Empty threads.size()-1 num lock
        FixNumLockN fixNumLockN = new FixNumLockN(threadsCount-1) {
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

        Assert.assertEquals(0,fixNumLockN.currentlyRegisteredCount());
        fixNumLockN.register(threads.get(0));
        Assert.assertEquals(1,fixNumLockN.currentlyRegisteredCount());
        fixNumLockN.register(threads.get(1));
        Assert.assertEquals(2,fixNumLockN.currentlyRegisteredCount());
        try{
            fixNumLockN.register(threads.get(2));
            Assert.assertTrue(false);
        } catch (Exception e){
            Assert.assertEquals(2,fixNumLockN.currentlyRegisteredCount());
        }
        try{
            fixNumLockN.unregister(threads.get(2));
            Assert.assertTrue(false);
        } catch (Exception e){
            Assert.assertEquals(2,fixNumLockN.currentlyRegisteredCount());
        }
        fixNumLockN.unregister(threads.get(1));
        Assert.assertEquals(1,fixNumLockN.currentlyRegisteredCount());
        fixNumLockN.register(threads.get(2));
        Assert.assertEquals(2,fixNumLockN.currentlyRegisteredCount());
        try{
            fixNumLockN.getId();
            Assert.assertTrue(false);
        } catch (Exception e){
            Assert.assertEquals(2,fixNumLockN.currentlyRegisteredCount());
        }

        fixNumLockN.reset();
        Assert.assertEquals(0,fixNumLockN.currentlyRegisteredCount());
    }
}