package com.read.storybook;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.read.storybook.model.Level;
import com.read.storybook.model.User;
import com.read.storybook.service.LevelService;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.util.AppCache;
import com.read.storybook.util.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StoriesFragment extends Fragment{

    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.stories_layout, container, false);
        FloatingActionButton fab = (FloatingActionButton) myView.findViewById(R.id.fab);
             fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), LevelActivity.class);
                startActivity(myIntent);
            }
        });

        User user = AppCache.getInstance().getUser();
        if(!user.isAdmin()){
            fab.setVisibility(View.INVISIBLE);
        }
        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
        search(myView);
    }

    private List<Level> createList(JSONObject resp){
        List<Level> levels = new ArrayList<Level>();
        JSONArray arr = resp.optJSONArray("records");//.leoptJSONObject(0).optString("id")
        for( int i=0; i< arr.length(); i++){
            JSONObject obj = arr.optJSONObject(i);
            Level level = new Level();
            level.setId(obj.optString("id"));
            level.setName(obj.optString("name"));
            level.setActive(obj.optInt("isActive") == 1);
            levels.add(level);
        }
        return levels;
    }
    private void search(final View view){
        Service service = new Service("Searching level...", getActivity(), new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    if (resp.optString("records") != null) {
                        addLevels(view, createList(resp));
                    }else{

                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
        LevelService.read(service);
    }
    private void addLevels(View myView, List<Level> levels){
        ListView lv= myView.findViewById(R.id.list);
        lv.setAdapter(new CustomAdapter(this.getActivity(), levels));

    }
}
