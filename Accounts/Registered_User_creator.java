package Accounts;

public class Registered_User_creator implements AccountCreator{

    /**
     * creates a registered user factory
     */
    public Registered_User_creator(){}

    /**
     * creates a new registered user account
     */
    @Override
    public Account createAccount(String name, String password, Integer Height, Integer Weight, String dobDate, String goal) {
        return new Registered_User(name, password, Height, Weight, dobDate, goal);
    }
    
}
