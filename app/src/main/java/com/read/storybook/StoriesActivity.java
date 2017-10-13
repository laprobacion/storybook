package com.read.storybook;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    TextView storyArray [];
    Level level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        level = (Level)getIntent().getSerializableExtra(AppConstants.LEVEL_NAME);
        TextView title = (TextView) findViewById(R.id.textView);
        title.setText(level.getName() + " Stories" );
        Button add = (Button)findViewById(R.id.addStoriesActivityBtn);
        add.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent myIntent = new Intent(StoriesActivity.this, AddStoryActivity.class);
                myIntent.putExtra(AppConstants.LEVEL_ID,level.getId());
                startActivity(myIntent);
            }
        });
        User user = AppCache.getInstance().getUser();
        if(!user.isAdmin()){
            add.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(storyArray != null && storyArray.length > 0){
            RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activityStoriesRelativeLayout);
            for(TextView tv : storyArray){
                relativeLayout.removeView(tv);
            }
        }
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
        storyArray = new TextView[stories.size()];
        TextView parentTv = (TextView)findViewById(R.id.textView);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activityStoriesRelativeLayout);
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);

        int i = 0;
        int top = 60;
        for (final Story story : stories  ) {
            TextView tv = new TextView(StoriesActivity.this.getApplicationContext());
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            tv.setId(i);
            tv.setText(story.getTitle());
            tv.setTextColor(Color.BLACK);
            storyArray[i] = tv;
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
                    Intent myIntent = new Intent(StoriesActivity.this, StoryActivity.class);
                    myIntent.putExtra(AppConstants.STORY_OBJ,story);
                    StoriesActivity.this.startActivity(myIntent);
                }
            });

            i++;
        }
    }
}
