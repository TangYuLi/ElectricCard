package com.example.yuli.electriccard.DataSource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;

import com.example.yuli.electriccard.Activity.LoginActivity;

import java.util.ArrayList;

/**
 * Created by YULI on 2018/1/21.
 */

public class LittleQDBOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB_NAME = "LittleQ.db";

    //SQLiteOpenHelper子类必须要的一个构造函数
    public LittleQDBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        //必须通过super 调用父类的构造函数
        super(context, name, factory, version);
    }

    //数据库的构造函数，传递三个参数的
    public LittleQDBOpenHelper(Context context, String name, int version){
        this(context, name, null, version);
    }

    //数据库的构造函数，传递一个参数的， 数据库名字和版本号都写死了
    public LittleQDBOpenHelper(Context context){
        this(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建对应数据库中的所有表操作
        String userSQL = "create table User(tel char(11) primary key,password varchar not null)";
        sqLiteDatabase.execSQL(userSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    //在多并发情况下安全操作数据库
    public synchronized void insert(String tableName,ContentValues values){
        if (tableName.equals("User")) insertValuesToUser(values);
    }

    private void insertValuesToUser(ContentValues values){
        SQLiteDatabase littleQ = getWritableDatabase();
        String tel = values.getAsString("tel");

        //首先取出tel集合，判断是否插入了重复的tel
        ArrayList<String> telList = new ArrayList<>();
        String telQuerySQL = "select * from User";
        Cursor telCursor = littleQ.rawQuery(telQuerySQL,new String[]{"tel"});
        if (telCursor!=null){
            while (telCursor.moveToNext()){
                if (telCursor.getString(0)!=null){
                    telList.add(telCursor.getString(0));
                }
            }
        }
        if (telList.contains(tel)){
            //通过注册一个Handler来实现更新主线程的UI
            final int SQLDB_INSERT_EXCEPTION = 0;
            Message msg = Message.obtain();
            msg.obj = "该手机号码已注册";
            msg.what = SQLDB_INSERT_EXCEPTION;
            LoginActivity.LoginActivityHandler.instance.sendMessage(msg);
            return;
        }

        //字段值tel和password不能为空，判断在btn_login的onClick方法中进行
        littleQ.insert("User",null,values);
        littleQ.close();
    }

    public synchronized void delete(String tableName,String primaryColumn){
        if (tableName.equals("User")) deleteUserItem(primaryColumn);
    }

    private void deleteUserItem(String tel){
        SQLiteDatabase littleQ = getWritableDatabase();
        ArrayList<String> telList = new ArrayList<>();
        Cursor cursor = littleQ.rawQuery("select tel from User",new String[]{"tel"});
        if (cursor!=null){
            while (cursor.moveToNext()){
                telList.add(cursor.getString(0));
            }
        }
        if (!telList.contains("tel")){
            return;
        }
        //根据tel查找对应的index
        String delSQL = "delete from User where tel="+tel;
        littleQ.execSQL(delSQL);
        littleQ.close();
    }

    public Cursor query(String tableName,String[] selectionArgs){
        if (tableName.equals("User")) return userTableQuery(selectionArgs);
        return null;
    }

    private Cursor userTableQuery(String[] selectionArgs){
        SQLiteDatabase littleQ = getReadableDatabase();
        Cursor cursor = littleQ.rawQuery("select * from User",selectionArgs);
        littleQ.close();
        return cursor;
    }

    public synchronized void update(String tableName,ContentValues values){
        if (tableName.equals("User")) updateUser(values);
    }

    private void updateUser(ContentValues values){
        SQLiteDatabase littleQ = getWritableDatabase();
        littleQ.update("User",values,"tel=?",new String[]{values.getAsString("tel")});
        littleQ.close();
    }
}
