package com.read.storybook;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.read.storybook.Image.ImageLoader;
import com.read.storybook.model.Image;
import com.read.storybook.model.Lesson;
import com.read.storybook.model.Sound;
import com.read.storybook.model.Story;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.StoryService;
import com.read.storybook.util.AppCache;
import com.read.storybook.util.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.read.storybook.util.AppConstants.LESSON_FOR;

public class LessonActivity extends AppCompatActivity {

    Story story;
    TextView title;
    MyPageAdapter pageAdapter;
    Story tempStory = new Story();
    ImageLoader imageLoader;
    boolean isLesson;
    List<Sound> soundList;
    public static final String CURRENT_PAGE = "CURRENT_PAGE";
    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        String pageLeft = getIntent().getStringExtra(LessonActivity.CURRENT_PAGE);
        story.addImage(new Image("blank"));
        for(int i=0;i<story.getImages().size();i++){
            String page = (i + 1) + " of " + story.getImages().size();
            fList.add(PageStoryFragment.newInstance(title.getText().toString(), story.getImages().get(i), (i + 1) == story.getImages().size(), tempStory, page, false, true,pageLeft,soundList));
        }
        return fList;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        story = (Story)getIntent().getSerializableExtra(AppConstants.STORY_OBJ);
        tempStory.setSoundList(story.getSoundList());
        tempStory.setTitle(story.getTitle());
        tempStory.setId(story.getId());
        title = (TextView) findViewById(R.id.mainStoryTitle);
        title.setText(story.getTitle());
        searchLessonNarratives();
        AppCache.getInstance().setPageOneDestroyed(false);
        imageLoader = new ImageLoader(this, story, tempStory, true);
    }

    private void searchLessonNarratives(){
        Service service = new Service("Searching Narratives", LessonActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    JSONArray arr = resp.optJSONArray("records");
                    if(arr != null && arr.length() > 0){
                        soundList = new ArrayList<Sound>();
                        for( int i=0; i< arr.length(); i++){
                            JSONObject obj = arr.optJSONObject(i);
                            Sound sound = new Sound(obj.optString("narrative"));
                            sound.setPriority(obj.optString("priority"));
                            soundList.add(sound);
                        }
                    }
                    searchImages(story);
                }catch (Exception e){e.printStackTrace();}
            }
        });
        StoryService.searchNarratives(story.getId(), service);
    }
    private void searchImages(final Story story){
        Service service = new Service("Loading resources...", LessonActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    getImages(resp, story, true);
                }catch (Exception e){e.printStackTrace();}
            }
        });
            StoryService.searchLessons(story.getId(), service);
    }
    public void getBitmaps(final Story story){
        Service service = new Service("Initializing resources...", LessonActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    List<Fragment> fragments = getFragments();
                    pageAdapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
                    ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
                    pager.setAdapter(pageAdapter);
                }catch (Exception e){e.printStackTrace();}
            }
        });
        StoryService.populateImages(story, service);
    }
    private void getImages(JSONObject resp, Story story,boolean executeBitmap){
        JSONArray arr = resp.optJSONArray("records");
        title.setText(LESSON_FOR + " " + title.getText().toString());
        for( int i=0; i< arr.length(); i++){
            JSONObject obj = arr.optJSONObject(i);
            story.addImage(new Image(obj.optString("image")));
        }
        getBitmaps(story);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
