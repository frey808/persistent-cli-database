/**
 * Filename: ShoppingList.java
 * Description: Code to implement shopping list functionality for NUTRiAPP
 * @author Peter Carbone pjc7686
 */

package ShoppingList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Constants.*;
import Database.Processing.Searching.SearchByName;
import Database.Processing.Searching.SearchContext;
import Database.Processing.Searching.SearchStrategy;
import Database.Processing.Writing.*;
import Recipes.*;

public class ShoppingList {

	private Map<Ingredient, Integer> ingredients; // (Ingredient, stock) key-value pair

    public ShoppingList() {
		this.ingredients = new HashMap<>();
    }

    /**
     * Gets the ingredient information
     * 
     * @return Ingredient set
     */
    public HashSet<Ingredient> getIngredients() {
        return new HashSet<>(ingredients.keySet());
    }

	public Ingredient getLastIngredient() {
		Ingredient[] ingredients =  this.getIngredients().toArray(new Ingredient[0]);
		return ingredients[ingredients.length - 1];
	}

	public Integer getLastIngredientAmount() {
		return getIngredientAmounts().get(getIngredientAmounts().size() - 1);
	}

    /**
     * Gets the stock information
     * 
     * @return Stock information
     */
    public ArrayList<Integer> getIngredientAmounts() {
        return new ArrayList<>(ingredients.values());
    }

    /**
     * Show the status of each ingredient 
     */
    public void showIngredientStatus() {
        Ingredient[] keys = getIngredients().toArray(new Ingredient[0]);
        ArrayList<Integer> values = getIngredientAmounts();

        for(int i = 0; i < keys.length; i++) {
            if(values.get(i) <= Constants.LOW_STOCK_THRESHOLD && values.get(i) > 0) {
                System.out.println("Ingredient: " + keys[i].getName() + " is low in stock");
            }
            else if(values.get(i) == 0) {
                System.out.println("Ingredient: " + keys[i].getName() + " is out of stock");
            }
        }
    }

    /**
     * Add to the stock of an ingredient by a given amount
     * 
     * @param name Name of ingredient
     * @param stock Amount of stock increased by
     */
    public void addStock(String name, int stock) {
        for(Ingredient ingredient: this.getIngredients()) {
            if(ingredient.getName().equals(name)) {
                ingredients.put(ingredient, ingredients.get(ingredient) + stock);
                return;
            }
        }
    }

    /**
     * Reduce the stock of an ingredient by a given amount.
     * 
     * @param name Name of ingredient
     * @param stock Amount of stock reduced by
     */
    public void reduceStock(String name, int stock) {
        for(Ingredient ingredient: this.getIngredients()) {
            if(ingredient.getName().equals(name)) {
                ingredients.put(ingredient, ingredients.get(ingredient) - stock);
				writeIngredientInfo();
                return;
            }
        }
    }

    /**
     * Add an ingredient to the shopping list
     * 
     * @param ingredientName Ingredient to be added
     * @param stock Amount of ingredient to be added
	 * 
	 * @return true if the ingredient was found and added
     */
    public boolean addIngredient(String ingredientName, int stock) {
        boolean found = true;
		
		try {
            // Create database file and search strategy
            File databaseFile = new File(Constants.INGREDIENT_DATABASE_PATH);
            SearchStrategy strategy = new SearchByName();
            SearchContext context = new SearchContext();
            context.setIngredientSearcher(strategy);

            // Search for ingredient and check if it exists. If not, print an error message. Otherwise, 
            // Add it to the shopping list
            Ingredient ingredient = strategy.searchForIngredient(ingredientName, databaseFile);
            if(ingredient.getName().equals(Constants.INGREDIENT_NOT_FOUND)) {
                found = false;
            }
            else {
                ingredients.put(ingredient, stock);
				writeIngredientInfo();
            }

		}
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

		return found;
    }

	/**
	 * Gets the stock of an ingredient given a name
	 * 
	 * @param name Name of ingredient
	 * 
	 * @return Amount of that ingredient in stock
	 */
	private int getStockOfIngredient(String name) {
		Ingredient[] names = this.ingredients.keySet().toArray(new Ingredient[0]);
		for(int i = 0; i < names.length; i++) {
			if(names[i].getName().equals(name)) {
				return ingredients.get(names[i]);
			}
		}

		return 0;
	}

	/**
	 * Recommend ingredients for a given recipe
	 * 
	 * @param recipe Recipe to recommend ingredients for
	 * 
	 * @return true if there are enough ingredients to prepare this recipe
	 */
	public boolean recommendIngredients(Recipe recipe) {
		Ingredient[] ingredients = recipe.getComponents().toArray(new Ingredient[0]);
		ArrayList<Integer> amountNeeded = recipe.getIngredientAmounts();
		boolean canBePrepared = true;
		for(int i = 0; i < ingredients.length; i++) {
			if(this.getStockOfIngredient(ingredients[i].getName()) < amountNeeded.get(i)) {
				System.out.println("More of " + ingredients[i].getName() + " is needed for this recipe!");
				System.out.println("Amount needed: " + amountNeeded.get(i) + 
					" Amount in stock: " + this.getStockOfIngredient(ingredients[i].getName()));
				System.out.println();
				canBePrepared = false;
			}
		}

		return canBePrepared;
	}

	/**
	 * Purchases all necessary ingredients to prepare a meal
	 * 
	 * @param meal Meal to purchase ingredients for
	 */
	public void purchaseIngredients(Meal meal) {
		Recipe[] recipes = meal.getComponents().toArray(new Recipe[0]);
		ArrayList<Integer> amountOfRecipe = meal.getRecipeAmounts();
		for(int i = 0; i < recipes.length; i++) {
			Ingredient[] ingredients = recipes[i].getComponents().toArray(new Ingredient[0]);
			ArrayList<Integer> amountNeeded = recipes[i].getIngredientAmounts();
			for(int j = 0; j < ingredients.length; j++) {
				this.addIngredient(ingredients[j].getName(), amountNeeded.get(j) * amountOfRecipe.get(i));
			}
		}
	}

	/**
	 * Prepare a meal
	 * 
	 * @param meal Meal to prepare
	 * @param calorieTarget Calorie target for the day
	 * 
	 * @return Number of calories the meal was to be deducted from target
	 */
	public double prepareMeal(Meal meal, double calorieTarget) {
		if(meal.getCalories() > calorieTarget) {
			System.out.println("This meal will exceed your daily calorie target!");
		}

		if(!canPrepareMeal(meal)) {
			System.out.println("Meal " + meal.getName() + " cannot be prepared!");
			return 0;
		}

		// See if the meal can be prepared. If not, exit the method with an error message
		Recipe[] recipes = meal.getComponents().toArray(new Recipe[0]);
		Integer[] recipeAmounts = meal.getRecipeAmounts().toArray(new Integer[0]);
		
		// Prepare each recipe in the meal
		for(int i = 0; i < recipes.length; i++) {
			for(int j = 0; j < recipeAmounts[i]; j++) {
				Ingredient[] recipeIngredients = recipes[i].getComponents().toArray(new Ingredient[0]);
				Integer[] ingredientAmounts = recipes[i].getIngredientAmounts().toArray(new Integer[0]);
				for(int k = 0; k < recipeIngredients.length; k++) {
					this.reduceStock(recipeIngredients[k].getName(), ingredientAmounts[k]);
				}
			}
		}
		
		return meal.calculate() - calorieTarget;
	}

	public boolean canPrepareMeal(Meal meal) {
		boolean canBePrepared = true;

		// See if the meal can be prepared. If not, exit the method with an error message
		Recipe[] recipes = meal.getComponents().toArray(new Recipe[0]);
		for(int i = 0; i < recipes.length; i++) {
			if(!recommendIngredients(recipes[i])) {
				// System.out.println("Not enough ingredients to prepare: " + recipes[i].getName());
				canBePrepared = false;
			}
		}

		return canBePrepared;
	}

	public String toString() {
		Ingredient[] names = this.ingredients.keySet().toArray(new Ingredient[0]);
		Integer[] amounts = this.ingredients.values().toArray(new Integer[0]);
		String items = "";
		for(int i = 0; i < amounts.length; i++) {
			items += "Ingredient: " + names[i].getName() + " Amount: " + amounts[i] + "\n"; 
		}
		return items;
	}

	/**
     * Writes the shopping list information to a csv file for the current user
     */
    private void writeIngredientInfo() {
        String filename = Constants.FOOD_PATH + Constants.SEPARATOR + "stock" + Constants.CSV;
        List<String[]> stockInfo = new ArrayList<>();
        stockInfo.add(Constants.INGREDIENT_INFO_HEADER);

        Ingredient[] keys = this.getIngredients().toArray(new Ingredient[0]);
        ArrayList<Integer> values = this.getIngredientAmounts();

        for(int i = 0; i < keys.length; i++){
            String[] ingredientFields = new String[Constants.NUM_INGREDIENT_FIELDS];
            ingredientFields[0] = keys[i].getName();
            ingredientFields[1] = Integer.toString(keys[i].getCalories());
            ingredientFields[2] = Double.toString(keys[i].getSaturatedFat());
            ingredientFields[3] = Double.toString(keys[i].getMonosaturatedFat());
            ingredientFields[4] = Double.toString(keys[i].getPolysaturatedFat());
            ingredientFields[5] = Double.toString(keys[i].getProtein());
            ingredientFields[6] = Double.toString(keys[i].getFiber());
            ingredientFields[7] = Double.toString(keys[i].getCarbohydrates());
            ingredientFields[8] = Integer.toString(values.get(i));
            stockInfo.add(i + 1, ingredientFields);
        }

		writeFileInfo(stockInfo, filename);
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
}