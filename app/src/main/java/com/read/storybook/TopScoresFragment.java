package com.read.storybook;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.read.storybook.model.Image;
import com.read.storybook.model.Story;
import com.read.storybook.model.UserScore;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.UserScoreService;
import com.read.storybook.service.UserStoryService;
import com.read.storybook.util.AppCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class TopScoresFragment extends Fragment{

    View myView;
    TextView usersScores;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.top_scores_layout, container, false);
        usersScores = myView.findViewById(R.id.usersScores);
        search();
        return myView;
    }

    private void search(){
        Service service = new Service("Getting Top Scores", getActivity(), new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    getUsers(resp);
                }catch (Exception e){e.printStackTrace();}
            }
        });
        UserStoryService.search(service);
    }

    private void getUsers(JSONObject resp){
        JSONArray arr = resp.optJSONArray("records");
        UserScoreService userScoreService = new UserScoreService();
        for( int i=0; i< arr.length(); i++){
            JSONObject obj = arr.optJSONObject(i);
            userScoreService.addUserScore(obj.optString("username"),obj.optString("USER_ID"),obj.optInt("SCORE"),obj.optInt("ITEM"));
            //obj.optString("STORY_ID")
        }
        populateList(userScoreService);
    }

    private void populateList(UserScoreService userScoreService){
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String,Integer> pair : userScoreService.getTopScores().entrySet()){
            sb.append(userScoreService.getUsername(pair.getKey()) + " " + pair.getValue() + "% \r\n");
        }
        usersScores.setText(sb.toString());
    }
}
