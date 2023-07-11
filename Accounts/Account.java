package Accounts;

import java.io.FileNotFoundException;

public interface Account {
    public void Browse_Ingredients() throws FileNotFoundException;
    public void Browse_Recipes() throws FileNotFoundException;
    public void Browse_Meals() throws FileNotFoundException;
    public String toString();
}
