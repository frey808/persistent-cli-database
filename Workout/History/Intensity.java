package Workout.History;

/**
 * Filename: Intensity.java
 * Description: Object representing the intensity of a workout, can either be easy, medium or hard
 * @author Kai Frazier ksf7880
 */
public enum Intensity {
    EASY("easy"),
    MEDIUM("medium"),
    HARD("hard");

    private String name;

    /**
     * Creates intensity enum for workouts
     * 
     * @param name level of intensity (string) easy, medium, hard
     */
    private Intensity(String name){
        this.name = name;
    }

    //toString
    @Override
    public String toString(){
        return name;
    }
}
