package criticalregion.demonstration;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class RaceConditionTest {
    @Test
    public void raceCondition() {
        RaceCondition raceCondition = new RaceCondition();
        raceCondition.run();
        Assert.assertTrue(raceCondition.allFinished());
        Assert.assertTrue(raceCondition.countWrong() > 0);
    }
}