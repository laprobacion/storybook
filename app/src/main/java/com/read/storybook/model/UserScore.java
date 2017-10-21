package com.read.storybook.model;


import java.math.RoundingMode;
import java.text.DecimalFormat;

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
        String v = "";
    }

    public String getUsername(){
        return this.username;
    }

    public int getPercentage(){
        Double score = Double.valueOf((double)this.score / (double)this.item);
        DecimalFormat df = new DecimalFormat("#.####");

        return ((Double)(Double.parseDouble(df.format(score)) * ((double)100))).intValue();
    }
}
