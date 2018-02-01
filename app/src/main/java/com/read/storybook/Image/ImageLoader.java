package com.read.storybook.Image;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.read.storybook.AddLessonNarrativeActivity;
import com.read.storybook.AddQuestionActivity;
import com.read.storybook.LessonActivity;
import com.read.storybook.R;
import com.read.storybook.StoryActivity;
import com.read.storybook.model.Choice;
import com.read.storybook.model.Lesson;
import com.read.storybook.model.Question;
import com.read.storybook.model.Story;
import com.read.storybook.model.User;
import com.read.storybook.service.QuestionService;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.util.AppCache;
import com.read.storybook.util.AppConstants;
import com.read.storybook.util.Player;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ImageLoader {
    Button createExam,btnNarrative,btnLesson,pageAdd;
    Activity activity;
    Story story;
    Story tempStory;
    boolean isLesson;

    public ImageLoader(Activity activity, Story story, Story tempStory, boolean isLesson){
        this.activity = activity;
        this.story = story;
        this.tempStory = tempStory;
        this.isLesson = isLesson;
        btnNarrative = (Button) activity.findViewById(R.id.btnNarrative);
        pageAdd = (Button) activity.findViewById(R.id.pageAdd);
        createExam = (Button) activity.findViewById(R.id.createExam);
        if(!isLesson){
            btnLesson = (Button) activity.findViewById(R.id.btnLesson);
        }
        if(AppCache.getInstance().getUser().isAdmin()){
            btnNarrative.setVisibility(View.VISIBLE);
            pageAdd.setVisibility(View.VISIBLE);
            if(!isLesson)
                createExam.setVisibility(View.VISIBLE);
        }else {
            btnNarrative.setVisibility(View.INVISIBLE);
            pageAdd.setVisibility(View.INVISIBLE);
            createExam.setVisibility(View.INVISIBLE);
        }
        //remove this
        setOnClickListeners();
    }

    public void setBtnLessonVisible(int v){
        btnLesson.setVisibility(View.INVISIBLE);
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

    public void getQuestions(final String storyId, final Story story){
        Service service = new Service("Loading resources...", activity, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    tempStory.setId(storyId);
                    tempStory.setQuestions( createQuestions(resp));
                    User user = AppCache.getInstance().getUser();
                    if(user.isAdmin()){
                        createExam.setVisibility(View.VISIBLE);
                    }
                    if(activity instanceof StoryActivity){
                        ((StoryActivity)activity).getBitmaps(story);
                    }else{
                        ((LessonActivity)activity).getBitmaps(story);
                    }

                }catch (Exception e){e.printStackTrace();}
            }
        });
        QuestionService.getQuestions(storyId, service);
    }




    private void setOnClickListeners(){

        btnNarrative.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(activity, AddLessonNarrativeActivity.class);
                myIntent.putExtra(AppConstants.STORY_ID,story.getId());
                myIntent.putExtra(AppConstants.STORY_NARRATIVE,story.getId());
                myIntent.putExtra(AppConstants.STORY_IS_STORY,String.valueOf(!ImageLoader.this.isLesson));
                activity.startActivity(myIntent);
                activity.finish();
            }
        });
        if(pageAdd != null){
            pageAdd.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent myIntent = new Intent(activity, AddLessonNarrativeActivity.class);
                    myIntent.putExtra(AppConstants.STORY_ID,story.getId());
                    if(!isLesson){
                        myIntent.putExtra(AppConstants.STORY_ADD,story.getId());
                    }
                    myIntent.putExtra(AppConstants.STORY_LESSON,story.getId());
                    activity.startActivity(myIntent);
                    activity.finish();
                }
            });
        }

        if(isLesson){
            return;
        }
        btnLesson.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(activity, AddLessonNarrativeActivity.class);
                myIntent.putExtra(AppConstants.STORY_ID,story.getId());
                myIntent.putExtra(AppConstants.STORY_LESSON,story.getId());
                activity.startActivity(myIntent);
                activity.finish();
            }
        });
        createExam.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(activity, AddQuestionActivity.class);
                myIntent.putExtra(AppConstants.STORY_ID,story.getId());
                activity.startActivity(myIntent);
                activity.finish();
            }
        });

    }

}
