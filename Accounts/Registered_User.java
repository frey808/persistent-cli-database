package Accounts;

import java.io.*;
import java.nio.file.*;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.lang.model.type.UnknownTypeException;

import org.junit.Assert;

import Constants.Constants;
import Database.Conversion.CSV.CSVToJSON;
import Database.Conversion.CSV.CSVToJSONConverter;
import Database.Conversion.CSV.CSVToXML;
import Database.Conversion.CSV.CSVToXMLConverter;
import Database.Conversion.JSON.JSONAdapter;
import Database.Conversion.JSON.JSONToCSV;
import Database.Conversion.JSON.JSONToCSVConverter;
import Database.Conversion.XML.XMLToCSV;
import Database.Conversion.XML.XMLToCSVConverter;
import Database.Processing.Reading.*;
import Database.Processing.Writing.*;
import Goals.GoalTypes;
import Goals.Goals;
import Recipes.*;
import Team.Team;
import Team.Team_iterator;
import Workout.History.History;
import Workout.History.PersonalHistory;
import Workout.History.Workout;
import Workout.History.Intensity;

public class Registered_User implements Account{

    public String User_name;
    private String password;
    public Integer User_Height;
    public Integer User_Starting_Weight;
    public Integer User_Current_Weight;
    private Date User_Dob;
	private String birthday;
    private Goals User_Goal;
    public Team Team;
    private ArrayList<Registered_User> invites;
    private String challenge;
    private PersonalHistory personalHistory;
    private long workoutlength;
    private ArrayList<String> notifications;
    public Date current_Date;

	/**
	 * Creates a user, this constructor is used for first time setup 
	 * 
	 * @param name Username
	 * @param password Password
	 * @param Height Users height
	 * @param Weight Users weight
	 * @param dobDate Users birthday
	 * @param goalName Users goalName
	 * 
	 * @throws Exception If something goes wrong
	 */
    public Registered_User(String name, String password, Integer Height, Integer Weight, String dobDate, String goalName) {
        this.User_name = name;
		this.password = password;
        this.User_Height = Height;
        this.User_Starting_Weight = Weight;
        this.User_Current_Weight = Weight;
		this.birthday = dobDate;
		this.personalHistory = new PersonalHistory(name);

        String[] mdy = dobDate.split("[ /-]");
        init_Birthday(Integer.parseInt(mdy[2]), Integer.parseInt(mdy[0]), Integer.parseInt(mdy[1]));
        this.User_Goal = new Goals(goalName, Weight);

        this.invites = new ArrayList<>();
        this.notifications  = new ArrayList<>();
       
        this.current_Date = new Date();
        
         // Write the account information to a new file if the account doesn't exist
         if(!Files.exists(Paths.get(Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + this.get_User_Name()))) {
			File folder = new File(Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + this.User_name);
			folder.mkdir();
            createAccountInfo();
			createWorkoutInfo();
        }
		else {
			loadIngredientInfo();
			loadWorkoutInfo();
			loadRecipeInfo();
			loadMealInfo();
		}
    }

	/**
     * Creates a user by pulling their existing account information. 
     * 
     * @param username Username to search for
     * @param password Account password
     */
    public Registered_User(String username, String password) {
        
        // Get the filename for the user
        String filename = Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + username + Constants.SEPARATOR + "info" + Constants.CSV;
		
        try {
            ReadStrategy strategy = new CSVReader();
            ReadContext context = new ReadContext();
            context.setReadStrategy(strategy);
            List<String[]> userData = strategy.readFile(new File(filename));
			userData.remove(0);
            
            String[] storedUserData = userData.get(0);

            int height = Integer.parseInt(storedUserData[2]);
            int startingWeight = Integer.parseInt(storedUserData[3]);
            int currentWeight = Integer.parseInt(storedUserData[4]);
            String birthday = storedUserData[5];
            String goalName = storedUserData[6];

			this.User_name = username;
			this.password = password;
			this.User_Height = height;
			this.User_Starting_Weight = startingWeight;
			this.User_Current_Weight = currentWeight;
			this.password = password;
			this.personalHistory = new PersonalHistory(username);
			this.invites = new ArrayList<>();
			String[] mdy = birthday.split("[ /-]");
			init_Birthday(Integer.parseInt(mdy[2]), Integer.parseInt(mdy[0]), Integer.parseInt(mdy[1]));
			this.User_Goal = new Goals(goalName, startingWeight);

			loadWorkoutInfo();
			loadIngredientInfo();
			loadRecipeInfo();
			loadMealInfo();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }

	/**
	 * Export the database and user information to discrete files
	 * 
	 * @param fileType Type of files to export to (csv, json, xml)
	 * @param path Path to export to
	 */
	public void exportDatabase(String fileType, String path) {
		String ingredientDatabaseFileName = "ingredients";
		String ingredientFileName = "stock";
		String workoutFileName = "workouts";
		File mealFolder = new File(Constants.FOOD_PATH + Constants.SEPARATOR + "Meals");

		if(fileType.equals("csv")) {
			ingredientDatabaseFileName += ".csv";
			ingredientFileName += ".csv";
			workoutFileName += ".csv";

			List<String[]> data = readFileInfo(new File(Constants.INGREDIENT_DATABASE_PATH));
			writeFileInfo(new File(path + "+ ingredients.csv"), data);
			data = readFileInfo(new File(Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + User_name + Constants.SEPARATOR + "workouts.csv"));
			writeFileInfo(new File(path + "+ workouts.csv"), data);
			data = readFileInfo(new File(Constants.FOOD_PATH + Constants.SEPARATOR + "stock" + Constants.CSV));
			writeFileInfo(new File(path + "+ stock.csv"), data);

			File[] files = mealFolder.listFiles();
			for(File file: files) {
				List<String[]> mealData = readFileInfo(file);
				writeFileInfo(new File(path + file.getName()), mealData);
			}
		}
		else if(fileType.equals("json")) {
			ingredientDatabaseFileName += ".json";
			ingredientFileName += ".json";
			workoutFileName += ".json";
			CSVToJSONConverter converter = new CSVToJSONConverter(new CSVToJSON());
			List<String[]> data = readFileInfo(new File(Constants.INGREDIENT_DATABASE_PATH));
			converter.export(data, path + ingredientDatabaseFileName);
			data = readFileInfo(new File(Constants.FOOD_PATH + Constants.SEPARATOR + "stock" + Constants.CSV));
			converter.export(data, path + ingredientFileName);
			data = readFileInfo(new File(Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + User_name + Constants.SEPARATOR + "workouts.csv"));
			converter.export(data, path + workoutFileName);

			File[] files = mealFolder.listFiles();
			for(File file: files) {
				List<String[]> mealData = readFileInfo(file);
				converter.export(mealData, path + file.getName() + ".json");
			}
		}
		else if(fileType.equals("xml")) {
			ingredientDatabaseFileName += ".xml";
			ingredientFileName += ".xml";
			workoutFileName += ".xml";

			CSVToXMLConverter converter = new CSVToXMLConverter(new CSVToXML());
			List<String[]> data = readFileInfo(new File(Constants.INGREDIENT_DATABASE_PATH));
			converter.export(data, path + ingredientDatabaseFileName);
			data = readFileInfo(new File(Constants.FOOD_PATH + Constants.SEPARATOR + "stock" + Constants.CSV));
			converter.export(data, path + ingredientFileName);
			data = readFileInfo(new File(Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + User_name + Constants.SEPARATOR + "workouts.csv"));
			converter.export(data, path + workoutFileName);

			File[] files = mealFolder.listFiles();
			for(File file: files) {
				List<String[]> mealData = readFileInfo(file);
				converter.export(mealData, path + file.getName() + ".xml");
			}
		}
	}

	/**
	 * Import a file to be parsed and stored in the database
	 * 
	 * @param path Path to file
	 * @param type meal, workouts, ingredients, stock
	 * @param fileType Type of file (csv, json, or xml)
	 */
	public void importFile(String path, String type, String fileType) {
		File file = new File(path);

		if(fileType.equals("csv")) {
			List<String[]> data = readFileInfo(new File(path));
			if(type.equals("meal")) {
				writeMeal(data, path);
			}
			else if(type.equals("workouts")) {
				List<String[]> workoutData = readFileInfo(new File(Constants.ACCOUNT_INFO_PATH + 
					Constants.SEPARATOR + User_name + Constants.SEPARATOR + "workouts.csv"));
				for(String[] row: data) {
					workoutData.add(row);
				}
				writeFileInfo(new File(Constants.ACCOUNT_INFO_PATH + 
					Constants.SEPARATOR + User_name + Constants.SEPARATOR + "workouts.csv"), workoutData);
			}
			else if(type.equals("ingredients")) {
				List<String[]> ingredientData = readFileInfo(new File(Constants.INGREDIENT_DATABASE_PATH));
				for(String[] row: data) {
					ingredientData.add(row);
				}
				writeFileInfo(new File(Constants.ACCOUNT_INFO_PATH + 
					Constants.SEPARATOR + User_name + Constants.SEPARATOR + "ingredients.csv"), ingredientData);
			}
			else if(type.equals("stock")) {
				List<String[]> stockData = readFileInfo(new File(Constants.FOOD_PATH + Constants.SEPARATOR 
					+  "stock.csv"));
				for(String[] row: data) {
					stockData.add(row);
				}
				writeFileInfo(new File(Constants.ACCOUNT_INFO_PATH + 
					Constants.SEPARATOR + User_name + Constants.SEPARATOR + "stock.csv"), stockData);
			}
		}
		else if(fileType.equals("json")) {
			JSONToCSVConverter converter = new JSONToCSVConverter(new JSONToCSV());
			if(type.equals("meal")) {
				converter.importData(path, Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + User_name + Constants.SEPARATOR + 
					file.getName() + ".csv");
			}
			else if(type.equals("workouts")) {
				converter.importData(path, Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + User_name + Constants.SEPARATOR + 
					User_name + "workouts.csv");
			}
			else if(type.equals("ingredients")) {
				converter.importData(path, Constants.INGREDIENT_DATABASE_PATH);
			}
			else if(type.equals("stock")) {
				converter.importData(path, Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + User_name + Constants.SEPARATOR + 
					User_name + "ingredients.csv");
			}
		}
		else if(fileType.equals("xml")) {
			XMLToCSVConverter converter = new XMLToCSVConverter(new XMLToCSV());
			if(type.equals("meal")) {
				converter.importData(path, Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + User_name + Constants.SEPARATOR + 
					file.getName() + ".csv");
			}
			else if(type.equals("workouts")) {
				converter.importData(path, Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + User_name + Constants.SEPARATOR + 
					"workouts.csv");
			}
			else if(type.equals("ingredients")) {
				converter.importData(path, Constants.INGREDIENT_DATABASE_PATH);
			}
			else if(type.equals("stock")) {
				converter.importData(path, Constants.FOOD_PATH + Constants.SEPARATOR + "stock" + Constants.CSV);
			}
		}
	}

	/**
	 * Write meal info to a csv file
	 * 
	 * @param data Data to write
	 * @param path Path to write to
	 */
	private void writeMeal(List<String[]> data, String path) {
		Meal meal = new Meal(path);
		for(int i = 0; i < data.size() - 1; i++) {
			Recipe recipe = new Recipe(data.get(i)[1]);
			if(data.get(i + 1)[1].equals(recipe.getName())) {
				continue;
			}

			int j = i;
			while(data.get(i)[1].equals(data.get(j)[1])) {
				Ingredient food = new Ingredient(data.get(j)[3], data.get(j)[5]);
				recipe.addFood(food, Integer.parseInt(data.get(j)[4]));
				j++;
			}
			meal.addRecipe(recipe, Integer.parseInt(data.get(i)[3]));
		}

		for(String[] row: data) {
			Recipe recipe = new Recipe(row[2]);
			for(int i = 0; i < Integer.parseInt(row[1]); i++) {
						
			}
		}	
		this.view_History().getMeals().add(meal, current_Date);
	}

    /**
     * Create a Location for the user's accounts, then create and write separate files for everything they need
     */
    private void createAccountInfo()  {
		// Add header to the user's csv file
        List<String[]> accountInfo = new ArrayList<>();
        accountInfo.add(Constants.REGISTERED_USER_HEADER);
        String[] accountFields = new String[Constants.NUM_USER_FIELDS];
        accountFields[0] = this.get_User_Name();
        accountFields[1] = Base64.getEncoder().encodeToString(this.password.getBytes());
        accountFields[2] = Integer.toString(this.getHeight());
        accountFields[3] = Integer.toString(this.getStartingWeight());
        accountFields[4] = Integer.toString(this.get_Weight());
        accountFields[5] = this.birthday;
        accountFields[6] = this.User_Goal.toString();
		accountFields[7] = "None";
		accountFields[8] = " \"None\" ";
        accountInfo.add(accountFields);

        // Create a new csv file for the user
        File accountFile = new File(Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + 
			this.get_User_Name() + Constants.SEPARATOR + "info" + Constants.CSV);
		writeFileInfo(accountFile, accountInfo);
    }

	/**
	 * Load this users' ingredient information
	 */
	private void loadIngredientInfo() {
		File ingredientFile = new File(Constants.FOOD_PATH + Constants.SEPARATOR + "stock" + Constants.CSV);
		try {
			ReadStrategy strategy = new CSVReader();
			ReadContext context = new ReadContext();
			context.setReadStrategy(strategy);
			List<String[]> userData = strategy.readFile(ingredientFile);
			userData.remove(0);
			for(String[] row: userData) {
				this.view_History().addIngredient(row[0], Integer.parseInt(row[8]));
			}
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Create the file to store this user's workout information
	 */
	private void createWorkoutInfo() {	
		File workoutFile = new File(Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + 
			this.get_User_Name() + Constants.SEPARATOR + "workouts" + Constants.CSV);
		List<String[]> workoutInfo = new ArrayList<>();
		workoutInfo.add(Constants.WORKOUT_HEADER);
		writeFileInfo(workoutFile, workoutInfo);
	}

	/**
	 * Load this users' workout information
	 */
	private void loadWorkoutInfo() {
		File workoutFile = new File(Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + 
			this.get_User_Name() + Constants.SEPARATOR + "workouts" + Constants.CSV);
		try {
			ReadStrategy strategy = new CSVReader();
			ReadContext context = new ReadContext();
			context.setReadStrategy(strategy);
			List<String[]> userData = strategy.readFile(workoutFile);
			userData.remove(0);
			for(String[] row: userData) {
				personalHistory.updateWorkouts(new Workout(row[0], Integer.parseInt(row[1]), row[2]));
			}
		}

		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Loads the users' meals into their personal history
	 */
	private void loadMealInfo() {
		File mealLocation = new File(Constants.FOOD_PATH + Constants.SEPARATOR + "Meals");
		File[] meals = mealLocation.listFiles();

		for(File file: meals) {
			// Get the meal data for each file in Database\Food\Meals
			List<String[]> mealData = readFileInfo(file);

			// Remove header row
			mealData.remove(0);
			
			if (mealData.size() != 0) {
				Meal meal = new Meal(mealData.get(0)[0]);
				for(int i = 0; i < mealData.size() - 1; i++) {
					Recipe recipe = new Recipe(mealData.get(i)[1]);
					if(mealData.get(i + 1)[1].equals(recipe.getName())) {
						continue;
					}

					int j = i;
					while(mealData.get(i)[1].equals(mealData.get(j)[1])) {
						Ingredient food = new Ingredient(mealData.get(j)[3], mealData.get(j)[5]);
						recipe.addFood(food, Integer.parseInt(mealData.get(j)[4]));
						j++;
					}
					meal.addRecipe(recipe, Integer.parseInt(mealData.get(i)[4]));
				}
				this.view_History().getMeals().add(meal, current_Date);
			}
			else{
				
			}
			
		}
	}

	private void loadRecipeInfo() {
		File recipeLocation = new File(Constants.FOOD_PATH + Constants.SEPARATOR + "Recipes");
		File[] recipes = recipeLocation.listFiles();
		Assert.assertNotNull(recipes);

		for(File file: recipes) {
			List<String[]> recipeData = readFileInfo(file);
				
			// Remove header row
			recipeData.remove(0);
			
			Recipe recipe = new Recipe(recipeData.get(0)[0]);
			for(int i = 0; i < recipeData.size(); i++) {
				Ingredient food = new Ingredient(recipeData.get(i)[1], recipeData.get(i)[3]);
				recipe.addFood(food, Integer.parseInt(recipeData.get(i)[2]));
			}
			this.view_History().getRecipes().add(recipe, current_Date);
		}
	}

	/**
	 * Utility method to create file info for a given user
	 * 
	 * @param file File to create
	 * @param data Data to write
	 */
	private void writeFileInfo(File file, List<String[]> data) {
		try {
            WriteStrategy strategy = new CSVWriter();
            WriteContext context = new WriteContext();
            context.setWriteStrategy(strategy);
            strategy.writeFile(file, data);
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
	}

	/**
	 * Change this users' password
	 * 
	 * @param newPassword New password to change
	 * 
	 * @throws Exception If something goes wrong during the encryption process
	 */
	public void changePassword(String newPassword) throws Exception {
		String encodedPassword = Base64.getEncoder().encodeToString(newPassword.getBytes());
		String cleanedPassword = encodedPassword.replaceAll("[^a-zA-Z0-9+/=]", "");
		this.password = cleanedPassword;
		writeNewPassword();
	}

	/**
	 * Write a user's new password to their file. This method only exists for the password because
	 * according to the requirements it is the only part the user is allowed to change
	 */
	private void writeNewPassword() {
		String accountFile = Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR +
				this.User_name + Constants.SEPARATOR + "info" + Constants.CSV;

		List<String[]> userData = readFileInfo(new File(accountFile));
		String[] data = userData.remove(1);
		data[1] = this.password;
		userData.add(data);
		writeFileInfo(new File(accountFile), userData);
	}

	/**
	 * Read a csv file and return its contents
	 * 
	 * @param file File to read
	 * 
	 * @return 
	 */
	private List<String[]> readFileInfo(File file) {
		List<String[]> data;
		try {
			ReadStrategy strategy = new CSVReader();
			ReadContext context = new ReadContext();
			context.setReadStrategy(strategy);
			data = strategy.readFile(file);

			// Remove header row before returning
			return data;
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
		return null;
	}

	/**
	 * converts the users entered dob info to a date
	 * @param year the yob
	 * @param month the mob
	 * @param day the dob
	 */
    private void init_Birthday(int year, int month, int day) {
        if (year > 0 && month > 0 && day > 0) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month-1 , day, 0, 0, 0);
            set_Dob(cal.getTime());
        }
    }

	/**
	 * updates the users weight
	 * 
	 * @param amount new weight
	 */
    public void update_Weight(int amount){
        User_Current_Weight += amount;
        if (User_Starting_Weight <= User_Current_Weight-5){
            User_Starting_Weight = User_Current_Weight;
            User_Goal.updateGoals(personalHistory);;
        }
        else if (User_Starting_Weight >= User_Current_Weight+5){
            User_Starting_Weight = User_Current_Weight;
            User_Goal.updateGoals(personalHistory);;
        }
        personalHistory.updateWeight(User_Current_Weight);
    }

	/**
	 * starts the workout
	 */
    public void start_Workout(){
        this.workoutlength = System.currentTimeMillis();
    }

	/**
	 * finishes the workout
	 * @param name name of the workout
	 * @param intenisty the intensity of the workout                                                                                                                                            
	 */
    public void finish_Workout(String name, String intenisty){
        long finishtime = System.currentTimeMillis();
        int time = (int)((finishtime-workoutlength) / 1000);
        personalHistory.updateWorkouts(new Workout(name, time, intenisty));
        if (Team != null){
            String message = User_name + " has finished a " + intenisty + " intensity Workout!";
            Team.notify_workout(message);
        }
    }
    
	/**
	 * new day elapses
	 */
    public void new_day(){
        Date newday = new Date();
        if (newday.compareTo(current_Date) > 0){
            this.current_Date = newday;
        }
    }

	/**
	 * @return the entire personal history
	 */
    public PersonalHistory view_History(){
        return this.personalHistory;
    }

	/**
	 * adds a notification to the list
	 * @param notification notification to be added
	 */
    public void recieve_notification(String notification){
        notifications.add(notification);
    }
    
	/**
	 * sets dob
	 * @param dob birthday
	 */
    private void set_Dob(Date dob){
        this.User_Dob = dob;
    }

	/**
	 * gets whether the user is in a team
	 * @return wether the user is in a team
	 */
    public boolean has_Team(){
        return Team != null;
    }

	/**
	 * sets the users weekly challenge
	 * @param challenge the challenge to be issued
	 */
    private void set_Challenge(String challenge){
        this.challenge = challenge;
    }
    
	/**
	 * creates a team and updates the csv info file
	 * @param name teamname
	 * @throws IOException
	 */
    public void create_Team(String name) throws IOException{
        Team = new Team(name, this);
		CSVReader reader = new CSVReader();
		File userfile = new File(Constants.ACCOUNT_INFO_PATH + "\\" + this.User_name + "\\" + "info" + Constants.CSV);
		List<String[]>userdata = reader.readFile(userfile);
		String updated_userdata[] = userdata.get(1);
		updated_userdata[7] = name;
		updated_userdata[8] = " \"" + this.User_name + "\"";
		userdata.remove(1);
		userdata.add(1, updated_userdata);
		writeFileInfo(userfile, userdata);
    }

	/**
	 * invites a user
	 * @param User user to b invited
	 */
    public void send_invite(Registered_User User){
        User.get_invite(this);
    }

	/**
	 * rejects an invite
	 * @param username user who's invite will be rejected
	 */
    public void reject_invite(String username){
        int i = 0;
        for (i = 0; invites.get(i).get_User_Name() != username; i++);
        if (invites.get(i).get_User_Name() == username){
            invites.remove(i);
        }
    }

	/**
	 * rejects all invites
	 */
    public void reject_all_invites(){
        for (int i = 0; !invites.isEmpty(); i++){
			invites.remove(i);
		}
    }

	/**
	 * accepts invite and adds the team and its memebrs to the csv info file
	 * @param username user who's invite will be accepted
	 */
    public void accept_invite(String username) throws IOException{
        int i = 0;
		for (i = 0; invites.get(i).get_User_Name() != username; i++){
			if (invites.get(i+1).get_User_Name() == username){
				File userfile = new File(Constants.ACCOUNT_INFO_PATH + "\\" + username + "\\" + "info" + Constants.CSV);
				CSVReader reader = new CSVReader();
				List<String[]> userdata = reader.readFile(userfile);
				Team = invites.get(i+1).get_Team();
				invites.remove(i+1);
				Team.add_Member(this);
				String members = userdata.get(2)[8];
				members = members.substring(0, members.length() - 2);
				members += " \"" + this.User_name + "\"";
				userfile = new File(Constants.ACCOUNT_INFO_PATH + "\\" + this.User_name + "\\" + "info" + Constants.CSV);
				userdata = reader.readFile(userfile);
				String updated_userdata[] = userdata.get(1);
				updated_userdata[8] = members;
				userdata.remove(1);
				userdata.add(1, updated_userdata);
				writeFileInfo(userfile, userdata);
			}
		}
		
    }

	/**
	 * view workout history
	 * @return workout history
	 */
    public History<Workout> get_Workout_History(){
        return personalHistory.getWorkouts();
    }

	/**
	 * views teammates workout history
	 * @param name teammate name
	 * @return workout history
	 */
    public History<Workout> get_TeamMate_Workout_History(String name){
        ArrayList<Registered_User> list = Team.get_members().stream().filter(x->x.get_User_Name().equals(name)).collect(Collectors.toCollection(ArrayList::new));
        return list.get(0).get_Workout_History();
    }
	
	/**
	 * issues challenge to team
	 * @param challenge challenge to be issued
	 */
    public void issue_Challenge(String challenge){
        Team.issue_Challenge(challenge);
    }

	/**
	 * receives challenge
	 * @param Challenge challenge to be recieved
	 */
    public void receive_Challenge(String Challenge){
        set_Challenge(Challenge);
    }

	/**
	 * leaves team
	 */
    public void leave_Team(){
        Team.remove_Member(this);
		Team.update(null);
        this.Team = null;
    }

	/**
	 * updates team and the csv info file
	 * @param update new iterator
	 */
    public void update_team(Team_iterator update) throws IOException{
        Team.update(update);
		CSVReader reader = new CSVReader();
		File userfile = new File(Constants.ACCOUNT_INFO_PATH + "\\" + this.User_name + "\\" + "info" + Constants.CSV);
		List<String[]>userdata = reader.readFile(userfile);
		String updated_userdata[] = userdata.get(1);
		updated_userdata[8] = " \"" + update.get_Members().toString() + "\"";
		userdata.remove(1);
		userdata.add(1, updated_userdata);
		writeFileInfo(userfile, userdata);
    }

	/**
	 * view list of pending invites
	 */
    public ArrayList<String> view_invites(){
        //gets team names from a stream
        return invites.stream().map(x->x.get_TeamName()).collect(Collectors.toCollection(ArrayList::new));
    }

	/**
	 * browse list of ingredients
	 */
    @Override
    public void Browse_Ingredients() throws FileNotFoundException {
		System.out.println(this.personalHistory.getShoppingList().toString());
    }

	/**
	 * browse list of recipes
	 */
    @Override
    public void Browse_Recipes() throws FileNotFoundException {
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
    public void Browse_Meals() throws FileNotFoundException {
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
		String file = "Database" + Constants.SEPARATOR + "ingredients.csv";
        String headers;
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            headers = br.readLine();
            while((line = br.readLine()) != null){
                String[] row = line.split(",");
					for(String str: row) {
						System.out.print(str);
					}
					System.out.println();
            }
        } catch (Exception e){
            System.out.println(e);
        }
	}

	public void dailyOperations(){
		personalHistory.resetCaloriesConsumed();
		User_Goal.updateGoals(personalHistory);
	}

	/**
	 * tostring
	 */
    @Override
    public String toString(){
        return "Username: " + User_name + " User Height: " + User_Height.toString() + " User Starting Weight: " + User_Starting_Weight.toString();
    }

	/**
	 * A bunch of getters
	 * @return return what they asy they do
	 */

	public ArrayList<String> get_Notifications(){
        return notifications;
    }
    
    public int getHeight() {
        return this.User_Height;
    }

	public String get_User_Name(){
        return this.User_name;
    }

    public int getStartingWeight() {
        return this.User_Starting_Weight;
    }

    public int get_Weight(){
        return User_Current_Weight;
    }

    public Goals get_Goal(){
        return User_Goal;
    }

    public Date getDOB() {
        return this.User_Dob;
    }

	public void get_invite(Registered_User invite){
        invites.add(invite);
    }

	public String get_Challenge(){
        return challenge;
    }

	public Team get_Team(){
        return Team;
    }

    public String get_TeamName(){
        return Team.get_Name();
    }

	public PersonalHistory getPersonalHistory(){
		return this.personalHistory;
	}
}
