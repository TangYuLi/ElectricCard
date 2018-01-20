package com.example.yuli.electriccard.Utils;

/**
 * Created by YULI on 2017/9/4.
 */

public class Person {

    private String tel;
    private String password;

    public Person(String tel, String password) {
        this.tel = tel;
        this.password = password;
    }

    public String getTel() {
        return tel;
    }

    public String getPassword() {
        return password;
    }
}
