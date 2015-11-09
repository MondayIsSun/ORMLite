# 单例模式最高效写法

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
