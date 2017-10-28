package com.read.storybook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.read.storybook.model.Choice;
import com.read.storybook.model.Image;
import com.read.storybook.model.Question;
import com.read.storybook.model.Story;
import com.read.storybook.model.User;
import com.read.storybook.service.QuestionService;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.StoryService;
import com.read.storybook.util.AppCache;
import com.read.storybook.util.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class StoryActivity extends FragmentActivity{
    Story story;
    TextView title;
    MyPageAdapter pageAdapter;
    Story tempStory = new Story();
    Button createExam;
    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        int ctr = 0;

        for(Image i : story.getImages()){
            String page = (ctr + 1) + " of " + story.getImages().size();
            fList.add(PageStoryFragment.newInstance(title.getText().toString(), i.getBitmap(), (ctr + 1) == story.getImages().size(), tempStory, page));
            ctr++;
        }
        return fList;
    }
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        createExam = (Button) findViewById(R.id.createExam);
        createExam.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(StoryActivity.this, AddQuestionActivity.class);
                myIntent.putExtra(AppConstants.STORY_ID,story.getId());
                StoryActivity.this.startActivity(myIntent);
                finish();
            }
        });

        title = (TextView) findViewById(R.id.mainStoryTitle);
        story = (Story)getIntent().getSerializableExtra(AppConstants.STORY_OBJ);
        title.setText(story.getTitle());
        searchImages(story);

    }

    private void searchImages(final Story story){
        Service service = new Service("Loading resources...", StoryActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    getImages(resp, story);
                }catch (Exception e){e.printStackTrace();}
            }
        });
        StoryService.searchImages(story.getId(), service);
    }
    private void getBitmaps(final Story story){
        Service service = new Service("Initializing resources...", StoryActivity.this, new ServiceResponse() {
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
    private void getImages(JSONObject resp, Story story){
        JSONArray arr = resp.optJSONArray("records");
        for( int i=0; i< arr.length(); i++){
            JSONObject obj = arr.optJSONObject(i);
            story.addImage(new Image(obj.optString("image")));
        }
        getQuestions(story.getId(), story);
    }

    private List<Question> createQuestions(JSONObject resp){
        List<Question> questions = new ArrayList<Question>();
        try {
            JSONArray arr = resp.optJSONArray("records");
            if(arr == null){
                return  null;
            }
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.optJSONObject(i);
                Question q = new Question();
                q.setId(obj.optString("QUESTION_ID"));
                q.setDescription(obj.optString("DESCRIPTION"));
                q.setActive(obj.optString("ISACTIVE").equals("1"));
                JSONArray arr2 = new JSONObject(obj.optString("choices")).optJSONArray("choices");
                for (int j = 0; j < arr2.length(); j++) {
                    JSONObject obj2 = arr2.optJSONObject(j);
                    Choice c = new Choice();
                    c.setId(obj2.optString("CHOICE_ID"));
                    c.setDescription(obj2.optString("DESCRIPTION"));
                    c.setActive(obj2.optString("isActive").equals("1"));
                    c.setAnswer(obj2.optString("isAnswer").equals("1"));
                    q.addChoice(c);
                }
                questions.add(q);
            }
        }catch (Exception e){ e.printStackTrace();}
        return  questions;
    }
    private void getQuestions(final String storyId, final Story story){
        Service service = new Service("Loading resources...", StoryActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    tempStory.setId(storyId);
                    tempStory.setQuestions( createQuestions(resp));
                    User user = AppCache.getInstance().getUser();
                    if(user.isAdmin() && tempStory != null && tempStory.getQuestions() != null && tempStory.getQuestions().size() == 0){
                        createExam.setVisibility(View.VISIBLE);
                    }
                    getBitmaps(story);
                }catch (Exception e){e.printStackTrace();}
            }
        });
        QuestionService.getQuestions(storyId, service);
    }

}
