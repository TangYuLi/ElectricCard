package com.example.yuli.electriccard.Utils;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YULI on 2017/9/2.
 */

public class ComponentsListUtil {
//    private static final ComponentsListUtil ourInstance = new ComponentsListUtil();

    private List<View> componentList;

    //单例模式
//    public static ComponentsListUtil getInstance() {
//        return ourInstance;
//    }

    public ComponentsListUtil() {
        componentList = new ArrayList<>();
    }

//    public void setComponentList(List<View> componentList){
//        this.componentList = componentList;
//    }

    public void addComponent(View view){
        componentList.add(view);
    }

    public void removeComponent(View view){
        componentList.remove(view);
    }

    public void allComponentsInvisible(){
        for (View v:componentList) {
            v.setVisibility(View.GONE);
        }
//        for (int i=0;i<componentList.size();i++){
//            view = componentList.get(i);
//            view.setVisibility(View.INVISIBLE);
//        }
//        view = componentList.get(0);
    }

    public void allComponentsVisible(){
        for (View v:componentList) {
            v.setVisibility(View.VISIBLE);
        }
//        for (int i=0;i<componentList.size();i++){
//            view = componentList.get(i);
//            view.setVisibility(View.VISIBLE);
//        }
//        view = componentList.get(0);
    }
}
