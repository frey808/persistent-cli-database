package Workout.History;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Filename: History.java
 * Description: Keeps track of a list of entries, each entry includes an element and a date
 * Also has options for undoing recent entries, getting recent entries, and viewing full history
 * @author Kai Frazier ksf7880
 */
public class History<T> implements Record<T>{
    private List<Record<T>> history;
    private Date dateCreated;

    /**
     * Creates history object to keep track of an attribute of personal history
     * This method automatically sets dateCreated to the current date instead of accepting it as a parameter
     */
    public History(){
        history = new ArrayList<Record<T>>();
        dateCreated = new Date();
    }

    /**
     * Creates history object to keep track of an attribute of a users personal history
     * 
     * @param dateCreated date initialized, implemented for purposes of consistency between composite and leaf classes
     */
    public History(Date dateCreated){
        history = new ArrayList<Record<T>>();
        this.dateCreated = dateCreated;
    }

    /**
     * Adds an element to the history
     * 
     * @param element element to be added
     * @param date date added
     */
    public void add(T element, Date date){
        history.add(new Entry<T>(element, date));
    }

    /**
     * Remove most recent entry from history
     */
    public void removeLastEntry(){
        if(history.size() > 0){
            history.remove(history.size() - 1);
        }
    }

    /**
     * Gets the most recent entry
     * Implemented for purposes of undoing the addition of the most recent entry
     * 
     * @return a reference to the most recent entry
     */
    public Record<T> getLastEntry(){
        if(history.size() < 1){
            return null;
        }
        return history.get(history.size() - 1);
    }

    //getters
    public Object getLastEntryElement(){
        return getLastEntry().getElement();
    }

    public Date getLastEntryDate(){
        return getLastEntry().getDate();
    }

	public int size() {
		return this.history.size();
	}

    public Object getElement() {
        return history;
    }

    public Date getDate() {
        return dateCreated;
    }

    //toString
    @Override
    public String toString(){
        String returnString = "";
        for (Record<T> entry : history){
            returnString += (entry.getDate() + ": " + entry.getElement() + "\n");
        }
        return returnString;
    }
}