/**
 * Filename: SearchContext.java
 * Description: Class for managing database searching context
 * GoF Pattern: Strategy
 * GoF Role: Context
 * @author Peter Carbone pjc7686
 */

package Database.Processing.Searching;

import java.io.File;
import java.io.IOException;

import Recipes.Ingredient;

public class SearchContext {
    private SearchStrategy searcher;

    public void setIngredientSearcher(SearchStrategy searcher) {
        this.searcher = searcher;
    }

    public Ingredient findIngredient(String name, File file) throws IOException {
        return searcher.searchForIngredient(name, file);
    }
}
