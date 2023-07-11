package Accounts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

import Constants.*;
import Recipes.Meal;
import Recipes.Recipe;
import Workout.History.History;
import Workout.History.PersonalHistory;
import Workout.History.Record;

public class Guest implements Account{

    /**
     * creats geust account
     */

	private PersonalHistory personalHistory;

    public Guest() {
		this.personalHistory = new PersonalHistory("Guest");
	}

    /**
	 * browse list of ingredients
	 */
    @Override
    public void Browse_Ingredients() throws FileNotFoundException {
        System.out.println(this.personalHistory.getShoppingList().toString());
    }

    /**
	 * Browse list of recipes
	 */
    @Override
    public void Browse_Recipes() {
		File file = new File(Constants.FOOD_PATH + Constants.SEPARATOR + "Recipes");
		File[] files = file.listFiles();
		for(File f: files) {
			String headers;
			String line;
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
				headers = br.readLine();
				while((line = br.readLine()) != null){
					String[] row = line.split(",");
					for(String str: row) {
						System.out.print(str + " ");
					}
					System.out.println();
				}
			} catch (Exception e){
				System.out.println(e);
			}
		}
    }

    /**
	 * browse list of meals
	 */
    @Override
    public void Browse_Meals() {
        File file = new File(Constants.FOOD_PATH + Constants.SEPARATOR + "Meals");
		File[] files = file.listFiles();
		for(File f: files) {
			String headers;
			String line;
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
				headers = br.readLine();
				while((line = br.readLine()) != null){
					String[] row = line.split(",");
					for(String str: row) {
						System.out.print(str + " ");
					}
					System.out.println();
				}
			} catch (Exception e){
				System.out.println(e);
			}
		}
    }

	public void browseDatabase() {
		String file = "Database\\ingredients.csv";
        String headers;
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            headers = br.readLine();
            while((line = br.readLine()) != null){
                String[] array = line.split(",");
                System.out.println(array[0] + " " + array[1]);
            }
        } catch (Exception e) {

		}
	}

    /**
     * tostring
     */
    @Override
    public String toString(){
        return "A Guest Account";
    }
    
}
