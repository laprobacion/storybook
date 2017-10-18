package com.read.storybook;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.UserScoreService;
import com.read.storybook.service.UserStoryService;
import com.read.storybook.util.AppCache;

import org.json.JSONArray;
import org.json.JSONObject;

public class ScoreFragment extends Fragment{

    View myView;
    TextView tv;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.score_layout, container, false);
        tv = myView.findViewById(R.id.scorePerStory);
        viewScore();
        return myView;
    }

    private void viewScore(){
        Service service = new Service("Getting Score...", getActivity(), new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    getUsers(resp);
                }catch (Exception e){e.printStackTrace();}
            }
        });
        UserStoryService.searchByName( service);
    }

    private void getUsers(JSONObject resp){
        StringBuilder sb = new StringBuilder();
        JSONArray arr = resp.optJSONArray("records");
        for( int i=0; i< arr.length(); i++){
            JSONObject obj = arr.optJSONObject(i);
            int percent = (obj.optInt("SCORE") / obj.optInt("ITEM")) * 100;
            sb.append(obj.optString("TITLE") + " = " + percent + "% \r\n");
        }
        tv.setText(sb.toString());
    }
}
