package Team;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

import Accounts.Registered_User;

public class Member_iterator implements Team_iterator{

    private ArrayList<Registered_User> members;

    /**
     * Creates the iterator for the team, adds the creator to the team
     * 
     * @param creator
     */
    public Member_iterator(Registered_User creator){
        if (creator.has_Team()){
            members = creator.get_Team().get_members();
        }
        else{
            members = new ArrayList<>();
            members.add(creator);
        }
    }

    /**
     * notifies all team members
     * 
     * @param notification the message
     */
    @Override
    public void update_Members(String notification){
        Consumer<Registered_User> consumer = s -> {s.recieve_notification(notification);};
        members.stream().forEach(consumer);
    }

    /**
     * returns a list of all team members
     * 
     */
    @Override
    public ArrayList<Registered_User> get_Members(){
        return members;
    }

    /**
     * invites a user to the team
     * 
     * @param user the user to be invited
     */
    @Override
    public void invite_User(Registered_User user) {
        user.get_invite(user);
    }

    /**
     * issues a challenge to all team members
     * 
     * @param challenge the challenge to be issued
     */
    @Override
    public void issue_Challenge(String challenge) {
        members.stream().forEach(x->x.receive_Challenge(challenge));
    }

    /**
     * user is removed from team
     * 
     * @param user use to be removed
     */
    @Override
    public void leave_Team(Registered_User user) {
        members.remove(user);
        //updates all other members of this change
        members.stream().forEach(x->{
            try {
                x.update_team(this);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }

    /**
     * adds a user to the team
     * 
     * @param user user to be added
     */
    @Override
    public void add_member(Registered_User user) {
        members.add(user);
        //updates all other members of this change
        members.stream().forEach(x->{
            try {
                x.update_team(this);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
    }
    
}
