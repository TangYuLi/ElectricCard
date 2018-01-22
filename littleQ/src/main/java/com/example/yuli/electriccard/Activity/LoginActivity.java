package com.example.yuli.electriccard.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuli.electriccard.Adapters.TelRecycleAdapter;
import com.example.yuli.electriccard.Beans.User;
import com.example.yuli.electriccard.DataSource.LittleQDBOpenHelper;
import com.example.yuli.electriccard.R;

import java.util.ArrayList;

/**
 * Created by YULI on 2017/9/2.
 */

public class LoginActivity extends Activity{

    private static Context context;
    public static LoginActivity instance;

    public EditText et_tel;
    public boolean isTelFocused  =false;
    public EditText et_password;
    public ImageButton imageBtn_tel_fold_unfold;
    public boolean isFold = false;
    public ImageButton imageBtn_tel_del;
    public ImageButton imageBtn_password_visible_invisible;
    public boolean isVisible = false;
    public Button btn_login;
    public Button btn_forget_password;
    public Button btn_register;
    public RecyclerView recyclerView_tel;
    public RecyclerView.Adapter<RecyclerView.ViewHolder> userAdapter;
    public ArrayList<User> userList;
    public TextView textView_verify_tel;
    public TextView textView_verify_password;

    private static final String TABLE_USER = "User";
    public Cursor userCursor = null;
    public SQLiteDatabase userDatabase = null;
    public SQLiteOpenHelper userDatabaseHelper = null;

    public static class LoginActivityHandler extends Handler{

        private final int SQLDB_INSERT_EXCEPTION = 0;
        private final int TEL_ITEM_CLICK = 1;

        //单例模式
        public static LoginActivityHandler instance = new LoginActivityHandler();

        public LoginActivityHandler(){
            if (instance == null){
                instance = new LoginActivityHandler();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SQLDB_INSERT_EXCEPTION:
                    String insertException = ""+msg.obj;
                    Toast.makeText(LoginActivity.instance,"该手机号码已注册",Toast.LENGTH_SHORT).show();
                    break;
                case TEL_ITEM_CLICK:
                    String tel = ""+msg.obj;
                    ((LoginActivity)context).et_tel.setText(tel);
                    //视觉效果相当于折叠起telList
                    ((LoginActivity)context).imageBtn_tel_fold_unfold.performClick();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_login);
        init();
    }

    public void init() {
        context = LoginActivity.this;
        et_tel = (EditText)findViewById(R.id.et_tel);
        et_password = (EditText)findViewById(R.id.et_password);
        imageBtn_tel_del = (ImageButton)findViewById(R.id.imageBtn_tel_del);
        imageBtn_tel_fold_unfold = (ImageButton)findViewById(R.id.imageBtn_tel_fold_unfold);
        imageBtn_password_visible_invisible =
                (ImageButton)findViewById(R.id.imageBtn_password_visible_invisible);
        textView_verify_tel = (TextView)findViewById(R.id.tv_verify_account);
        textView_verify_password = (TextView)findViewById(R.id.tv_verify_password);
        recyclerView_tel = (RecyclerView)findViewById(R.id.recycleView_account);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_forget_password = (Button)findViewById(R.id.btn_forgetPassword);
        btn_register = (Button)findViewById(R.id.btn_register);

        initTelRecycleView();
        initAnimation();
    }

    public void initTelRecycleView(){
        Cursor cursor = readUserFromSQLiteDB();
        userList = new ArrayList<>();
        if (cursor!=null){
            while (cursor.moveToNext()){
                User user = new User();
                user.setTel(cursor.getString(0));
                user.setHeadPic(cursor.getString(1));
                userList.add(user);
            }
            TelRecycleAdapter adapter = new TelRecycleAdapter(LoginActivity.this,userList);
            recyclerView_tel.setAdapter(adapter);
        }else return;
    }

    private Cursor readUserFromSQLiteDB(){
        LittleQDBOpenHelper littleQDBOpenHelper = new LittleQDBOpenHelper(LoginActivity.this);
        return littleQDBOpenHelper.query("User",new String[]{"tel,headPic"});
    }

    public void initAnimation(){

    }

    public void onClickImageBtnTelDel(){
        et_tel.setText("");
    }

    public void onClickImageBtnTelFold(){
        isFold = !isFold;
        if (isFold){
            imageBtn_tel_fold_unfold.setBackgroundResource(R.mipmap.fold);
        }
        else {
            imageBtn_tel_fold_unfold.setBackgroundResource(R.mipmap.unfold);
        }
        foldTelList(isFold);
    }

    public void foldTelList(boolean isFold){
        if (isFold){
            //显示电话列表（加动画），隐藏密码等控件（加动画）
            recyclerView_tel.setVisibility(View.VISIBLE);
            et_password.setVisibility(View.GONE);
            imageBtn_password_visible_invisible.setVisibility(View.GONE);
            //viewGroup.setVisible(View.GONE);
        }else {
            //隐藏电话列表（加动画），显示密码等控件（加动画）
            recyclerView_tel.setVisibility(View.GONE);
            et_password.setVisibility(View.VISIBLE);
            imageBtn_password_visible_invisible.setVisibility(View.VISIBLE);
            //viewGroup.setVisible(View.VISIBLE);
        }
    }

    public void onClickImageBtnPasswordVisible(){
        isVisible = !isVisible;
        if (isVisible){
            imageBtn_password_visible_invisible.setBackgroundResource(R.mipmap.open_eye);
            et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }else {
            imageBtn_password_visible_invisible.setBackgroundResource(R.mipmap.close_eye);
            et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    public void onClickBtnLogin(){

    }

    public void onClickBtnForgetPassword(){
        Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
        startActivity(intent);
    }

    public void onClickBtnRegister(){
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

}
