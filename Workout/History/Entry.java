package Workout.History;

import java.util.Date;

/**
 * Filename: Entry.java
 * Description: Stores one element and a date to be entered as a pair to a history object
 * @author Kai Frazier ksf7880
 */
public class Entry<T> implements Record<T>{
    private T element;
    private Date date;

    /**
     * Creates entry to be used as a field in one of the attribute histories kept track of by a users personal history object
     * 
     * @param element element to be recorded by history object
     * @param date date entered
     */
    public Entry(T element, Date date) {
        this.element = element;
        this.date = date;
    }

    //getters
    public Object getElement(){
        return element;
    }

    public Date getDate(){
        return date;
    }
}
