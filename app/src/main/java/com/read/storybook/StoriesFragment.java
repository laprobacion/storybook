package com.read.storybook;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.util.List;

public class StoriesFragment extends Fragment{

    View myView;
    TextView [] lvlArray;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.stories_layout, container, false);
        Button addLevelBtn = (Button)myView.findViewById(R.id.addLevelBtn);
        addLevelBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent myIntent = new Intent(getActivity(), LevelActivity.class);
                startActivity(myIntent);
            }
        });
        User user = AppCache.getInstance().getUser();
        if(!user.isAdmin()){
            addLevelBtn.setVisibility(View.INVISIBLE);
        }
        return myView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(lvlArray != null && lvlArray.length > 0){
            RelativeLayout relativeLayout = (RelativeLayout) myView.findViewById(R.id.storiesRelativeLayout);
            for(TextView tv : lvlArray){
                relativeLayout.removeView(tv);
            }
        }
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
        lvlArray = new TextView[levels.size()];
        TextView parentTv = (TextView)myView.findViewById(R.id.storiesTitle);
        RelativeLayout relativeLayout = (RelativeLayout) myView.findViewById(R.id.storiesRelativeLayout);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);

        int i = 0;
        int top = 60;
        for (final Level level : levels  ) {
            TextView tv = new TextView(getActivity().getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            tv.setId(i);
            tv.setText(level.getName());
            tv.setTextColor(Color.BLACK);
            lvlArray[i] = tv;
            lp.addRule(RelativeLayout.BELOW , parentTv.getId());
            if( i == 0){
                lp.setMargins(100,10,0,0);
            }else{
                lp.setMargins(100,top,0,0);
                top += 50;
            }

            tv.setLayoutParams(lp);
            relativeLayout.addView(tv);
            tv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent myIntent = new Intent(getActivity(), StoriesActivity.class);
                    myIntent.putExtra(AppConstants.LEVEL_NAME,level);
                    StoriesFragment.this.startActivity(myIntent);
                }
            });

            i++;
        }
    }
}
