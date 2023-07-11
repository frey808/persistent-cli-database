package Accounts.Account_tests;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.junit.*;

import Accounts.Guest;
import Accounts.Registered_User;
import Constants.*;
import Database.Processing.Reading.CSVReader;
import Database.Processing.Reading.ReadContext;
import Database.Processing.Reading.ReadStrategy;
import Goals.GoalTypes;

public class TestAccount {
    
    @Test
    public void testCreateRegisteredUser() throws Exception{
        Registered_User User = new Registered_User("Ayden Boyko", "12345", 60, 137, "11-24-2003", "maintain");
        Assert.assertEquals("Username: Ayden Boyko User Height: 60 User Starting Weight: 137", User.toString());
    }

    @Test
    public void testCreateGuestUser(){
        Guest Guest = new Guest();
        Assert.assertEquals("A Guest Account", Guest.toString());
    }

    @Test
    public void testWeightChange() throws Exception{
        Registered_User User = new Registered_User("Ayden Boyko", "12345", 60, 137, "11-24-2003", "maintain");
        int User_Before = User.get_Weight();
        User.update_Weight(2);
        int User_After = User.get_Weight();
        Assert.assertNotEquals(User_After, User_Before);
    }

	@Test
	public void testFilesAreWritten() throws Exception {
		Registered_User user = new Registered_User("Peter", "password", 74, 200, "01-13-2001", "lose");
		
		File accountFile = new File(Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + "Peter" + 
			Constants.SEPARATOR + "info.csv");
		Assert.assertTrue(accountFile.exists());
		
		File workoutFile = new File(Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + "Peter" + 
			Constants.SEPARATOR + "workouts.csv");
		Assert.assertTrue(workoutFile.exists());

		File ingredientFile = new File(Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + "Peter" + 
			Constants.SEPARATOR + "ingredients.csv");
		Assert.assertTrue(ingredientFile.exists());
	}

	@Test
	public void testEncryption() throws Exception {
		Registered_User user = new Registered_User("Peter", "Pass word", 74, 200, "01-13-2001", "lose");

		File accountFile = new File(Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + "Peter" + 
			Constants.SEPARATOR + "info.csv");

		ReadStrategy strategy = new CSVReader();
		ReadContext context = new ReadContext();
		context.setReadStrategy(strategy);
		List<String[]> data = strategy.readFile(accountFile);
		data.remove(0);

		String[] row = data.get(0);
		for(String str: row) {
			System.out.println(str);
		}

		String test = "1234567890";
		System.out.println("Test: " + test);
		String encodedTest = Base64.getEncoder().encodeToString(test.getBytes());
		System.out.println("Encoded test: " + encodedTest);

		byte[] decodedTest = Base64.getDecoder().decode(encodedTest);
		String decodedTestFinal = new String(decodedTest);
		System.out.println("Decoded test: " + decodedTestFinal);


		String encryptedPassword = row[1];
		System.out.println("Encrypted password: " + encryptedPassword);

		byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);

		String password = new String(decodedBytes);
		System.out.println("Decrypted password: " + password);

		Assert.assertEquals("Pass word", password);
	}

	@Test
	public void testChangePassword() throws Exception {
		Registered_User user = new Registered_User("Peter", "password", 74, 200, "01-13-2001", "lose");
		
		File accountFile = new File(Constants.ACCOUNT_INFO_PATH + Constants.SEPARATOR + "Peter" + 
			Constants.SEPARATOR + "info.csv");

		ReadStrategy strategy = new CSVReader();
		ReadContext context = new ReadContext();
		context.setReadStrategy(strategy);
		List<String[]> data = strategy.readFile(accountFile);

		String password = data.get(0)[1].toLowerCase();
		Assert.assertEquals(password, "password");

		user.changePassword("drowssap");
		data = strategy.readFile(accountFile);
		data.remove(0);
		password = new String(Base64.getDecoder().decode(data.get(0)[1]));

		Assert.assertEquals("drowssap", password);
	}

	 @Test
	 public void testExports() throws Exception {
	 	Registered_User user = new Registered_User("Peter", "password", 74, 200, "01-13-2001", "lose");

	 	user.exportDatabase("csv", "C:\\Users\\peter\\Downloads");
	 	user.exportDatabase("json", "C:\\Users\\peter\\Downloads");
	 	user.exportDatabase("xml", "C:\\Users\\peter\\Downloads");

	 	Assert.assertTrue(new File("C:\\Users\\peter\\Downloads\\ingredients.csv").exists());
	 	Assert.assertTrue(new File("C:\\Users\\peter\\Downloads\\workouts.csv").exists());
	 	Assert.assertTrue(new File("C:\\Users\\peter\\Downloads\\ingredients.csv").exists());

	 	Assert.assertTrue(new File("C:\\Users\\peter\\Downloads\\ingredients.json").exists());
	 	Assert.assertTrue(new File("C:\\Users\\peter\\Downloads\\workouts.json").exists());
	 	Assert.assertTrue(new File("C:\\Users\\peter\\Downloads\\ingredients.json").exists());

	 	Assert.assertTrue(new File("C:\\Users\\peter\\Downloads\\ingredients.xml").exists());
	 	Assert.assertTrue(new File("C:\\Users\\peter\\Downloads\\workouts.xml").exists());
	 	Assert.assertTrue(new File("C:\\Users\\peter\\Downloads\\ingredients.xml").exists());
	 }
}
