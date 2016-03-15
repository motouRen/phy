package com.winning.pregnancy.util;

import android.content.Context;

import com.winning.pregnancy.ahibernate.util.MyDBHelper;
import com.winning.pregnancy.model.Content;
import com.winning.pregnancy.model.EMMessageLocal;

public class DBHelper extends MyDBHelper
{
    private static final String DBNAME = "PregnancyAndroid.db";// 数据库名
    private static final int DBVERSION = 1;
    private static final Class<?>[] clazz =
    { EMMessageLocal.class, Content.class };// 要初始化的表

    public DBHelper(Context context)
    {
        super(context, DBNAME, null, DBVERSION, clazz);
    }

}
