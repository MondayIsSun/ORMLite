# 顺便谈谈我对单例模式的理解

	private static DatabaseHelper instance;

    private DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 4);
    }

    public static synchronized DatabaseHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }
        return instance;
    }