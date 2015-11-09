package mr.lmd.personal.ormliteapplication.template.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import mr.lmd.personal.ormliteapplication.template.bean.Article;
import mr.lmd.personal.ormliteapplication.template.bean.Student;
import mr.lmd.personal.ormliteapplication.template.bean.User;

/**
 * Created by LinMingDao on 2015/11/6.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    /**
     * 一个数据库下有多张表——>多个对应映射类——>多个DAO
     * 指定要创建的数据库名
     */
    private static final String TABLE_NAME = "sqlite-test.db";

    /**
     * 统一管理多个DAO类
     */
    private Map<String, Dao> mDAOs = new HashMap<>();

    private static DatabaseHelper mInstance;

    private DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 4);
    }

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (mInstance == null) {
            synchronized (DatabaseHelper.class) {
                if (mInstance == null)
                    mInstance = new DatabaseHelper(context);
            }
        }
        return mInstance;
    }

    /**
     * 数据库创建逻辑
     *
     * @param database
     * @param connectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, Article.class);
            TableUtils.createTable(connectionSource, Student.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 数据库升级逻辑
     *
     * @param database
     * @param connectionSource
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase database,
                          ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            /**
             * 如何升级已经有数据的数据库?
             */
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Article.class, true);
            TableUtils.dropTable(connectionSource, Student.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 提供获取相应DAO的接口
     *
     * @param clazz
     * @return
     * @throws SQLException
     */
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (mDAOs.containsKey(className)) {
            dao = mDAOs.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            mDAOs.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        for (String key : mDAOs.keySet()) {
            Dao dao = mDAOs.get(key);
            dao = null;
        }
    }
}
