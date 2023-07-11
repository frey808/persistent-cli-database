package ShoppingList;

import java.util.HashSet;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.*;

import Recipes.Food;
import Recipes.Ingredient;
import Recipes.Meal;
import Recipes.Recipe;

public class TestShoppingList {
	@Test
	public void testAddIngredient() {
		ShoppingList shoppingList = new ShoppingList();

		shoppingList.addIngredient("BUTTER WITH SALT", 10);
		HashSet<Ingredient> ingredients = shoppingList.getIngredients();
		ArrayList<Ingredient> test = new ArrayList<>(ingredients);
		Ingredient ingredient = test.remove(0);
		Assert.assertEquals(ingredient.getName(), "BUTTER WITH SALT");
	}

	@Test
	public void testAddStock() {
		ShoppingList shoppingList = new ShoppingList();
		shoppingList.addIngredient("BUTTER WITH SALT", 10);
		ArrayList<Integer> stocks = shoppingList.getIngredientAmounts();
		Assert.assertEquals(stocks.get(0).intValue(), 10);
		
		shoppingList.addStock("BUTTER WITH SALT", 10);
		stocks = shoppingList.getIngredientAmounts();
		Assert.assertEquals(stocks.get(0).intValue(), 20);
	}

	@Test
	public void testReduceStock() {
		ShoppingList shoppingList = new ShoppingList();
		shoppingList.addIngredient("BUTTER WITH SALT", 10);
		ArrayList<Integer> stocks = shoppingList.getIngredientAmounts();
		Assert.assertEquals(stocks.get(0).intValue(), 10);
		
		shoppingList.reduceStock("BUTTER WITH SALT", 10);
		stocks = shoppingList.getIngredientAmounts();
		Assert.assertEquals(stocks.get(0).intValue(), 0);
	}

	@Test
	public void testRecommendation() throws IOException {
		ShoppingList shoppingList = new ShoppingList();
		Recipe recipe = new Recipe("heart attack");
		recipe.addFood(new Ingredient("BUTTER WITH SALT", "whip"), 100);
		recipe.addFood(new Ingredient("CHEESE FONTINA", "grate"), 50);
		Assert.assertFalse(shoppingList.recommendIngredients(recipe));

		shoppingList.addIngredient("BUTTER WITH SALT", 100);
		shoppingList.addIngredient("CHEESE FONTINA", 50);
		Assert.assertTrue(shoppingList.recommendIngredients(recipe));
	}

	@Test
	public void testPreparemeal() throws IOException {
		ShoppingList shoppingList = new ShoppingList();
		Recipe dairy = new Recipe("heart attack");
		dairy.addFood(new Ingredient("BUTTER WITH SALT", "whip"), 100);
		dairy.addFood(new Ingredient("CHEESE FONTINA", "grate"), 50);
		
		Recipe moreDairy = new Recipe("just milk");
		moreDairy.addFood(new Ingredient("MILK LO NA FLUID", "pour"), 10);
		moreDairy.addFood(new Ingredient("MILK SHEEP FLUID", "pour"), 10);

		Meal meal = new Meal("max bmi");
		meal.addRecipe(dairy, 2);
		meal.addRecipe(moreDairy, 3);

		double result = shoppingList.prepareMeal(meal, 0);
		Assert.assertEquals(result, 0);

		shoppingList.addIngredient("BUTTER WITH SALT", 100);
		shoppingList.addIngredient("CHEESE FONTINA", 50);
		shoppingList.addIngredient("MILK LO NA FLUID", 10);
		shoppingList.addIngredient("MILK SHEEP FLUID", 10);
		
		int target = 10;
		result = shoppingList.prepareMeal(meal, target);
		Assert.assertNotEquals(result, 0);
	}
}
