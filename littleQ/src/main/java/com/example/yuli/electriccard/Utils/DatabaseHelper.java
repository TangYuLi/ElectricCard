package com.example.yuli.electriccard.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by YULI on 2017/9/3.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "login.db";
    public static final int version = 1;

    //已经硬性规定了三个参数的值，因此只需要再传一个Context参数即可
    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, version);
    }

//    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
//                          int version){
//        super(context, name, factory, version);
//    }

//    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
//                          int version, DatabaseErrorHandler errorHandler){
//        super(context, name, factory, version, errorHandler);
//    }

    //getReadableDatabase()或getWritableDatabase()之后调用
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建在DB_NAME数据库下管理的表
        String sql = "create table LoginInfo(" +
                "tel char(11) primary key," +
                "password varchar(12) not null)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
