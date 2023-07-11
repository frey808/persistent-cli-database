package Accounts;

public class Guest_creator implements AccountCreator{

    /**
     * creates a guest factory
     */
    public Guest_creator(){}

    /**
     * creates a guest account
     */
    @Override
    public Account createAccount(String name, String password, Integer Height, Integer Weight, String dobDate,String goal) {
        return new Guest();
    }
    
}
