package Workout.History;

import java.util.Date;

/**
 * Filename: Record.java
 * Description: Interface for history objects and the entries they store
 * Makes it theoretically possible to store histories as elements in another history object
 * @author Kai Frazier ksf7880
 */
public interface Record<T> {
    public Object getElement();

    public Date getDate();
}
