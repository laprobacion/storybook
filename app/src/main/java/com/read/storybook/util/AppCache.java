package com.read.storybook.util;

import com.read.storybook.CustomViewPager;
import com.read.storybook.model.User;

import java.util.HashMap;
import java.util.Map;

public class AppCache {

    public static AppCache _this;
    private boolean isPageOneDestroyed;
    private Map<Integer, Boolean> questionPages = new HashMap<Integer, Boolean>();
    private CustomViewPager viewPager;

    public void clearQuestionPages(CustomViewPager viewPager){
        this.viewPager = viewPager;
        questionPages.clear();
    }
    public void enablePager(){
        this.viewPager.setPagingEnabled(true);
    }
    public void addQuestionPage(int i){
        questionPages.put(i+1,false);
    }
    public void updateQuestionPage(int i){
        questionPages.put(i,true);
    }
    public boolean isQuestionPageAnswered(int i){
        Boolean flag = questionPages.get(i);
        if(flag != null){
            return flag;
        }
        return true;
    }
    public boolean isPageOneDestroyed() {
        return isPageOneDestroyed;
    }

    public void setPageOneDestroyed(boolean pageOneDestroyed) {
        isPageOneDestroyed = pageOneDestroyed;
    }

    private User user;

    public static AppCache getInstance(){
        if(_this == null){
            _this = new AppCache();
        }
        return _this;
    }

    public void setUser(User user){
        this.user = user;
    }
    public User getUser(){
        return  this.user;
    }
}
