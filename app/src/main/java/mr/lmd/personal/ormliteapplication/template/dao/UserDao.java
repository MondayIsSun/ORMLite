package mr.lmd.personal.ormliteapplication.template.dao;

import java.sql.SQLException;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import mr.lmd.personal.ormliteapplication.template.bean.User;

public class UserDao {

    private Context mContext;
    private Dao<User, Integer> mUserDao;
    private DatabaseHelper helper;

    public UserDao(Context context) {
        this.mContext = context;
        try {
            helper = DatabaseHelper.getHelper(context);
            mUserDao = helper.getDao(User.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 增加一个用户
     *
     * @param user
     * @throws SQLException
     */
    public void add(User user) {
        /*//事务操作
        TransactionManager.callInTransaction(helper.getConnectionSource(),
				new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						return null;
					}
				});
		*/
        try {
            mUserDao.create(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取用户
     *
     * @param id
     * @return
     */
    public User get(int id) {
        try {
            return mUserDao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
