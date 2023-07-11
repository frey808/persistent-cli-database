package Team;

import java.io.IOException;
import java.util.ArrayList;

import Accounts.Registered_User;

public interface Team_iterator{
    public void invite_User(Registered_User user);
    public void leave_Team(Registered_User user);
    public ArrayList<Registered_User> get_Members();
    public void issue_Challenge(String challenge);
    public void add_member(Registered_User user) throws IOException;
    public void update_Members(String notification);
}