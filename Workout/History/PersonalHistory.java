package Workout.History;

import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.List;

import Constants.Constants;
import ShoppingList.ShoppingList;
import Database.Processing.Reading.CSVReader;
import Database.Processing.Reading.ReadContext;
import Database.Processing.Reading.ReadStrategy;
import Database.Processing.Writing.CSVWriter;
import Database.Processing.Writing.WriteContext;
import Database.Processing.Writing.WriteStrategy;
import Recipes.Meal;
import Recipes.Recipe;
import Recipes.Food;
import Recipes.Ingredient;

/**
 * Filename: PersonalHistory.java
 * Description: Records users weight history, meal history and workout history, and keeps track of
 * progress towards target calories throughout the day
 * @author Kai Frazier ksf7880
 */
public class PersonalHistory{
    private String user;
    private History<Integer> weights;
    private double caloriesConsumed;
    private double caloriesTarget;
    private History<Meal> meals;
	private History<Recipe> recipes;
    private History<Workout> workouts;
	private ShoppingList shoppingList;

    /**
     * Creates a personal history object for a single user
     * 
     * @param user name of user who's history will be recorded
     */
    public PersonalHistory(String user){
        this.user = user;
        this.weights = new History<>();
        this.caloriesConsumed = 0;
        this.caloriesTarget = 0;
        this.meals = new History<>();
		this.recipes = new History<>();
        this.workouts = new History<>();
		this.shoppingList = new ShoppingList();
    }

    //getter
    public History<Integer> getWeights(){
        return this.weights;
    }

	public History<Recipe> getRecipes() {
		return this.recipes;
	}

    /**
     * Adds entry to weight history
     * 
     * @param weight weight to be recorded
     */
    public void updateWeight(int weight){
        weights.add(weight, new Date());
    }

    /**
     * Gets info on percentage of target calories that have been consumed
     * 
     * @return consumed percentage of target calories
     */
    public String getCaloriesConsumedVsTarget(){
        return "" + (caloriesConsumed/caloriesTarget)*100;
    }

    /**
     * Checks if calories target has been met
     * 
     * @return true if consumed calories is greater than target calories, otherwise false
     */
    public boolean checkCaloriesTargetMet(){
        if(caloriesConsumed >= caloriesTarget){
            return true;
        }else{
            return false;
        }
    }

    //getter
    public double getCaloriesConsumed(){
        return caloriesConsumed;
    }

    /**
     * Resets calories consumed to zero
     */
    public void resetCaloriesConsumed(){
        caloriesConsumed = 0;
    }

    /**
     * Adds calories to consumed calories
     * 
     * @param calories amount of calories consumed
     */
    public Workout consumeCalories(double calories){
        caloriesConsumed += calories;
        double excessCalories = caloriesConsumed - caloriesTarget;
        if(excessCalories > 0){
            return new Workout("recommended workout", excessCalories/5, "easy");
        }else{
            return null;
        }
    }

    //getter
    public double getCaloriesTarget(){
        return caloriesTarget;
    }

    /**
     * Updates target calories
     * 
     * @param calories new target calories
     */
    public void updateCaloriesTarget(double calories){
        caloriesTarget = calories;
    }

    //getters
    public History<Meal> getMeals(){
        return meals;
    }

    /**
     * Adds new entry to meal history
     * 
     * @param meal meal to be entered to records
     */
    public void updateMeals(Meal meal){
        meals.add(meal, new Date());
		writeMealInfo();
    }

	public void updateRecipes(Recipe recipe) {
		recipes.add(recipe, new Date());
		writeRecipeInfo();
	}

    //getter
    public History<Workout> getWorkouts(){
        return workouts;
    }

	public Recipe getRecipe(String name) {
		List<Record<Recipe>> recipes = (ArrayList<Record<Recipe>>) this.recipes.getElement();
		for(int i = 0; i < recipes.size(); i++) {
			Record<Recipe> recipeRecord = recipes.get(i);
			Recipe recipe = (Recipe) recipeRecord.getElement();
			if(recipe.getName().equals(name)) {
				return recipe;
			}
		}
		return null;
	}

	public Meal getMeal(String name) {
		List<Record<Meal>> meals = (ArrayList<Record<Meal>>) this.meals.getElement();
		for(int i = 0; i < meals.size(); i++) {
			Record<Meal> mealRecord = meals.get(i);
			Meal meal = (Meal) mealRecord.getElement();
			if(meal.getName().equals(name)) {
				return meal;
			}
		}
		return null;
	}
	
	public ShoppingList getShoppingList() {
		return this.shoppingList;
	}

    /**
     * Adds new entry to workout history
     * Adds calories burned in the workout to the calorie target
     * 
     * @param workout workout to be entered into records
     */
    public void updateWorkouts(Workout workout){
        workouts.add(workout, workout.getDate());
		writeWorkoutInfo();
    }

    /**
     * Adds ingredient to shopping list
     * 
     * @param name name of food
     * @param stock amount of item in stock
     */
	public void addIngredient(String name, int stock) {
		this.shoppingList.addIngredient(name, stock);
	}

	/**
	 * Writes the user's workout info to their file after an update
	 */
	private void writeWorkoutInfo() {
		List<Record<Workout>> workouts = (ArrayList<Record<Workout>>) this.recipes.getElement();
		List<String[]> workoutData = new ArrayList<>();
		String workoutFile = Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + user + 
			Constants.SEPARATOR + "workouts" + Constants.CSV;

		workoutData.add(Constants.WORKOUT_HEADER);

		for(Record<Workout> record: workouts) {
			Workout workout = (Workout) record.getElement();

			String[] workoutRow = new String[Constants.NUM_WORKOUT_FIELDS];
			workoutRow[0] = workout.toString();
			workoutRow[1] = Double.toString(this.caloriesTarget);
			workoutRow[2] = Double.toString(this.caloriesConsumed);
			workoutRow[3] = Integer.toString((Integer) this.weights.getLastEntry().getElement());
			workoutRow[4] = workout.getIntensity().toString();

			workoutData.add(workoutRow);
		}

		writeFileInfo(workoutData, workoutFile);
	}

	private void writeRecipeInfo() {
		List<Record<Recipe>> recipes = (ArrayList<Record<Recipe>>) this.recipes.getElement();

		List<String[]> recipeData = new ArrayList<>();
		recipeData.add(Constants.RECIPE_INFO_HEADER);
		for(Record<Recipe> recipe: recipes) {
			Recipe r = (Recipe) recipe.getElement();
			String recipeFile = Constants.FOOD_PATH + Constants.SEPARATOR + 
				"Recipes" + Constants.SEPARATOR + r.getName() + Constants.CSV;

			Ingredient[] ingredients = r.getComponents().toArray(new Ingredient[0]);
			Integer[] ingredientAmounts = r.getIngredientAmounts().toArray(new Integer[0]);

			for(int i = 0; i < ingredientAmounts.length; i++) {
				String[] recipeRow = new String[Constants.NUM_RECIPE_FIELDS];
				recipeRow[0] = r.getName();
				recipeRow[1] = ingredients[i].getName();
				recipeRow[2] = Integer.toString(ingredientAmounts[i]);
				recipeRow[3] = ingredients[i].getVerb();

				recipeData.add(recipeRow);
			}

			writeFileInfo(recipeData, recipeFile);
		}
	}

	/**
	 * Write meal info to the user's account information
	 */
	private void writeMealInfo() {
		List<Record<Meal>> meals = (ArrayList<Record<Meal>>) this.meals.getElement();

		// Create csv data and add header
		List<String[]> mealData = new ArrayList<>();
		mealData.add(Constants.MEAL_HEADER);
		for(Record<Meal> meal: meals) {;
			Meal m = (Meal) meal.getElement();
			String mealFile = Constants.FOOD_PATH + Constants.SEPARATOR + 
				"Meals" + Constants.SEPARATOR + m.getName() + Constants.CSV;

			Recipe[] recipes = m.getComponents().toArray(new Recipe[0]);
			Integer[] recipeAmounts = m.getRecipeAmounts().toArray(new Integer[0]);

			for(int i = 0; i < recipes.length; i++) {
				Ingredient[] ingredients = recipes[i].getComponents().toArray(new Ingredient[0]);
				Integer[] ingredientAmounts = recipes[i].getIngredientAmounts().toArray(new Integer[0]);

				for(int j = 0; j < ingredients.length; j++) {
					String[] mealRow = new String[Constants.NUM_MEAL_FIELDS];
					mealRow[0] = m.getName();
					mealRow[1] = recipes[i].getName();
					mealRow[2] = Integer.toString(recipeAmounts[i]);
					mealRow[3] = ingredients[i].getName();
					mealRow[4] = Integer.toString(ingredientAmounts[i]);
					mealRow[5] = ingredients[i].getVerb();
					mealData.add(mealRow);
				}
			}

			writeFileInfo(mealData, mealFile);
		}

	}

    /**
     * Writes data to a file
     *
     * @param data Data to write
     * @param filename File to write data to
     */
	private void writeFileInfo(List<String[]> data, String filename) {
		try {
			// Initialize I/O strategies 
			WriteStrategy writeStrategy = new CSVWriter();
			WriteContext writeContext = new WriteContext();
			writeContext.setWriteStrategy(writeStrategy);
			writeStrategy.writeFile(new File(filename), data);
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public boolean recipeExists(String name) {
		List<Record<Recipe>> recipes = (ArrayList<Record<Recipe>>) this.recipes.getElement();
		for(int i = 0; i < recipes.size(); i++) {
			Record<Recipe> recipeRecord = recipes.get(i);
			Recipe recipe = (Recipe) recipeRecord.getElement();
			if(recipe.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean mealExists(String name) {
		List<Record<Meal>> meals = (ArrayList<Record<Meal>>) this.meals.getElement();
		for(int i = 0; i < meals.size(); i++) {
			Record<Meal> mealRecord = meals.get(i);
			Meal meal = (Meal) mealRecord.getElement();
			if(meal.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

    //undo functions
    public void undoWorkoutUpdate(){
        workouts.removeLastEntry();
    }

    public void undoMealUpdate(){
        meals.removeLastEntry();
    }

    //toString
    @Override
    public String toString(){
        return "-" + user + "'s Personal History-" +
        "\nCalories consumed/target: " + caloriesConsumed + "/" + caloriesTarget +
        "\nWeight History:\n" + weights +
        "\nMeal History:\n" + meals +
        "\nWorkout History:\n" + workouts;
    }
}