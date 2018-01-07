package com.read.storybook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.read.storybook.model.Question;
import com.read.storybook.model.Story;
import com.read.storybook.util.AppCache;
import com.read.storybook.util.AppConstants;
import com.read.storybook.util.Util;

import java.util.List;
import java.util.Random;


public class PageStoryFragment extends Fragment {

    public static final String PAGE_TITLE = "PAGE_TITLE";
    public static final String IMAGE = "IMAGE";
    public static final String IS_LAST = "IS_LAST";
    public static final String STORY = "STORY";
    public static final String PAGING = "PAGING";
    public static final String PAGE_TO_SHOW = "PAGE_TO_SHOW";
    public static final String PAGE_LEFT = "PAGE_LEFT";
    public static final String IS_LESSON = "IS_LESSON";

    public PageStoryFragment() {    }

    public static final PageStoryFragment newInstance(String message, Bitmap bitmap, boolean isLast, Story story, String paging, boolean hasLesson, boolean isLesson, String pageLeft)
    {
        PageStoryFragment f = new PageStoryFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(PAGE_TITLE, message);
        bdl.putParcelable(IMAGE, bitmap);
        bdl.putBoolean(IS_LAST,isLast);
        bdl.putString(PAGING, paging);
        bdl.putString(PAGE_LEFT, pageLeft);
        bdl.putBoolean(PAGE_TO_SHOW,hasLesson);
        bdl.putBoolean(IS_LESSON,isLesson);
        bdl.putSerializable(STORY, story);
        f.setArguments(bdl);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String message = getArguments().getString(PAGE_TITLE);
        Bitmap bitmap = getArguments().getParcelable(IMAGE);
        boolean isLast = getArguments().getBoolean(IS_LAST);
        final Story story = (Story)getArguments().getSerializable(STORY);
        String pageLeft = getArguments().getString(PAGE_LEFT);
        View v = inflater.inflate(R.layout.fragment_page_story, container, false);
        TextView messageTextView = (TextView)v.findViewById(R.id.storyTitle);
        messageTextView.setText("");
        ImageView iv = (ImageView) v.findViewById(R.id.imageStory);
        RelativeLayout parent = (RelativeLayout)v.findViewById(R.id.myfragment_layout);
        iv.setImageBitmap(bitmap);
        iv.setMinimumWidth(3000);
        iv.setMinimumHeight(1000);
        Button takeExam =  (Button)v.findViewById(R.id.takeExam);
        if(isLast && story !=null && story.getQuestions() != null && story.getQuestions().size() > 0){
            takeExam.setVisibility(View.VISIBLE);
        }
        takeExam.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(getActivity(), ExamActivity.class);
                myIntent.putExtra(AppConstants.QUESTIONS,story);
                startActivity(myIntent);
               getActivity().finish();
            }
        });
        TextView pageNo = v.findViewById(R.id.paging);

        pageNo.setText(getArguments().getString(PAGING));
        if(getArguments().getString(PAGING).split(" of ")[0].equals("1")){
            if(!AppCache.getInstance().isPageOneDestroyed()){
                Util.createFadingWindow(parent,"Swipe left or right to turn page.",120,1400,200,350);
            }
        }
        if(getArguments().getBoolean(PAGE_TO_SHOW)){
            createWindow(40,parent, "Lesson Time!",story,pageNo.getText().toString(), false,null);
            parent.invalidate();
        }
        if(getArguments().getBoolean(IS_LESSON) && isLast){
            createWindow(30,parent, "You have finished the ",story,pageNo.getText().toString(), true,pageLeft);
            parent.invalidate();
        }
        return v;
    }

    private void createWindow(int textSize, final RelativeLayout parent, String str, final Story s, final String page, final boolean isLesson, final String pageLeft){
        final RelativeLayout window = Util.createWindowRelativeLayout(parent,isLesson);
        TextView message = Util.createTextView(str,textSize,20,0,window,RelativeLayout.LayoutParams.MATCH_PARENT);
        message.setTextColor(Color.argb(200, 255, 255, 255));
        message.setAllCaps(false);
        String txtButton = "OK";
        TextView button = null;
        if(isLesson){
            TextView subMessage = Util.createTextView("lesson!",textSize,100,0,window,RelativeLayout.LayoutParams.MATCH_PARENT);
            subMessage.setTextColor(Color.argb(200, 255, 255, 255));
            subMessage.setAllCaps(false);
            txtButton = " Return to story ";
            button = Util.createTextView(txtButton,20,215,220,window,RelativeLayout.LayoutParams.WRAP_CONTENT);
        }else{
            button = Util.createTextView(txtButton,30,170,320,window,RelativeLayout.LayoutParams.WRAP_CONTENT);
            button.getLayoutParams().width = 150;
        }
        button.setTextColor(Color.argb(180, 255, 255, 255));
        button.setBackgroundColor(Color.argb(200,108,186,249));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = null;
                s.setLessons(null);
                if(!isLesson){
                    myIntent = new Intent(parent.getContext(), LessonActivity.class);
                    myIntent.putExtra(LessonActivity.CURRENT_PAGE,page.split(" of ")[0]);
                }else{
                    myIntent = new Intent(parent.getContext(), StoryActivity.class);
                    myIntent.putExtra(LevelsActivity.IS_LESSON,"false");
                    myIntent.putExtra(LessonActivity.CURRENT_PAGE,pageLeft);
                }
                myIntent.putExtra(AppConstants.STORY_OBJ,s);
                parent.getContext().startActivity(myIntent);
                getActivity().finish();

            }
        });
    }



}
