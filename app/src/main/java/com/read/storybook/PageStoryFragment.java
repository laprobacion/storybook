package com.read.storybook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.read.storybook.model.Question;
import com.read.storybook.model.Story;
import com.read.storybook.util.AppConstants;

import java.util.List;


public class PageStoryFragment extends Fragment {

    public static final String PAGE_TITLE = "PAGE_TITLE";
    public static final String IMAGE = "IMAGE";
    public static final String IS_LAST = "IS_LAST";
    public static final String STORY = "STORY";
    public static final String PAGING = "PAGING";

    public PageStoryFragment() {    }

    public static final PageStoryFragment newInstance(String message, Bitmap bitmap, boolean isLast, Story story, String paging)
    {
        PageStoryFragment f = new PageStoryFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(PAGE_TITLE, message);
        bdl.putParcelable(IMAGE, bitmap);
        bdl.putBoolean(IS_LAST,isLast);
        bdl.putString(PAGING, paging);
        if(isLast) {
            bdl.putSerializable(STORY, story);
        }
        f.setArguments(bdl);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String message = getArguments().getString(PAGE_TITLE);
        Bitmap bitmap = getArguments().getParcelable(IMAGE);
        boolean isLast = getArguments().getBoolean(IS_LAST);
        final Story story = isLast ? (Story)getArguments().getSerializable(STORY) : null;
        View v = inflater.inflate(R.layout.fragment_page_story, container, false);
        TextView messageTextView = (TextView)v.findViewById(R.id.storyTitle);
        messageTextView.setText("");
        ImageView iv = (ImageView) v.findViewById(R.id.imageStory);
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
        return v;
    }

}
