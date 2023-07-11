package UI;

/**
 * Filename: NutriAppUI.java
 * Description: NutriApp User interface. User interacts with the application through
 * this command line interface in the console. The user can create goals, workout history, list goals and other functions.
 * Every 24 hours automatically executes daily operations.
 * @author Tyler Kranzen tok4416 
 * @author Peter Carbone pjc7686
 * @author Kai Frazier ksf7880
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.lang.model.type.UnionType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;

import Accounts.Guest;

import Accounts.Registered_User;
import Constants.Constants;
import Database.Processing.Reading.*;
import Goals.GoalTypes;
import Goals.Goals;
import Recipes.Food;
import Recipes.Ingredient;
import Recipes.Meal;
import Recipes.Recipe;
import Workout.History.History;

import org.junit.*;


public class NutriAppUI {
	private List<Registered_User> userList;
 
	public NutriAppUI() {
		this.userList = new ArrayList<Registered_User>();
	}
 
	/**
	 * Create a registered user object when someone authenticates
	 * 
	 * @return new Registered_User object
	 */
	public Registered_User authenticate(BufferedReader reader) throws IOException {
		System.out.println("Enter your username");
	 	String username = reader.readLine();
 
		File[] files = new File(Constants.ACCOUNT_INFO_PATH).listFiles();
		for(File file: files) {
			if(file.getName().equals(username)) {
				System.out.println("Enter your password");
				String password = reader.readLine();
 
				ReadStrategy readStrategy = new CSVReader();
				ReadContext context = new ReadContext();
				context.setReadStrategy(readStrategy);
				List<String[]> userData = readStrategy.readFile(new File(Constants.ACCOUNT_INFO_PATH + "\\" + username +  "\\" + "info.csv"));
				userData.remove(0);
 
				String storedPassword = userData.get(0)[1];
				byte[] passwordBytes = Base64.getDecoder().decode(storedPassword);
				String decodedPassword = new String(passwordBytes);

				if(password.equals(decodedPassword)) {
					System.out.println("Currently logged in as  " + username);
					return new Registered_User(username, password);
				}
				else {
					System.out.println("Incorrect password, try again");
				}
			}
		}
 
		System.out.println("Enter your password");
		String password = reader.readLine();
 
		System.out.println("Enter your height");
		int height = Integer.parseInt(reader.readLine());
 
		System.out.println("Enter your weight");
		int weight = Integer.parseInt(reader.readLine());
 
		System.out.println("Enter your birthday (mm/dd/yyyy)");
		String birthday = reader.readLine();
			 
		System.out.println("Enter your goal (gain, maintain, lose)");
		String goal = reader.readLine();
 
		return new Registered_User(username, password, height, weight, birthday, goal);
	}
 
	public void runUI() {
		Guest guest = new Guest();

		System.out.println("Welcome to the NutriApp!");
		System.out.println("Currently viewing as a Guest!");
		startDailyCycle();
 
		guestCommands();
 
		System.out.println("\nEnter a command or type quit: ");
		 
		String command = "";
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

			while(!command.equals("quit")) { // Guest User
				command = reader.readLine();
 
				if(command.equals("HELP")) {
					guestCommands();
				}
				else if(command.equals("quit")) {
					reader.close();
					System.exit(0);
				}
				else if(command.equals("INGREDIENTS")) {
					guest.browseDatabase();
				}
				else if(command.equals("STOCK")) {
					guest.Browse_Ingredients();
				}
				else if(command.equals("RECIPES")) {
					guest.Browse_Recipes();
				}
				else if(command.equals("MEALS")) {
					guest.Browse_Meals();
				}
				else if(command.equals("LOGIN")) {
					break;
				}
				else if(command.equals("CREATEACCOUNT")) {
					System.out.println("Enter your username");
					String username = reader.readLine();
 
					System.out.println("Enter your password");
					String password = reader.readLine();
 
					System.out.println("Enter your height");
					int height = Integer.parseInt(reader.readLine());
 
					System.out.println("Enter your weight");
					int weight = Integer.parseInt(reader.readLine());
 
					System.out.println("Enter your birthday (mm/dd/yyyy)");
					String birthday = reader.readLine();
						 
					System.out.println("Enter your goal (gain, maintain, lose)");
					String goal = reader.readLine();
 
					
					Registered_User user = new Registered_User(username, password, height, weight, birthday, goal);
					userList.add(user);

					System.out.println("Account created");
				}
				else if(command.equals("quit")) {
					System.exit(0);
				}
				else {
					System.err.println("Unsupported command: " + command);
				}
			}
 
			Registered_User user = authenticate(reader);		
			

			Meal lastPreparedMeal = null;
			String lastPurchasedIngredientName = "";
			int lastPurchasedIngredientAmount = 0;
			while(!command.equals("quit")) { //Registered User
				command = reader.readLine();
				 
				if(command.equals("EXPORT")) {
					String fileType = "";

					boolean validType = false;
					while(!validType) {
						System.out.println("Select the file type to export these files to (csv, json, xml)");
						fileType = reader.readLine().trim();
						if(!fileType.equals("csv") && !fileType.equals("json") && !fileType.equals("xml")) {
							System.out.println("Invlaid fileType: " + fileType + ". Please enter csv, json, or xml");
						}
						else {
							validType = true;
						}
					}
 
					user.exportDatabase(fileType, System.getProperty("user.dir"));
					System.out.println("Files successfully exported");
				}
				else if(command.equals("HELP")) {
					showCommands();
				}
				else if(command.equals("IMPORT")) {
					String type = "";

					boolean validType = false;
					while(!validType) {
						System.out.println("Select the type of file to be imported (meal, workouts, ingredients, stock)");
						type = reader.readLine();
						if(!type.equals("meal") && !type.equals("workouts") && 
							!type.equals("stock") && !type.equals("ingredients")) {
							System.out.println("Invalid import type " + type + ". Please enter a valid type");
						}
						else {
							validType = true;
						} 
					}
 
					String fileType = "";
					boolean validFormat = false;
					while(!validFormat) {
						System.out.println("Select the file type of the imported file (csv, json, xml)");
						fileType = reader.readLine();
						if(!fileType.equals("csv") && !fileType.equals("json") && !fileType.equals("xml")) {
							System.out.println("Invalid file type " + fileType + ". Please enter a valid type");
						}
						else {
							validFormat = true;
						} 
					}
 
					String path = "";
					boolean validPath = false;
					while(!validPath) {
						System.out.println("Enter the path of the file to be imported (C:\\Users\\...someFile.csv, json, or xml)");
						path = reader.readLine();
						File tempFile = new File(path);
						if(!tempFile.exists()) {
							System.out.println("Invalid path " + path + ". Please enter a valid file path");
						}
						else {
							validPath = true;
						}
					}
 
					user.importFile(path, type, fileType);

					System.out.println("File successfully imported");
				}
				else if(command.equals("INFO")) {
					System.out.println(user.toString());
				}
				else if(command.equals("CREATEMEAL")) {
					if(user.view_History().getRecipes().size() == 0) {
						System.out.println("No recipes exist, please create at least 1 recipe before preparing a meal");
					}
					else {
						System.out.println("Enter the name of your meal");
						boolean uniqueName = false;
						String mealName = "";
						Meal meal = null;
						while(!uniqueName) {
							mealName = reader.readLine();
							if(user.view_History().mealExists(mealName)) {
								System.out.println("Meal " + mealName + " already exists! Please enter a new name");
							}
							else {
								meal = new Meal(mealName);
								uniqueName = true;
							}
						}
						while(true) {
						String recipeName = "";
						int recipeAmount = 0;
						
						boolean exists = false;
						while(!exists) {
							System.out.println("Enter the name of the recipe to add to this meal");
							recipeName = reader.readLine();
							if(!user.view_History().recipeExists(recipeName)) {
								System.out.println("Recipe " + recipeName + " does not exist. Please enter the name of a recipe that exists");
							}
							else {
								exists = true;
							}
						}

						System.out.println("Enter the amount of " + recipeName + " to add to this meal");
						recipeAmount = Integer.parseInt(reader.readLine());

						meal.addRecipe(user.view_History().getRecipe(recipeName), recipeAmount);

						System.out.println("Would you like to keep adding recipes to this meal? (yes/no)");
						String response = reader.readLine();

						if(response.equals("no")) {
							break;
						}
						System.out.println("Added " + recipeAmount + " of recipe " + recipeName + " to meal " + mealName);
					}
					user.view_History().updateMeals(meal);
					System.out.println("Successfully created meal " + mealName + "\n");

					
					}
				}
				else if(command.equals("CREATERECIPE")) {
					System.out.println("Enter the name of the recipe (or stop to quit)");
					boolean uniqueName = false;
					String recipeName = "";
					Recipe recipe = null;

					while(!uniqueName) {
						recipeName = reader.readLine();
						if(user.view_History().recipeExists(recipeName)) {
							System.out.println("Recipe " + recipeName + " already exists! Please enter a new name");
						}
						else if(recipeName.equals("stop")) {
							System.out.println("Cancelling meal creation");
							break;
						}
						else {
							recipe = new Recipe(recipeName);
							uniqueName = true;
						}
					}
 
					while(true) {
						System.out.println("Enter the name of an ingredient to add to this recipe. Type 'stop' to end");
						String ingredientName = reader.readLine();
						 
						System.out.println("Enter the instructions for preparing this food");
						String instructions = reader.readLine();
 
						System.out.println("Enter how many of this ingredient to add to this recipe");
						String amount = reader.readLine();

						recipe.addFood(new Ingredient(ingredientName, instructions), Integer.parseInt(amount));

						System.out.println("Would you like to keep adding ingredients? (yes/no)");
						String response = reader.readLine();

						if(response.equals("no")) {
							break;
						}
					}
					user.view_History().updateRecipes(recipe);

					System.out.println("Recipe " + recipeName + " has been added!\n");
				}
				else if(command.equals("PREPAREMEAL")) {
					System.out.println("Enter the name of the meal to prepare");
					String mealName = reader.readLine();

					Meal meal = user.view_History().getMeal(mealName);
					if(!user.view_History().getShoppingList().canPrepareMeal(meal)) {
						System.out.println("Not enough ingredients to prepare " + mealName);
						System.out.println("Would you like to purchase the necessary ingredients? (yes/no)");
						
						String response = reader.readLine();
						boolean validResponse = false;
						while(!validResponse) {
							if(response.equals("yes")) {
								user.view_History().getShoppingList().purchaseIngredients(meal);
								validResponse = true;
								System.out.println("Ingredients purchased. Please re-enter the PREPAREMEAL command to prepare this meal");
							}
							else if(response.equals("no")) {
								validResponse = true;
								System.out.println("No ingredients purchased");
							}
							else {
								System.out.println("Invalid response " + response + ". Please respond with 'yes' or 'no");
							}
						}
					}
					else {
						user.view_History().getShoppingList().prepareMeal(meal, user.view_History().getCaloriesTarget());
						lastPreparedMeal = meal;
						System.out.println("Meal " + mealName + " prepared!\n");
					}
				}
				else if(command.equals("LISTGOALS")) {
					System.out.println(Goals.listGoals());
				}
				else if(command.equals("PURCHASEINGREDIENT")) {
					System.out.println("Enter the name of the ingredient you would like to purchase");
					String ingredientName = reader.readLine();

					System.out.println("Enter the amount of " + ingredientName + " to purchase");
					int ingredientAmount = Integer.parseInt(reader.readLine());

					boolean validName = false;
					while(!validName) {
						if(!user.view_History().getShoppingList().addIngredient(ingredientName, ingredientAmount)) {
							System.out.println("Ingredient" + ingredientAmount + " is not in the database");
						}
						else {
							System.out.println(ingredientAmount + " of " + ingredientName + " purchased!");
							lastPurchasedIngredientName = ingredientName;
							lastPurchasedIngredientAmount = ingredientAmount;
							validName = true;
						}
					}
				}
				else if(command.equals("SHOPPINGLIST")) {
					System.out.println(user.view_History().getShoppingList().toString());
				}
				else if(command.equals("WORKOUTS")) {
					System.out.println(user.get_Workout_History());
				}
				else if(command.equals("HELP")) {
					showCommands();
				}
				else if(command.equals("quit")) {
					System.exit(0);
				}
				else if(command.equals("RECIPES")) {
					user.Browse_Recipes();
				}
				else if(command.equals("MEALS")) {
					user.Browse_Meals();
				}
				else if(command.equals("INGREDIENTS")) {
					user.browseDatabase();
				}
				else if(command.equals("CREATETEAM")) {
					System.out.println("Enter a name for your team");
					String teamname = reader.readLine();
					user.create_Team(teamname);
					System.out.println("Team created");
				}
				else if(command.equals("INVITEUSER")) {
					System.out.println("Enter the name of the user you would like to invite");
					String username = reader.readLine();
					user.send_invite(user);
					System.out.println("User invited");
				}
				else if(command.equals("LISTINVITES")) {
					if(user.view_invites() ==  null){
						System.out.println("No new invites");
					}
					else{
						System.out.println(user.view_invites());
					};
				}
				else if(command.equals("REJECTINVITE")) {
					System.out.println("Enter the name of the user whos invite you would like to reject");
					String username = reader.readLine();
					if(user.view_invites().size() == 0){
						System.out.println("No new invites");
					}
					else{
						user.reject_invite(username);
						System.out.println("Invite rejected");
					}
				}
				
				else if(command.equals("REJECTALL")) {
					System.out.println("All invites rejected");
					user.reject_all_invites();
				}
				else if(command.equals("LEAVETEAM")) {
					user.leave_Team();
					System.out.println("Team left");
				}
				else if(command.equals("ISSUECHALLENGE")) {
					System.out.println("What is the challenge you would like to issue");
					String challenge = reader.readLine();
					user.issue_Challenge(challenge);
					System.out.println("Challenge issued");
				}
				else if(command.equals("VIEW_MEMBER_WORKOUT_HISTORY")) {
					System.out.println("Enter the name of the user whos workout history you would like to view");
					String username = reader.readLine();
					System.out.println(user.get_TeamMate_Workout_History(username));
					
				}
				else if(command.equals("VIEWNOTIFICATIONS")) {
					if(user.get_Notifications() == null){
						System.out.println("No new notifications");
					}
					else{
						System.out.println(user.get_Notifications());
					}
				}
				else if(command.equals("CONSUMECALORIES")) {
					System.out.println("How many calories you have consumed: ");
					String input = reader.readLine();
					int calories = Integer.parseInt(input);
					Workout.History.Workout workoutRecommendation = user.getPersonalHistory().consumeCalories(calories);
					if(workoutRecommendation != null){
						System.out.println("You exceeded your target calories! Recommended workout to stay on track: \n" +
						workoutRecommendation +
						"\nWould you like to perform recommended workout? Y/N");
						input = reader.readLine();
						if(input.equals("Y")){
							user.getPersonalHistory().updateWorkouts(workoutRecommendation);
							System.out.println("Get going! Your records have already been updated with this workout.");
						}else{
							System.out.println("Workout declined");
						}
					}
				}
				else if(command.equals("BROWSE_PERSONAL_HISTORY")) {
					if(user.getPersonalHistory() == null){
						System.out.println("Personal History is empty");
					}
					else{
						System.out.println(user.getPersonalHistory()); 
					}
				}
				else if(command.equals("ADDWORKOUT")){
					System.out.println("Add a workout: ");
					String input = reader.readLine();
					System.out.println("Number of minutes:  ");
					String input2 = reader.readLine();
					int minutes = Integer.parseInt(input2);
					System.out.println("Intensity (easy. medium, hard)");
					String intensity = reader.readLine();
					Workout.History.Workout workout = new Workout.History.Workout(input,minutes,intensity);
					user.getPersonalHistory().updateWorkouts(workout);

				}
				else if(command.equals("UPDATEWEIGHT")){
					System.out.println("Add a weight: ");
					String input = reader.readLine();
					int weight = Integer.parseInt(input);
					user.getPersonalHistory().updateWeight(weight);
				}
				else if(command.equals("UNDO PREPAREMEAL")){
					if(lastPreparedMeal == null) {
						lastPreparedMeal = (Meal) user.view_History().getMeals().getLastEntry().getElement();
					}
					Map<Recipe, Integer> recipes = lastPreparedMeal.getRecipes();
					for(Map.Entry<Recipe, Integer> map: recipes.entrySet()) {
						
						Map<Ingredient, Integer> ingredients = map.getKey().getFoods();
						for(Map.Entry<Ingredient, Integer> subMap: ingredients.entrySet()) {

							user.view_History().getShoppingList().addIngredient(subMap.getKey().getName(), subMap.getValue() * map.getValue());
						}
					}
					user.getPersonalHistory().updateCaloriesTarget(user.getPersonalHistory().getCaloriesConsumed() - lastPreparedMeal.getCalories());
				}
				else if(command.equals("UNDO PURCHASEINGREDIENT")) {
					if(lastPurchasedIngredientName.equals("")) {
						lastPurchasedIngredientName = user.view_History().getShoppingList().getLastIngredient().getName();
						lastPurchasedIngredientAmount = user.view_History().getShoppingList().getLastIngredientAmount();
					}
					user.view_History().getShoppingList().reduceStock(lastPurchasedIngredientName, lastPurchasedIngredientAmount);
				}
				else if(command.equals("UNDOWORKOUT")){
					user.getPersonalHistory().undoWorkoutUpdate();
				}
				else if(command.equals("quit")) {
					reader.close();
					System.exit(0);
				}
				else {
					System.err.println("Unsupported command: " + command);
				}
			}
		}
 
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
 
	/**
	 * Show the commands available to an authenticated user
	 */
	public static void showCommands() {
		System.out.println("\nINFO - View user info");
		System.out.println("LISTGOALS - Lists all goals");
		System.out.println("RECIPES - Lists all recipes");
		System.out.println("SHOPPINGLIST - List items in shopping list");
		System.out.println("EXPORT - Export the ingredient database and personal information");
		System.out.println("IMPORT - Import data from a csv, json, or xml file into the database");
		System.out.println("HELP - List all commands");
		System.out.println("CREATEMEAL - Create a meal");
		System.out.println("CREATERECIPE - Create a recipe");
		System.out.println("PREPAREMEAL - Prepare a meal");
		System.out.println("PURCHASEINGREDIENT - Purchase an amount of an ingredient in the database");
		System.out.println("CREATETEAM - Create a team");
		System.out.println("INVITEUSER - Invite a user to your team");
		System.out.println("LISTINVITES - Lists all pending invites");
		System.out.println("REJECTINVITE - Rejects a specific invite");
		System.out.println("REJECTALL - Rejects all invites");
		System.out.println("LEAVETEAM - Leave the team that you are in");
		System.out.println("ISSUECHALLENGE - Issues a challenge to your team members");
		System.out.println("VIEW_MEMBER_WORKOUT_HISTORY - View a specific team member's workout history");
		System.out.println("VIEWNOTIFICATIONS - View all notifications");
		System.out.println("UNDO PREPAREMEAL - Undo the most recently prepared meal");
		System.out.println("UNDO PURCHASEINGREDIENT - Undo purchasing of the most recent ingredient");
	}
 
	/**
	 * Show the commands available to a guest
	 */
	public static void guestCommands() {
		System.out.println("MEALS - Lists all meals");
		System.out.println("RECIPES - Lists all recipes");
		System.out.println("INGREDIENTS - Lists all ingredients");
		System.out.println("LOGIN - Log into an account or create one");
		System.out.println("CREATEACCOUNT - Create an account");
		System.out.println("HELP - List all commands");
	}

	public void dailyOperations(){
		for(Registered_User user : userList){
			user.dailyOperations();
		}
	}

	private void startDailyCycle(){
		Thread cycle = new DailyCycle(this);
		cycle.start();
	}
	 
	public static void main(String args[]){
		NutriAppUI ui = new NutriAppUI();
		ui.runUI();
 	}
}