package Workout.History.tests;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import Workout.History.History;

public class HistoryTest {
    private History<String> h;

    @Before
    public void setUp(){
        this.h = new History<String>();
    }

    @Test
    public void testGetLastEntryNoEntries(){
        assertEquals(h.getLastEntry(), null);
    }

    @Test
    public void testAdd(){
        assertEquals(h.getLastEntry(), null);
        h.add("deez", new Date());
        assertEquals(h.getLastEntry().getElement(), "deez");
    }

    @Test
    public void testGetLastEntry(){
        h.add("deez", new Date());
        assertEquals(h.getLastEntryElement(), "deez");
    }

    @Test
    public void testRemoveLastEntry(){
        h.add("deez", new Date());
        assertEquals(h.getLastEntryElement(), "deez");
        h.removeLastEntry();
        assertEquals(h.getLastEntry(), null);
    }

    @Test
    public void testRemoveLastEntryNoEntries(){
        try{
            h.removeLastEntry();
        }catch(IndexOutOfBoundsException e){
            fail();
        }
        assertEquals(h.getLastEntry(), null);
    }

    @Test
    public void testToStringNoEntries(){
        assertEquals(h.toString(), "");
    }

    @Test
    public void testToString(){
        Date d = new Date();
        h.add("deez", d);
        assertEquals(h.toString(), d + ": deez\n");
    }
}
