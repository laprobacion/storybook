package com.read.storybook.model;


public class UserScore {

    private int score;
    private int item;
    private String username;

    public void setUsername(String username){
        this.username = username;
    }

    public void addScoreItem(int score, int item){
        this.score += score;
        this.item += item;
    }

    public String getUsername(){
        return this.username;
    }

    public int getPercentage(){
        return (this.score / this.item) * 100;
    }
}
