/**
 * Filename: Constants.java
 * Description: Collection of various constants related to the database and file paths
 * @author Peter Carbone pjc7686
 */

package Constants;

import java.io.File;

public final class Constants {

	// Top-level project folder name
	public static final String TOP_LEVEL_FOLDER = "SWEN-262-Nutriapp-Group5";

	// File paths for databases and folders
	public static final String INGREDIENT_DATABASE_PATH;
    public static final String ACCOUNT_INFO_PATH;
	public static final String FOOD_PATH;

	// File extensions
    public static final String CSV = ".csv";
    public static final String JSON = ".json";
    public static final String XML = ".xml";

	public static final String SEPARATOR = File.separator;
	
	// Define folder paths
	static {

		// Reconstruct the user's path until the top level project folder is reached, 
        // then add the extra information
		String[] directories = System.getProperty("user.dir").split("[/\\\\]");
		String desiredPath = "";
		for(String string: directories) {
			desiredPath += string + SEPARATOR;
			if(string.equals(TOP_LEVEL_FOLDER)) {
				break;
			}
		}

		INGREDIENT_DATABASE_PATH = desiredPath + "Database" + SEPARATOR + "ingredients" + CSV;
		ACCOUNT_INFO_PATH = desiredPath + "Database" + SEPARATOR + "RegisteredAccounts";
		FOOD_PATH = desiredPath + "Database" + SEPARATOR + "Food";
	}

    // Ingredient database column indices
    public static final int NAME_COL_INDEX = 1;
    public static final int CALORIE_INDEX = 3;
    public static final int PROTEIN_INDEX = 4;
    public static final int CARB_INDEX = 7;
    public static final int FIBER_INDEX = 8;
    public static final int SAT_FAT_INDEX = 46;
    public static final int MONO_FAT_INDEX = 47;
    public static final int POLY_FAT_INDEX = 48;
    
    public static final int LOW_STOCK_THRESHOLD = 5;
	public static final String INGREDIENT_NOT_FOUND = "Ingredient not found";

    // Registered user header
    public static final int NUM_USER_FIELDS = 9;
    public static final String USERNAME = "Username";
    public static final String PASSWORD = "Password";
    public static final String HEIGHT = "Height";
    public static final String STARTING_WEIGHT = "Starting Weight";
    public static final String CURRENT_WEIGHT = "Current Weight";
    public static final String DOB = "DoB";
    public static final String GOAL = "Goal";
    public static final String[] REGISTERED_USER_HEADER = {USERNAME, PASSWORD, HEIGHT,
        STARTING_WEIGHT, CURRENT_WEIGHT, DOB, GOAL};
		

	// Ingredient, recipe, meal, and workout headers. Calories, recipe name, and ingredient name are reused

	// Ingredient header
    public static final int NUM_INGREDIENT_FIELDS = 9;
    public static final String INGREDIENT_NAME = "ingredient";
    public static final String STOCK = "stock";
    public static final String CALORIES = "calories";
    public static final String SAT_FAT = "saturatedfat";
    public static final String MONO_UNSAT_FAT = "monounsatruatedfat";
    public static final String POLY_UNSAT_FAT = "polyunsaturatedfat";
    public static final String PROTEIN = "protein";
    public static final String FIBER = "fiber";
    public static final String CARBS = "carbohydrates";

    public static final String[] INGREDIENT_INFO_HEADER = {INGREDIENT_NAME, CALORIES, SAT_FAT, 
        MONO_UNSAT_FAT, POLY_UNSAT_FAT, PROTEIN, FIBER, CARBS, STOCK};

	// Recipe header
	public static final int NUM_RECIPE_FIELDS = 4;
	public static final String RECIPE_NAME = "Recipe";
	public static final String INGREDIENT_AMOUNT = "Ingredientamount";
	public static final String INSTRUCTIONS = "Instructions";
	public static final String[] RECIPE_INFO_HEADER = {RECIPE_NAME, INGREDIENT_NAME, INGREDIENT_AMOUNT, 
		INSTRUCTIONS};

	// Meal header
	public static final int NUM_MEAL_FIELDS = 6;
	public static final String MEAL_NAME = "Meal";
	public static final String RECIPE_AMOUNT = "Recipeamount";
	public static final String[] MEAL_HEADER = {MEAL_NAME, RECIPE_NAME, RECIPE_AMOUNT, INGREDIENT_NAME, 
		INGREDIENT_AMOUNT, INSTRUCTIONS};

	// Workout header
	public static final int NUM_WORKOUT_FIELDS = 5;
	public static final String WORKOUT = "Workout";
	public static final String CALORIES_CONSUMED = "Caloriesconsumed";
	public static final String DAILY_TARGET = "Dailytarget";
	public static final String WEIGHT = "Weight";
	public static final String INTENSITY = "Intensity";
	public static final String[] WORKOUT_HEADER = {WORKOUT, CALORIES_CONSUMED, DAILY_TARGET, 
		WEIGHT, INTENSITY};
}