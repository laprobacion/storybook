package com.read.storybook;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.read.storybook.model.Level;
import com.read.storybook.model.Story;
import com.read.storybook.model.User;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.StoryService;
import com.read.storybook.util.AppCache;
import com.read.storybook.util.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoriesActivity extends AppCompatActivity {

    Level level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        level = (Level)getIntent().getSerializableExtra(AppConstants.LEVEL_NAME);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(StoriesActivity.this, AddStoryActivity.class);
                myIntent.putExtra(AppConstants.LEVEL_ID,level.getId());
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
        search();
    }
    private void search(){
        Service service = new Service("Searching story...", StoriesActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    if (resp.optString("records") != null) {
                        addStories(createList(resp));
                    }else{

                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
        StoryService.search(level.getId(),service);
    }

    private List<Story> createList(JSONObject resp){
        List<Story> stories = new ArrayList<Story>();
        JSONArray arr = resp.optJSONArray("records");
        for( int i=0; i< arr.length(); i++){
            JSONObject obj = arr.optJSONObject(i);
            Story story = new Story();
            story.setId(obj.optString("id"));
            story.setTitle(obj.optString("name"));
            story.setActive(obj.optInt("isActive") == 1);
            stories.add(story);
        }
        return stories;
    }
    private void addStories(List<Story> stories){
        ListView lv= (ListView)findViewById(R.id.list);
        lv.setAdapter(new CustomStoryAdapter(this, stories));
    }
}
