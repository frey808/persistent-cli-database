package Workout.History.tests;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import Workout.History.Workout;

public class WorkoutTest {
    private Workout w;
    private Date d;

    @Before
    public void setUp(){
        this.w = new Workout("the sun stare", 99, "easy");
        this.d = new Date();
    }

    @Test
    public void testGetInfo(){
        assertEquals(w.getInfo(), "-the sun stare-\nDuration in minutes: 99\nIntensity: easy\nDate or workout: " + d);
    }

    @Test
    public void testGetDate(){
        assertEquals(w.getDate(), d);
    }

    @Test
    public void testToString(){
        assertEquals(w.toString(), "the sun stare");
    }
}
