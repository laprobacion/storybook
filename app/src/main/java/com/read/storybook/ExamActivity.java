package com.read.storybook;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.read.storybook.model.Question;
import com.read.storybook.model.Story;
import com.read.storybook.service.QuestionService;
import com.read.storybook.util.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class ExamActivity extends AppCompatActivity {
    MyPageAdapter pageAdapter;
    Story story;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        story = (Story) getIntent().getSerializableExtra(AppConstants.QUESTIONS);
        pageAdapter = new MyPageAdapter(getSupportFragmentManager(), getFragments());
        ViewPager pager = (ViewPager)findViewById(R.id.examviewpager);
        pager.setAdapter(pageAdapter);
    }
    private List<Fragment> getFragments() {
        List<Fragment> fList = new ArrayList<Fragment>();
        int ctr = 0;

        for(Question q : QuestionService.randomizeQuestions(story)){
            fList.add(PageExamFragment.newInstance( (ctr + 1) == story.getQuestions().size(), q));
            ctr++;
        }
        return fList;
    }
}
