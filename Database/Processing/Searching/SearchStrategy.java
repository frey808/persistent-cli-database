/**
 * Filename: SearchStrategy.java
 * Description: Interface defining searchForIngredient method
 * GoF Pattern: Strategy
 * GoF Role: Strategy
 * @author Peter Carbone pjc7686
 */

package Database.Processing.Searching;

import java.io.File;
import java.io.IOException;

import Recipes.Ingredient;

public interface SearchStrategy {
    Ingredient searchForIngredient(String name, File file) throws IOException;
}