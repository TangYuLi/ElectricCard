package com.example.yuli.electriccard.Activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.yuli.electriccard.R;
import com.example.yuli.electriccard.Utils.ComponentsListUtil;
import com.example.yuli.electriccard.Utils.DatabaseHelper;
import com.example.yuli.electriccard.Utils.DividerItemDecoration;
import com.example.yuli.electriccard.Utils.Person;
import com.example.yuli.electriccard.Utils.TelListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YULI on 2017/9/2.
 */

public class LoginActivity extends Activity implements View.OnClickListener{

    public static LoginActivity instance;

    public EditText et_tel;
    public EditText et_password;
    public ImageButton ib_unfold;
    public ImageButton ib_del;
    public ImageButton ib_see_password;
    public ImageButton ib_fold;
    public Button btn_login;
    public Button btn_forget_password;
    public Button btn_register;
    public RecyclerView rv_telList;
    public View view_decoration;
    public View rootView;
    public View loginView;
    public View unfoldView;
    public TextView textView_format_tel;
    public TextView textView_format_password;
    public ComponentsListUtil componentsListUtil;
    public ComponentsListUtil telComponentList;
    public LayoutInflater inflater;
    public List<Person> personList;
    public TelListAdapter telListAdapter;
    public boolean password_visible_state = false;
    public final String text_tel_format = "*请输入11位数的手机号码*";
    public final String text_tel_not_exist = "*该手机号码还未注册*";
    public final String text_tel_null = "*手机号码不允许为空*";
    public final String text_password_format = "*密码只允许包含6-12位限于[a-z][A-Z][0-9]的字符*";
    public final String text_password_mistake = "*密码错误*";
    public final String text_password_null = "*密码不允许为空*";
    public boolean isFormatTel = false;
    public boolean isFormatPassword = false;
    public boolean isTelNull = false;
    public boolean isPasswordNull = false;
    public boolean isTelExist = false;
    public boolean isPasswordRight = false;
    public CharSequence tel;
    public CharSequence password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(this);
        rootView = inflater.inflate(R.layout.ac_login,null);
        setContentView(rootView);
        loginView = inflater.inflate(R.layout.include_login,
                (ViewGroup)rootView.findViewById(R.id.login_space));
        unfoldView = inflater.inflate(R.layout.include_unfold,
                (ViewGroup)rootView.findViewById(R.id.login_space));
        loginView.setVisibility(View.VISIBLE);
        unfoldView.setVisibility(View.GONE);
        Log.e("login_space",""+rootView.findViewById(R.id.login_space).getVisibility());

        float fromXDelta = getResources().getDimension(R.dimen.x16);
        float toXDelta = getResources().getDimension(R.dimen.x16);
        float fromYDelta = getResources().getDimension(R.dimen.y180);
        float toYDelta = getResources().getDimension(R.dimen.y100);

        Animation anim_login_page = AnimationUtils.loadAnimation(this,R.anim.anim_login_page);
        loginView.setAnimation(anim_login_page);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(loginView,"translationX",fromXDelta,toXDelta),
                ObjectAnimator.ofFloat(loginView,"translationY",fromYDelta,toYDelta),
                ObjectAnimator.ofFloat(loginView,"alpha",0.1f,1)
        );
        set.setDuration(500).start();

        init();
        initComponentList();
        readDataFromSQLiteDatabase();
        initEditText_TEL();
        initRecycleView();
    }

    public void init() {

        et_tel = (EditText) loginView.findViewById(R.id.et_tel);
        et_password = (EditText) loginView.findViewById(R.id.et_password);
        textView_format_tel = (TextView)loginView.findViewById(R.id.tv_format_tel);
        textView_format_password = (TextView)loginView.findViewById(R.id.tv_format_password);
        ib_unfold = (ImageButton) loginView.findViewById(R.id.ib_unfold);
        ib_del = (ImageButton) loginView.findViewById(R.id.ib_del);
        ib_see_password = (ImageButton) loginView.findViewById(R.id.ib_see_password);
        ib_fold = (ImageButton)unfoldView.findViewById(R.id.ib_fold);
        btn_login = (Button) loginView.findViewById(R.id.btn_login);
        btn_forget_password = (Button) findViewById(R.id.btn_forgetPassword);
        btn_register = (Button) findViewById(R.id.btn_register);
        view_decoration = unfoldView.findViewById(R.id.view_decoration);

        rv_telList = (RecyclerView)unfoldView.findViewById(R.id.rv_telList);
        rv_telList.setLayoutManager(new LinearLayoutManager(this));
        rv_telList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL_LIST));

        et_tel.addTextChangedListener(tWatcher_tel);
        et_password.addTextChangedListener(tWatch_password);
        ib_unfold.setOnClickListener(this);
        ib_del.setOnClickListener(this);
        ib_see_password.setOnClickListener(this);
        ib_fold.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_forget_password.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    private TextWatcher tWatcher_tel = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            tel = editable.toString();
            int length = tel.length();
            if (length<11 && length>0){
                isFormatTel = false;
                textView_format_tel.setText(text_tel_format);
                textView_format_tel.setVisibility(View.VISIBLE);
            }
            else{
                if (length == 0)et_password.setText("");
                isFormatTel = true;
                textView_format_tel.setVisibility(View.GONE);
            }
        }
    };

    private TextWatcher tWatch_password = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            password = editable.toString();
            int length = password.length();
            String sPattern = "^[A-Za-z0-9]+$";
            Pattern pattern = Pattern.compile(sPattern);
            Matcher matcher = pattern.matcher(password);
            if ((length<6 || !matcher.matches())&&length!=0){
                isFormatPassword = false;
                textView_format_password.setText(text_password_format);
                textView_format_password.setVisibility(View.VISIBLE);
            }else {
                isFormatPassword = true;
                textView_format_password.setVisibility(View.GONE);
            }
        }
    };

    public void initComponentList() {
        componentsListUtil = new ComponentsListUtil();
        telComponentList = new ComponentsListUtil();

        componentsListUtil.addComponent(et_tel);
        componentsListUtil.addComponent(et_password);
        componentsListUtil.addComponent(ib_unfold);
        componentsListUtil.addComponent(ib_del);
        componentsListUtil.addComponent(ib_see_password);
        componentsListUtil.addComponent(btn_login);
        componentsListUtil.addComponent(btn_forget_password);
        componentsListUtil.addComponent(btn_register);
        componentsListUtil.addComponent(textView_format_tel);
        componentsListUtil.addComponent(textView_format_password);

        telComponentList.addComponent(ib_fold);
        telComponentList.addComponent(view_decoration);
        telComponentList.addComponent(rv_telList);
    }

    public void readDataFromSQLiteDatabase() {
        DatabaseHelper helper = new DatabaseHelper(LoginActivity.this);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "select * from LoginInfo";
        Cursor cursor = db.rawQuery(sql, null);
        int tel_index = cursor.getColumnIndex("tel");
        int password_index = cursor.getColumnIndex("password");
        if (personList == null) personList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String tel = cursor.getString(tel_index);
            String password = cursor.getString(password_index);
            personList.add(new Person(tel, password));
        }
        cursor.close();
        if (personList.size()==0){
            personList.add(new Person("18826139506","tyl5616898"));
            personList.add(new Person("18319130033","tyl5616898"));
        }
    }

    public void initEditText_TEL() {
        if (personList != null) {
            int firstPersonIndex = 0;
            et_tel.setText(personList.get(firstPersonIndex).getTel());
        }
    }

    public void initRecycleView() {
        telListAdapter = new TelListAdapter(personList);
        telListAdapter.setOnRecycleViewListener(new TelListAdapter.OnRecycleViewListener() {

            @Override
            public void onItemDelClick(int pos) {
                personList.remove(pos);
                telListAdapter.notifyDataSetChanged();
//                telComponentList.allComponentsInvisible();
//                componentsListUtil.allComponentsVisible();
                unfoldView.setVisibility(View.GONE);
                loginView.setVisibility(View.VISIBLE);

                if (isFormatTel)textView_format_tel.setVisibility(View.GONE);
                if (isFormatPassword) textView_format_password.setVisibility(View.GONE);
            }

            @Override
            public void onItemClick(int pos) {
                Person person = personList.get(pos);
                et_tel.setText(person.getTel());
//                telComponentList.allComponentsInvisible();
//                componentsListUtil.allComponentsVisible();
                unfoldView.setVisibility(View.GONE);
                loginView.setVisibility(View.VISIBLE);
                if (isFormatTel)textView_format_tel.setVisibility(View.GONE);
                if (isFormatPassword) textView_format_password.setVisibility(View.GONE);
            }
        });
        rv_telList.setAdapter(telListAdapter);
        telListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_unfold:
//                componentsListUtil.allComponentsInvisible();
                loginView.setVisibility(View.GONE);
//                telComponentList.allComponentsVisible();
                unfoldView.setVisibility(View.VISIBLE);
                break;
            case R.id.ib_del:
                et_tel.setText("");
                break;
            case R.id.ib_see_password:
                password_visible_state = !password_visible_state;
                Log.e("password_visible_state", "" + password_visible_state);
                if (password_visible_state) {
                    Log.e("open_eye", "open_eye");
                    ib_see_password.setBackgroundResource(R.mipmap.open_eye);
                    //密码显示
                    et_password.setTransformationMethod(
                            HideReturnsTransformationMethod.getInstance());

                } else {
                    Log.e("close_eye", "close_eye");
                    ib_see_password.setBackgroundResource(R.mipmap.close_eye);
                    //密码隐藏
                    et_password.setTransformationMethod(
                            PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.ib_fold:
//                telComponentList.allComponentsInvisible();
//                componentsListUtil.allComponentsVisible();
                unfoldView.setVisibility(View.GONE);
                loginView.setVisibility(View.VISIBLE);
                if (isFormatTel)textView_format_tel.setVisibility(View.GONE);
                if (isFormatPassword) textView_format_password.setVisibility(View.GONE);
                break;
            case R.id.btn_login:
                break;
            case R.id.btn_forgetPassword:
                Intent intent = new Intent(LoginActivity.this,
                        ForgetPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_register:
                Intent intent1 = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }
}
