/**
 * Filename: IngredientInfo.java
 * Description: Class defining info on how an ingredient is stored in the database. 
 * 
 * GoF Pattern: Composite
 * GoF Role: Leaf
 * 
 * @author Peter Carbone pjc7686
 * @author Qadira Moore
 */

package Recipes;

import java.io.File;
import java.io.IOException;

import Constants.*;
import Database.Processing.Searching.SearchByName;
import Database.Processing.Searching.SearchContext;
import Database.Processing.Searching.SearchStrategy;

public class Ingredient implements Food {

    // Private Fields
    private String name;
	private String verb;
    private int calories;
    private double saturatedFat;
    private double monounsaturatedFat;
    private double polyunsaturatedFat; 
    private double totalFat;   // totalFat = saturatedFat + monounsaturatedFat + polyunsaturatedFat
    private double protein;
    private double fiber;
    private double carbohydrates;
	private double caloriesPerUnit;
	private double fatPerUnit;
	private double proteinPerUnit;
	private double fiberPerUnit;
	private double carbsPerUnit;
	private double totalGrams;

	private Ingredient ingredient;

    /**
     * Constructs an Ingredient Object
     * 
     * @param name Name of the ingredient
     * @param calories Amount of calories
     * @param saturatedFat Amount of saturated fat
     * @param monounsaturatedFat Amount of monounsturated fat
     * @param polyunsaturatedFat Amount of polyunsaturated fat
     * @param protein Amount of protein
     * @param fiber Amount of fiber
     * @param carbohydrates Amount of carbohydrates
     */
    public Ingredient(String name, int calories, double saturatedFat, double monounsaturatedFat, 
    	double polyunsaturatedFat, double protein, double fiber, double carbohydrates) {
        this.name = name;
        this.calories = calories;
        this.saturatedFat = saturatedFat;
        this.monounsaturatedFat = monounsaturatedFat;
        this.polyunsaturatedFat = polyunsaturatedFat;
        this.totalFat = this.saturatedFat + this.monounsaturatedFat + this.polyunsaturatedFat;
        this.protein = protein;
        this.fiber = fiber;
        this.carbohydrates = carbohydrates;
		this.totalGrams = this.totalFat + this.protein + this.fiber + this.carbohydrates;
		this.caloriesPerUnit = this.calories / this.totalGrams;
		this.fatPerUnit = this.totalFat / this.totalGrams;
		this.fiberPerUnit = this.totalFat / this.totalGrams;
		this.proteinPerUnit = this.protein / this.totalGrams;
		this.carbsPerUnit = this.carbohydrates / this.totalGrams;
    }

	public Ingredient(String name, String verb) {
		this.name = name;
		this.verb = verb;

		this.getDatabaseInfo(name);

		this.calories = this.ingredient.getCalories();
		this.saturatedFat = this.ingredient.getSaturatedFat();
		this.monounsaturatedFat = this.ingredient.getMonosaturatedFat();
		this.polyunsaturatedFat = this.ingredient.getPolysaturatedFat();
		this.totalFat = this.ingredient.getTotalFat();
		this.protein = this.ingredient.getProtein();
		this.fiber = this.ingredient.getFiber();
		this.carbohydrates = this.ingredient.getCarbohydrates();
		this.totalGrams = this.ingredient.getTotalGrams();
		this.caloriesPerUnit = this.ingredient.getCaloriesPerUnit();
		this.fatPerUnit = this.ingredient.getFatPerUnit();
		this.fiberPerUnit = this.ingredient.getFiberPerUnit();
		this.proteinPerUnit = this.ingredient.getProteinPerUnit();
		this.carbsPerUnit = this.ingredient.getCarbsPerUnit();
	}

	private void getDatabaseInfo(String name) {
		try {
			SearchStrategy searchStrategy = new SearchByName();
			SearchContext context = new SearchContext();
			context.setIngredientSearcher(searchStrategy);
			this.ingredient = searchStrategy.searchForIngredient(name, new File(Constants.INGREDIENT_DATABASE_PATH));
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

    //=============== Get Methods ================//

    public String getName() {
        return this.name;
    }

	public String getVerb() {
		return this.verb;
	}

    public int getCalories() {
        return this.calories;
    }

    public double getSaturatedFat() {
        return this.saturatedFat;
    }

    public double getMonosaturatedFat() {
        return this.monounsaturatedFat;
    }

    public double getPolysaturatedFat() {
        return this.polyunsaturatedFat;
    }

    public double getTotalFat() {
        return this.totalFat;
    }

    public double getProtein() {
        return this.protein;
    }

    public double getFiber() {
        return this.fiber;
    }

    public double getCarbohydrates() {
        return this.carbohydrates;
    }

	public double getCaloriesPerUnit() {
		return this.caloriesPerUnit;
	}

	public double getTotalGrams() {
		return this.totalGrams;
	}

	public double getFatPerUnit() {
		return this.fatPerUnit;
	}

	public double getFiberPerUnit() {
		return this.fiberPerUnit;
	}

	public double getProteinPerUnit() {
		return this.proteinPerUnit;
	}

	public double getCarbsPerUnit() {
		return this.carbsPerUnit;
	}

	@Override
    public void prepare() {
        System.out.println(verb+"ing the "+name);

    }
    @Override
    public double calculate() {
        return caloriesPerUnit;
    }

    /**
     * Two Ingredient objects are equal if all of their fields match
     * 
     * @param i IngredientInfo object to check
     * 
     * @return True if equal, false otherwise
     */
    // public boolean equals(Ingredient i) {
    //     return this.name.equals(i.name) && this.calories == i.calories 
    //     && this.saturatedFat == i.saturatedFat && this.monounsaturatedFat == i.monounsaturatedFat
    //     && this.polyunsaturatedFat == i.polyunsaturatedFat && this.protein == i.protein 
    //     && this.fiber == i.fiber && this.carbohydrates == i.carbohydrates;
    // }
}