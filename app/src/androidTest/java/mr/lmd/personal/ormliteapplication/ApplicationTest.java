package mr.lmd.personal.ormliteapplication;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import mr.lmd.personal.ormliteapplication.ORMLiteABC.DatabaseHelper;
import mr.lmd.personal.ormliteapplication.ORMLiteABC.User;
import mr.lmd.personal.ormliteapplication.template.bean.Article;
import mr.lmd.personal.ormliteapplication.template.bean.Student;
import mr.lmd.personal.ormliteapplication.template.dao.ArticleDao;
import mr.lmd.personal.ormliteapplication.template.dao.UserDao;
import mr.lmd.personal.ormliteapplication.template.utils.L;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {

    public ApplicationTest() {
        super(Application.class);
    }

    /********************************ABC目录测试********************************/

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

    /********************************Template目录测试********************************/

    public void testAddArticle() {
        mr.lmd.personal.ormliteapplication.template.bean.User u = new mr.lmd.personal.ormliteapplication.template.bean.User();
        u.setName("林明道");
        new UserDao(getContext()).add(u);
        Article article = new Article();
        article.setTitle("ORMLite的使用");
        article.setUser(u);
        new ArticleDao(getContext()).add(article);
    }

    public void testGetArticleById() {
        Article article = new ArticleDao(getContext()).get(1);
        L.e(article.getUser() + " , " + article.getTitle());
    }

    public void testGetArticleWithUser() {
        Article article = new ArticleDao(getContext()).getArticleWithUser(1);
        L.e(article.getUser() + " , " + article.getTitle());
    }

    public void testListArticlesByUserId() {
        List<Article> articles = new ArticleDao(getContext()).listByUserId(1);
        L.e(articles.toString());
    }

    public void testGetUserById() {
        mr.lmd.personal.ormliteapplication.template.bean.User user = new UserDao(getContext()).get(1);
        L.e(user.getName());
        if (user.getArticles() != null)
            for (Article article : user.getArticles()) {
                L.e(article.toString());
            }
    }

    public void testAddStudent() throws SQLException {
        Dao dao = DatabaseHelper.getHelper(getContext()).getDao(Student.class);
        Student student = new Student();
        student.setDao(dao);
        student.setName("林明道");
        student.create();
    }

}