package Team;

import java.io.IOException;
import java.util.ArrayList;

import Accounts.Registered_User;

public class Team implements Member_network{

    public String teamname;
    private Team_iterator updater;

    /**
     * Creates a team and a iterator for said team,
     *  automatically adds team creator to team
     * @param name creators name
     * @param creator creators account
     */
    public Team(String name, Registered_User creator){
        teamname = name;
        create_Member_Iterator(creator);
    }

    /**
     * Creates iterator for team
     *
     * @param creator the teams creator
     */
    public void create_Member_Iterator(Registered_User creator) {
        updater = new Member_iterator(creator);
    }
    
    /**
     * returns the list of members
     *
     */
    public ArrayList<Registered_User> get_members(){
        return updater.get_Members();
    }

    /**
     * Issues challenge to all team members
     *
     * @param challenge the challenge
     */
    public void issue_Challenge(String challenge){
        updater.issue_Challenge(challenge);
    }

    /**
     * removes a user from team
     *
     * @param member the user to be removed
     */
    public void remove_Member(Registered_User member){
        updater.leave_Team(member);
    }

    /**
     * adds a User to team
     *
     * @param user a registered user that was invited and accepted the invite
     * @throws IOException
     */
    public void add_Member(Registered_User user) throws IOException{
        updater.add_member(user);
    }

    /**
     * updates the list of members and the iterator
     *
     * @param team the iterator
     */
    public void update(Team_iterator team){
        updater = team;
    }
    

    /**
     * notifies team of workout completion
     *
     * @param notification sends a notification to teammembers that user completed a workout
     */
    public void notify_workout(String notification){
        updater.update_Members(notification);
    }

    /**
     * returns teamname
     */
    public String get_Name(){
        return teamname;
    }
}
