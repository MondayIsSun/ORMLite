package mr.lmd.personal.ormliteapplication;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import java.sql.SQLException;
import java.util.List;

import mr.lmd.personal.ormliteapplication.ORMLiteABC.DatabaseHelper;
import mr.lmd.personal.ormliteapplication.ORMLiteABC.User;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() {
        super(Application.class);
    }

    public void testAddUser() {
        User u1 = new User("ORMLite1", "2B青年");
        DatabaseHelper helper = DatabaseHelper.getHelper(getContext());
        try {
            helper.getUserDao().create(u1);
            u1 = new User("ORMLite2", "2B青年");
            helper.getUserDao().create(u1);
            u1 = new User("ORMLite3", "2B青年");
            helper.getUserDao().create(u1);
            u1 = new User("ORMLite4", "2B青年");
            helper.getUserDao().create(u1);
            u1 = new User("ORMLite5", "2B青年");
            helper.getUserDao().create(u1);
            u1 = new User("ORMLite6", "2B青年");
            helper.getUserDao().create(u1);

            testList();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void testDeleteUser() {
        DatabaseHelper helper = DatabaseHelper.getHelper(getContext());
        try {
            helper.getUserDao().deleteById(2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void testUpdateUser() {
        DatabaseHelper helper = DatabaseHelper.getHelper(getContext());
        try {
            User u1 = new User("ORMLite-android", "2B青年");
            u1.setId(3);
            helper.getUserDao().update(u1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void testList() {
        DatabaseHelper helper = DatabaseHelper.getHelper(getContext());
        try {
            User u1 = new User("ORMLite-android", "2B青年");
            u1.setId(2);
            List<User> users = helper.getUserDao().queryForAll();
            for (User u : users) {
                Log.e("ELSeed", "Name = " + u.getName() + " , Desc = " + u.getDesc());
            }
            Log.e("ELSeed", users.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}