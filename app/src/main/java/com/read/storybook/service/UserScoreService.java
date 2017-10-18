package com.read.storybook.service;


import com.read.storybook.model.User;
import com.read.storybook.model.UserScore;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UserScoreService {

    private Map<String, UserScore> userScoreMap;
    private Map<String, Integer> userScores;

    public UserScoreService(){
        userScoreMap = new HashMap<String, UserScore>();
        userScores = new HashMap<String, Integer>();
    }

    public void addUserScore(String username, String userId, int score, int item){
        UserScore userScore = userScoreMap.get(userId);
        if(userScore == null){
            UserScore us = new UserScore();
            us.addScoreItem(score, item);
            us.setUsername(username);
            userScoreMap.put(userId,us);
        }else{
            userScore.addScoreItem(score, item);
        }
    }

    private void prepareScores(){
        for (Map.Entry<String,UserScore> pair : userScoreMap.entrySet()){
            userScores.put(pair.getKey(), pair.getValue().getPercentage());
        }
    }

    private Map<String, Integer> sortByValue(){
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(userScores.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                //return (o1.getValue()).compareTo(o2.getValue()); ASC
                return (o2.getValue()).compareTo(o1.getValue()); // DESC
            }
        });
        Map<String, Integer> sorted = new LinkedHashMap<String, Integer>();
        int count = 0;
        for(Map.Entry<String, Integer> entry : list){
            if(count <= 10) {
                sorted.put(entry.getKey(), entry.getValue());
            }
            count++;
        }
        return sorted;
    }

    public Map<String, Integer> getTopScores(){
        prepareScores();
        return sortByValue();
    }

    public String getUsername(String userId){
        return userScoreMap.get(userId).getUsername();
    }
}
