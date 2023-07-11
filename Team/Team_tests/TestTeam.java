package Team.Team_tests;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import Accounts.Registered_User;
import Workout.History.History;
import Workout.History.PersonalHistory;
import Workout.History.Workout;

public class TestTeam {

    private Registered_User User1;

    @Before
    public void initialize() throws Exception {
        this.User1 = new Registered_User("Ayden Boyko", "12345", 60, 137, "11-24-2003", "maintain");
        User1.create_Team("Team 1");
    }

    @Test
    public void test_Create_Team(){
        Assert.assertEquals(User1.get_TeamName(), "Team 1");
    }

    @Test
    public void test_Leave_Team(){
        User1.leave_Team();
        Assert.assertEquals(User1.has_Team(), false);
    }

    @Test
    public void test_invite_user() throws Exception{
        Registered_User User2 = new Registered_User("Person 2", "12345", 60, 137, "11-24-2003", "maintain");
        User1.send_invite(User2);
        User2.accept_invite("Ayden Boyko");
        ArrayList<Registered_User> list = new ArrayList<>();
        list.add(User1);
        list.add(User2);
        Assert.assertEquals(User1.get_Team().get_members(), list);
    }

    @Test
    public void test_Issue_challenge() throws Exception{
        Registered_User User2 = new Registered_User("Person 2", "12345", 60, 137, "11-24-2003", "maintain");
        User1.send_invite(User2);
        User2.accept_invite("Ayden Boyko");
        User1.issue_Challenge("Do 100 pushups");
        Assert.assertEquals(User1.get_Challenge() , "Do 100 pushups");
        Assert.assertEquals(User2.get_Challenge() , "Do 100 pushups");
    }

    @Test
    public void test_view_invite() throws Exception{
        Registered_User User2 = new Registered_User("Person 2", "12345", 60, 137, "11-24-2003", "maintain");
        User1.send_invite(User2);
        ArrayList<String> testlist = new ArrayList<>();
        testlist.add(User1.get_TeamName());
        Assert.assertEquals(testlist, User2.view_invites());
    }

    @Test
    public void test_get_workout_notifications() throws Exception{
        Registered_User User2 = new Registered_User("Person 2", "12345", 60, 137, "11-24-2003", "maintain");
        User1.send_invite(User2);
        User2.accept_invite("Ayden Boyko");
        User2.start_Workout();
        User2.finish_Workout("deez", "easy");
        ArrayList<String> notifications  = new ArrayList<>();
        notifications.add("Person 2 has finished a easy intensity Workout!");
        Assert.assertEquals(notifications, User1.get_Notifications());
    }

    
    @Test
    public void test_view_member_workout_history() throws Exception{
        Registered_User User2 = new Registered_User("Person 2", "12345", 60, 137, "11-24-2003", "maintain");
        User1.send_invite(User2);
        User2.accept_invite("Ayden Boyko");
        User2.start_Workout();
        User2.finish_Workout("deez", "easy");
        Workout expected1 = new Workout("deez", 0, "easy");
        History<Workout> expected = new History<Workout>();
        expected.add(expected1, new Date());
        History<Workout> result = User1.get_TeamMate_Workout_History("Person 2");
        Assert.assertEquals(expected.toString(), result.toString());
    }
}
