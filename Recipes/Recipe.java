package Recipes;
import java.util.*;
public class Recipe implements Food {
    
    private String name;
    private int calories;
    private Map<Ingredient, Integer> foods; 
	private double caloriesPerUnit;
	private double fatPerUnit;
	private double proteinPerUnit;
	private double fiberPerUnit;
	private double carbsPerUnit;
    
    public Recipe(String name){
        this.name = name;
        this.foods = new HashMap<>();
		this.caloriesPerUnit = 0;
		this.fatPerUnit = 0;
		this.fiberPerUnit = 0;
		this.proteinPerUnit = 0;
		this.carbsPerUnit = 0;
    }
	
    public String getName() {
        return name;
    }

    public Map<Ingredient, Integer> getFoods() {
        return foods;
    }

	public HashSet<Ingredient> getComponents() {
		return new HashSet<>(foods.keySet());
	}

	public ArrayList<Integer> getIngredientAmounts() {
		return new ArrayList<>(foods.values());
	}

    /**
     * Update the name of the recipe
     * @param newName
     */
    public void editName(String newName){
        this.name = newName;
    }
    
    /**
     * Add a food to the recipe and how much
     * @param food
     * @param quantity
     */
    public void addFood(Ingredient food, int quantity){
        if (foods.containsKey(food) && quantity > 0){
            foods.put(food , foods.get(food) + quantity); //increase the amount
        }   
        else {
            foods.put(food, quantity); //create a new ingredient
        }

		for(int i = 0; i < quantity; i++) {
			adjustGramsPerUnit(food);
		}
    }

	private void adjustGramsPerUnit(Ingredient ingredient) {
		this.caloriesPerUnit += ingredient.getCaloriesPerUnit();
		this.fatPerUnit += ingredient.getFatPerUnit();
		this.fiberPerUnit += ingredient.getFiberPerUnit();
		this.proteinPerUnit += ingredient.getProteinPerUnit();
		this.carbsPerUnit += ingredient.getCarbsPerUnit();
	}

    /**
     * Delete a food from the recipe how many you are removing
     * @param food
     * @param quantity
     */
    public void removeFood(Ingredient food, int quantity){
        if (foods.containsKey(food)){
            foods.put(food , foods.get(food) - quantity); //remove the amount
            
            if (foods.get(food)<= 0){
                foods.remove(food); //delete entry
            }
        }    
    }

    //print the name
    @Override
    public String toString() {
        return name;
    }
    /**
     * Prepare the recipe. Those subcomponets are food have a prepare method as well
     * which will be called. 
     */
    @Override
    public void prepare() {
        System.out.println("Preparing " + name+"...");
        System.out.println("Foods: ");
        for (Map.Entry<Ingredient,Integer> map : foods.entrySet()) {
            System.out.print("-"+map.getValue()+" "+map.getKey().getName()+", ");// print the quant and food name
            map.getKey().prepare();
            
        }
    }
    @Override
    public double calculate() {
        for (Map.Entry<Ingredient,Integer> map : foods.entrySet()) {
            map.getKey().prepare();
        }
        return this.caloriesPerUnit;
    }
}
