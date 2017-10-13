package com.read.storybook.util;

import com.read.storybook.model.User;

public class AppCache {

    public static AppCache _this;
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
