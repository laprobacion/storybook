package com.read.storybook;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.read.storybook.model.Level;
import com.read.storybook.model.User;
import com.read.storybook.service.LevelService;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.util.AppCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LevelsActivity extends AppCompatActivity {
    public static final String IS_LESSON = "IS_LESSON";
    private boolean isLesson;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
        isLesson = Boolean.valueOf(getIntent().getStringExtra(IS_LESSON));
        lv= (ListView)findViewById(R.id.list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LevelsActivity.this, LevelActivity.class);
                startActivity(myIntent);
            }
        });

        User user = AppCache.getInstance().getUser();
        if(!user.isAdmin()){
            fab.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(lv.getAdapter() != null){
            lv.setAdapter(null);
        }
        search();
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
    private void search(){
        Service service = new Service("Searching level...", LevelsActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    if (resp.optString("records") != null) {
                        addLevels(createList(resp));
                    }else{

                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
        LevelService.read(service);
    }
    private void addLevels(List<Level> levels){

        lv.setAdapter(new CustomAdapter(LevelsActivity.this, levels, isLesson));

    }
}
