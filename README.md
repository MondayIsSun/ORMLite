# ORMLite最佳实践

0、题外话：高效的单例模式写法

    private static DatabaseHelper instance;

    private DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 4);
    }

    public static synchronized DatabaseHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) { //第一层判断：先过滤掉大部分情况
            synchronized (DatabaseHelper.class) {
                if (instance == null) //第二次判断：过滤掉极少数多线程情况
                    instance = new DatabaseHelper(context);
            }
        }
        return instance;
    }

1、配置表和对象的映射关系

	@DatabaseTable(tableName = "tb_user")
	public class User {
	
	    @DatabaseField(generatedId = true)
	    private int id;
	
	    @DatabaseField(columnName = "name")
	    private String name;
	
	    @DatabaseField(columnName = "desc")
	    private String desc;
	
	    public User() {
	    }
	
	    public User(String name, String desc) {
	        this.name = name;
	        this.desc = desc;
	    }
		
		//GET and SET
	}

2、提供DatabaseHelper管理类

- 一张表对应于一个DAO(Data Access Object)类
- 数据库创建逻辑(一个数据库下有多张表——>多个对应映射类——>多个DAO)
- 数据库升级逻辑——>如何升级已经有数据的数据库?
- 统一管理多个DAO类
- 资源的释放

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

3、获取对应的DAO类执行数据库操作

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