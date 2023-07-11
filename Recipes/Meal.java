package Recipes;
import java.util.*;

import org.junit.Assert;

/*
 * Let the user create a recipe using a bunch of foods
 */

public class Meal implements Food{
    
    private String name;
    private int calories;
    private Map<Recipe, Integer> recipes;
	private double caloriesPerUnit;
	private double fatPerUnit;
	private double proteinPerUnit;
	private double fiberPerUnit;
	private double carbsPerUnit;

    public Meal(String name) {
        this.name = name;
		this.recipes = new HashMap<>();
		this.caloriesPerUnit = 0;
		this.fatPerUnit = 0;
		this.fiberPerUnit = 0;
		this.proteinPerUnit = 0;
		this.carbsPerUnit = 0;
    }

	public String getName() {
		return this.name;
	}

	public double getCalories() {
		return this.caloriesPerUnit;
	}

	public Map<Recipe, Integer> getRecipes() {
		return this.recipes;
	}

	public HashSet<Recipe> getComponents() {
		return new HashSet<>(recipes.keySet());
	}

	public ArrayList<Integer> getRecipeAmounts() {
		return new ArrayList<>(recipes.values());
	}

    /**
     * Create a recipe for the meal and how many you will be making
     * @param recipe
     * @param quantity
     */
    public void addRecipe(Recipe recipe, int quantity) {
        if (recipes.containsKey(recipe) && quantity > 0){
            recipes.put(recipe , recipes.get(recipe) + quantity); //increase the amount
        }   
        else {
            recipes.put(recipe, quantity); //create a new entry
        }

		for(int i = 0; i < quantity; i++) {
			adjustGramsPerUnit(recipe);
		}
    }

	private void adjustGramsPerUnit(Recipe recipe) {
		int quantity = this.recipes.get(recipe);
		Assert.assertNotNull(recipe);
		Ingredient[] ingredients = recipe.getComponents().toArray(new Ingredient[0]);
		for(Ingredient ingredient: ingredients) {
			this.caloriesPerUnit += ingredient.getCaloriesPerUnit() * quantity;
			this.fatPerUnit += ingredient.getFatPerUnit() * quantity;
			this.fiberPerUnit += ingredient.getFiberPerUnit() * quantity;
			this.proteinPerUnit += ingredient.getProteinPerUnit() * quantity;
			this.carbsPerUnit += ingredient.getCarbsPerUnit() * quantity;
		}
	}

    /**
     * Delete a recipe from the meal how many you are removing
     * @param recipe
     * @param quantity
     */
    public void removeRecipe(Recipe recipe, int quantity) {
        if (recipes.containsKey(recipe)){
            recipes.put(recipe , recipes.get(recipe) - quantity); //decrease the amount

            if (recipes.get(recipe) <= 0){
                recipes.remove(recipe); //delete entry
            }
               
        }
    }

    /**
     * Prepare the meal. Those subcomponets are recipes have a prepare method as well
     * which will be called. 
     */
    @Override
    public void prepare() {
        //print out all the recipes in the meal. 
        // one meal can have multiple recipes
        
        System.out.println("__________________________________");
        System.out.println("Preparing " + name+":");
        System.out.println("Recipes: ");
        System.out.println("__________________________________");
        
        for (Map.Entry<Recipe,Integer> map : recipes.entrySet()) {
            System.out.println("\nMaking "+ map.getValue()+" "+map.getKey().getName());
            map.getKey().prepare();
        }
        System.out.println();
    }

    @Override
    public double calculate() {
        for (Map.Entry<Recipe,Integer> map : recipes.entrySet()) {
            map.getKey().prepare();
        }
        return this.caloriesPerUnit;
    }
}
