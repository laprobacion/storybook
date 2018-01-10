package com.read.storybook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.read.storybook.Image.ImageLoader;
import com.read.storybook.model.Choice;
import com.read.storybook.model.Image;
import com.read.storybook.model.Lesson;
import com.read.storybook.model.Question;
import com.read.storybook.model.Story;
import com.read.storybook.model.User;
import com.read.storybook.service.QuestionService;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.StoryService;
import com.read.storybook.util.AppCache;
import com.read.storybook.util.AppConstants;
import com.read.storybook.util.Player;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StoryActivity extends FragmentActivity{
    Story story;
    TextView title;
    MyPageAdapter pageAdapter;
    Story tempStory = new Story();
    ImageLoader imageLoader;
    boolean isLesson;
    Integer pageToShow;
    String pageToLoad;
    FloatingActionButton fab;
    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        int ctr = 0;
        if(!isLesson && pageToLoad == null) {
            if (pageToShow == null && tempStory.getLessons() != null && tempStory.getLessons().size() > 0) {
                pageToShow = generateRandomNumbers(story.getImages().size());
            }
        }
        for(Image i : story.getImages()){
            String page = (ctr + 1) + " of " + story.getImages().size();
            boolean hasLesson = pageToShow != null ? pageToShow == (ctr + 1): false;
            fList.add(PageStoryFragment.newInstance(title.getText().toString(), i.getBitmap(), (ctr + 1) == story.getImages().size(), tempStory, page, hasLesson, false,null));
            ctr++;
        }
        return fList;
    }
    private int generateRandomNumbers(int max){
        Random rand = new Random();
        Integer  n = 0;
        int median = (int)Math.ceil((1 + max) / 2.0);
        n = rand.nextInt(max + 1 - median) + median;
        if( n > max){
            n = n - max;
        }
        return n;
    }
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        isLesson = Boolean.valueOf(getIntent().getStringExtra(LevelsActivity.IS_LESSON));
        story = (Story)getIntent().getSerializableExtra(AppConstants.STORY_OBJ);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(StoryActivity.this, AddLessonNarrativeActivity.class);
                myIntent.putExtra(AppConstants.STORY_ID,story.getId());
                myIntent.putExtra(AppConstants.STORY_LESSON,story.getId());
                startActivity(myIntent);
            }
        });
        pageToLoad = getIntent().getStringExtra(LessonActivity.CURRENT_PAGE);
        if(!isLesson){
            tempStory.setTitle(story.getTitle());
            imageLoader = new ImageLoader(this, story, tempStory, pageToLoad == null);
            fab.setVisibility(View.INVISIBLE);
            if(story.getSoundList() != null && story.getSoundList().size() > 0){
                imageLoader.setBtnNarrative(View.INVISIBLE);
            }else{
                imageLoader.setBtnNarrative(View.VISIBLE);
            }

        }
        title = (TextView) findViewById(R.id.mainStoryTitle);
        title.setText(story.getTitle());
        searchImages(story);
        AppCache.getInstance().setPageOneDestroyed(false);
    }

    private void searchImages(final Story story){
        Service service = new Service("Loading resources...", StoryActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    if(!isLesson && pageToLoad == null){
                        searchLesson(story);
                    }
                    getImages(resp, story, true);
                }catch (Exception e){e.printStackTrace();}
            }
        });
        if(isLesson){
            StoryService.searchLessons(story.getId(), service);
        }else{
            StoryService.searchImages(story.getId(), service);
        }

    }
    private void searchLesson(final Story story){
        Service service = new Service("Loading resources...", StoryActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    getImages(resp, story, false);
                }catch (Exception e){e.printStackTrace();}
            }
        });
        StoryService.searchLessons(story.getId(), service);
    }
    public void getBitmaps(final Story story){
        Service service = new Service("Initializing resources...", StoryActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    List<Fragment> fragments = getFragments();
                    pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
                    ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
                    pager.setAdapter(pageAdapter);
                    if(pageToLoad != null){
                        pager.setCurrentItem(Integer.valueOf(pageToLoad) - 1);
                    }

                }catch (Exception e){e.printStackTrace();}
            }
        });
        StoryService.populateImages(story, service);
    }
    private void getImages(JSONObject resp, Story story,boolean executeBitmap){
        JSONArray arr = resp.optJSONArray("records");
        if(isLesson){
            if(arr == null) {
                title.setText("No available lesson.");
                fab.setVisibility(View.VISIBLE);
            }else{
                title.setText("Lesson for " + title.getText().toString());
            }
        }
        for( int i=0; i< arr.length(); i++){
            JSONObject obj = arr.optJSONObject(i);
            if(!executeBitmap){
                tempStory.addLesson(new Lesson(obj.optString("image")));
            }else{
                story.addImage(new Image(obj.optString("image")));
            }

        }
        if(executeBitmap){
            if(imageLoader != null){
                imageLoader.getQuestions(story.getId(), story);
            }
            getBitmaps(story);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
