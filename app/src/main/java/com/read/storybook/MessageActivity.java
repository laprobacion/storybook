package com.read.storybook;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.read.storybook.model.Story;
import com.read.storybook.util.AppConstants;

public class MessageActivity extends AppCompatActivity {
    public static final String QUESTION_ANS = "QUESTION_ANS";
    ConstraintLayout messageLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        messageLayout = findViewById(R.id.messageLayout);
        boolean ans= getIntent().getBooleanExtra(QUESTION_ANS,false);
        final Story s = (Story) getIntent().getSerializableExtra(AppConstants.STORY_OBJ);
        final String pageLeft = getIntent().getStringExtra(LessonActivity.CURRENT_PAGE);

        if(ans){
            messageLayout.setBackground(getResources().getDrawable(R.drawable.correct_ans));
        }else{
            messageLayout.setBackground(getResources().getDrawable(R.drawable.incorrect_ans));
        }
        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /**
                Intent i = new Intent(MessageActivity.this, StoryActivity.class);
                i.putExtra(LevelsActivity.IS_LESSON,"false");
                i.putExtra(LessonActivity.CURRENT_PAGE,pageLeft);
                i.putExtra(AppConstants.STORY_OBJ,s);
                startActivity(i);
                 **/
            }
        });
    }
}
