package Goals;

import Workout.History.PersonalHistory;

/**
 * Filename: Goals.java
 * Description: Gives the user the ability to create a goal by 
 *  adding a goal_type, target calories, target weight and their current weight.
 * @author Tyler Kranzen tok4416
 * @author Kai Frazier ksf7880
 */
public class Goals {
    private int current_weight;
    private GoalTypes goal_type;
    private int target_weight;

    public Goals(String goal_type, int current_weight){
        this.current_weight = current_weight;
        setGoalType(goal_type);
    }

    private void setGoalType(String goal){
        switch(goal.toLowerCase().trim()){
            case "maintain":
                goal_type = GoalTypes.Maintain_Weight;
                target_weight = current_weight;
                break;
            case "lose":
                goal_type = GoalTypes.Lose_Weight;
                target_weight = current_weight-10;
                break;
            case "gain":
                goal_type = GoalTypes.Gain_Weight;
                target_weight = current_weight+10;
                break;
            case "[]":
                goal_type = GoalTypes.Maintain_Weight;
        }
    }
    
    /** 
     * Adding a goal to the user
     * @param goal_type
     * @param target_weight
     * @param current_weight
     * @return Goals
     */
    public Goals addGoal(String goal_type, int current_weight){
        if(Math.abs(current_weight) <= 5){
            Goals goal = new Goals(goal_type, current_weight);
            return goal;
        }
        Goals goal = new Goals(goal_type, current_weight);
        return goal;
    }

    /** 
     * Getter to get current weight of goal 
     * @return int
     */
    public int getCurrentWeight(){
        return this.current_weight;
    }

     /** 
     * Getter to get target weight of goal 
     * @return int
     */
    public int getTargetWeight(){
        return this.target_weight;
    }
     /** 
     * Getter to get Goal Type of goal
     * @return GoalTypes
     */
    public GoalTypes getGoalType(){
        return this.goal_type;
    }

    /** 
     * Return list of types of goals
     * @return String of goals
     */
    public static String listGoals(){
        return "Maintain_Weight (maintain), Gain_Weight (gain), Lose_Weight (lose)";
    }
    
    /** 
     * Automatically changes goal based on current Weight and target weight
     * @param goal
     */
    public void updateGoals(PersonalHistory history){
        if(getTargetWeight() == getCurrentWeight() && (this.goal_type == GoalTypes.Gain_Weight || this.goal_type == GoalTypes.Lose_Weight)){           
           this.goal_type = GoalTypes.Maintain_Weight;
           history.updateCaloriesTarget(2500);
        }
        else if(getTargetWeight() < getCurrentWeight()){
            this.goal_type = GoalTypes.Lose_Weight;
            history.updateCaloriesTarget(2000);
        }
        else if(getTargetWeight() > getCurrentWeight()){
            this.goal_type = GoalTypes.Gain_Weight;
            history.updateCaloriesTarget(3000);
        }
    }
}