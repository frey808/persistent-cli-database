package Workout.History.tests;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Recipes.Meal;
import Workout.History.PersonalHistory;
import Workout.History.Workout;

public class PersonalHistoryTest {
    private PersonalHistory p;

    @Before
    public void setUp(){
        this.p = new PersonalHistory("penjamin");
    }

    @Test
    public void testUpdateWeight(){
        p.updateWeight(69);
        assertEquals(p.getWeights().getLastEntryElement(), 69);
    }

    @Test
    public void testConsumeCalories(){
        p.consumeCalories(420);
        assertEquals(p.getCaloriesConsumed(), 420);
    }

    @Test
    public void testResetCaloriesConsumed(){
        p.consumeCalories(420);
        p.resetCaloriesConsumed();
        assertEquals(p.getCaloriesConsumed(), 0);
    }

    @Test
    public void testUpdateCaloriesTarget(){
        p.updateCaloriesTarget(7177135);
        assertEquals(p.getCaloriesTarget(), 7177135);
    }

    @Test
    public void testUpdateWorkouts(){
        Workout w = new Workout("the sun stare", 99, "easy");
        p.updateWorkouts(w);
        assertEquals(p.getWorkouts().getLastEntryElement(), w);
        assertEquals(p.getCaloriesTarget(), 99*5);
    }

    @Test
    public void testUpdateMeals(){
        Meal m = new Meal("glass");
        p.updateMeals(m);
        assertEquals(p.getMeals().getLastEntryElement(), m);
    }

    @Test
    public void testToString(){
        assertEquals(p.toString(), "-penjamin's Personal History-\nCalories consumed/target: 0/0\nWeight History:\n\nMeal History:\n\nWorkout History:\n");
    }
}