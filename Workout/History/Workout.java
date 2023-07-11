package Workout.History;
import java.util.Date;

/**
 * Filename: Workout.java
 * Description: Represents a workout of varying intensity and duration, also records date automatically
 * @author Kai Frazier ksf7880
 */
public class Workout {
    private String name;
    private double minutes;
    private double calsPerMinute;
    private Intensity intensity;
    private Date date;

    /**
     * Creates a workout object with the current date
     * 
     * @param name
     * @param minutes
     * @param intensity
     */
    public Workout(String name, double minutes, String intensity){
        this.name = name;
        this.minutes = minutes;
        this.date = new Date();
        set_intensity(intensity);
    }

    /**
     * Converts string parameter to enum
     * 
     * @param intensity string converted to intensity enum (easy, medium, hard)
     */
    public void set_intensity(String intensity){
        switch(intensity.toLowerCase().trim()){
            case "easy":
                this.intensity = Intensity.EASY;
                calsPerMinute = 5;
                break;
            case "medium":
                this.intensity = Intensity.MEDIUM;
                calsPerMinute = 10;
                break;
            case "hard":
                this.intensity = Intensity.HARD;
                calsPerMinute = 7.5;
                break;
            default:
                this.intensity = Intensity.EASY;
                calsPerMinute = 5;
                break;
        }
    }

    /**
     * Gets basic info in string form
     * 
     * @return string of attributes info
     */
    public String getInfo(){
        return "-" + name + "-" +
        "\nDuration in minutes: " + minutes +
        "\nIntensity: " + intensity +
        "\nDate or workout: " + date;
    }

    //getters
    public Date getDate(){
        return date;
    }

	public Intensity getIntensity() {
		return this.intensity;
	}

    public double getCalories(){
        return minutes * calsPerMinute;
    }

    //toString
    @Override
    public String toString(){
        return name;
    }
}
