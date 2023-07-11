package Goals;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;


public class GoalsTest {

    @Test
    public void testCreateGoal(){
        Goals goal1 = new Goals("maintain",135);
        assertNotNull("goal evaluated as null", goal1);
    }

    @Test
    public void testGetCurrentWeight(){
        Goals goal1 = new Goals("maintain",135);
        assertEquals(goal1.getCurrentWeight(),135);
    }

    @Test
    public void testGetTargetWeight(){
        Goals goal1 = new Goals("maintain",135);
        assertEquals(goal1.getTargetWeight(),135);
    }

    
    @Test
    public void testGoalMet(){
        Goals goal1 = new Goals("maintain",135);
        
        boolean expected = true;
        boolean actual =  (goal1.getTargetWeight() == goal1.getCurrentWeight());
        
        assertEquals(expected,actual);
    }




















}

