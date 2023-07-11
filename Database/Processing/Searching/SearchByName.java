/**
 * Filename: SearchByName.java
 * Description: File defining a sequential search for an ingredient based on its name
 * GoF Pattern: Strategy
 * GoF Role: ConcreteStrategy
 * @author Peter Carbone pjc7686
 */

package Database.Processing.Searching;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Constants.*;
import Database.Processing.Reading.*;
import Recipes.Ingredient;

public class SearchByName implements SearchStrategy{

	/**
	 * Conduct a sequential search of an ingredient given its name
	 * 
	 * @param name Name of the ingredient
	 * @param file File to search
	 * 
	 * @return new IngredientInfo object
	 */
    @Override 
    public Ingredient searchForIngredient(String name, File file) throws IOException{

		// Initialize a ReadStrategy and parse the file. This can be done dynamically later
        ReadStrategy strategy = new CSVReader();
        ReadContext context = new ReadContext();
        context.setReadStrategy(strategy);
        List<String[]> csvData = new ArrayList<>(context.parseFile(file));
		
		// Remove header row
		csvData.remove(0);
        
		// Find the row containing the name of the ingredient
        String[] targetRow = findTargetRow(csvData, name);

		// Parse the target row for information to construct the IngredientInfo object
		String ingredientName = targetRow[Constants.NAME_COL_INDEX];
		int calories = Integer.parseInt(targetRow[Constants.CALORIE_INDEX]);
		double protein = Double.parseDouble(targetRow[Constants.PROTEIN_INDEX]);
		double carbohydrates = Double.parseDouble(targetRow[Constants.CARB_INDEX]);
		double fiber = Double.parseDouble(targetRow[Constants.FIBER_INDEX]);
		double saturatedFat = Double.parseDouble(targetRow[Constants.SAT_FAT_INDEX]);
		double monounsaturatedFat = Double.parseDouble(targetRow[Constants.MONO_FAT_INDEX]);
		double polyunsaturatedFat = Double.parseDouble(targetRow[Constants.POLY_FAT_INDEX]);

		return new Ingredient(ingredientName, calories, saturatedFat, monounsaturatedFat, polyunsaturatedFat,
			protein, fiber, carbohydrates);
    }

	private String[] findTargetRow(List<String[]> data, String name) {
		for(String[] row: data) {
			if(row[Constants.NAME_COL_INDEX].equals(name)) {
				return row;
			}
		}

		return null;
	}
}
