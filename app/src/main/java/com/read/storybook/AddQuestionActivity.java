package com.read.storybook;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.read.storybook.model.Choice;
import com.read.storybook.model.Question;
import com.read.storybook.service.QuestionService;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.StoryService;
import com.read.storybook.util.AppConstants;
import com.read.storybook.util.Util;

import org.json.JSONObject;

public class AddQuestionActivity extends AppCompatActivity {
    EditText option1;
    EditText option2;
    EditText option3;
    EditText option4;
    EditText option5;
    EditText answerTxt;
    EditText questionTitle;
    TextView addQuestionErrMsg;
    String storyId;
    int ans = 0;
    private void clear(){
        option1.setText("");
        option2.setText("");
        option3.setText("");
        option4.setText("");
        option5.setText("");
        this.ans = 0;
        questionTitle.setText("");
        addQuestionErrMsg.setText("");
        addQuestionErrMsg.setVisibility(View.INVISIBLE);
        answerTxt.setText("");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        questionTitle = (EditText) findViewById(R.id.questionTitle);
        option1 = (EditText) findViewById(R.id.option1);
        option2 = (EditText) findViewById(R.id.option2);
        option3 = (EditText) findViewById(R.id.option3);
        option4 = (EditText) findViewById(R.id.option4);
        option5 = (EditText) findViewById(R.id.option5);
        storyId = getIntent().getStringExtra(AppConstants.STORY_ID);
        answerTxt = (EditText) findViewById(R.id.answerTxt);
        addQuestionErrMsg = (TextView) findViewById(R.id.addQuestionErrMsg);
        addQuestionErrMsg.setTextColor(Color.RED);
        addQuestionErrMsg.setVisibility(View.INVISIBLE);
        Button saveAndAddQuestion = (Button)findViewById(R.id.saveAndAddQuestion);
        saveAndAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String err = validate();
                if(err != null){
                    addQuestionErrMsg.setVisibility(View.VISIBLE);
                    addQuestionErrMsg.setText(err);
                }else{
                    addQuestion(false);
                }
            }
        });
        Button saveQuestion = (Button)findViewById(R.id.saveQuestion);
        saveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String err = validate();
                if(err != null){
                    addQuestionErrMsg.setVisibility(View.VISIBLE);
                    addQuestionErrMsg.setText(err);
                }else{
                    addQuestion(true);
                }
            }
        });

    }


    private String validate(){
        if(questionTitle.getText().toString().trim().equals("")){
            return "Question Title cannot be empty.";
        }
        if(answerTxt.getText().toString().trim().equals("")){
            return "Answer cannot be empty.";
        }
        int ans = 0;

        try{
            ans = Integer.parseInt(answerTxt.getText().toString());
        }catch (NumberFormatException e){
            return "Answer must be a number.";
        }
        if(ans > 5 || ans < 1){
            return "Answer must be a number 1 - 5.";
        }

        if(ans == 1){
            if(option1.getText().toString().trim().equals("")){
                return getAnserErrMsg(ans);
            }
        }
        if(ans == 2){
            if(option2.getText().toString().trim().equals("")){
                return getAnserErrMsg(ans);
            }
        }
        if(ans == 3){
            if(option3.getText().toString().trim().equals("")){
                return getAnserErrMsg(ans);
            }
        }
        if(ans == 4){
            if(option4.getText().toString().trim().equals("")){
                return getAnserErrMsg(ans);
            }
        }
        if(ans == 5){
            if(option5.getText().toString().trim().equals("")){
                return getAnserErrMsg(ans);
            }
        }
        this.ans = ans;
        return null;
    }

    private String getAnserErrMsg(int ans){
        return "Answer is " + ans + ". " + " Answer " + ans + " Text must not be empty.";
    }

    private Question createQuestion(){
        Question q = new Question();
        q.setId(Util.generateId());
        q.setActive(true);
        q.setDescription(questionTitle.getText().toString().trim());

        if(!option1.getText().toString().trim().equals("")){
            q.addChoice(addChoice(option1.getText().toString().trim(), this.ans == 1));
        }
        if(!option2.getText().toString().trim().equals("")){
            q.addChoice(addChoice(option2.getText().toString().trim(), this.ans == 2));
        }
        if(!option3.getText().toString().trim().equals("")){
            q.addChoice(addChoice(option3.getText().toString().trim(), this.ans == 3));
        }
        if(!option4.getText().toString().trim().equals("")){
            q.addChoice(addChoice(option4.getText().toString().trim(), this.ans == 4));
        }
        if(!option5.getText().toString().trim().equals("")){
            q.addChoice(addChoice(option5.getText().toString().trim(), this.ans == 5));
        }
        return q;
    }

    private Choice addChoice(String str, boolean isAns){
        Choice c = new Choice();
        c.setId(Util.generateId());
        c.setActive(true);
        c.setDescription(str);
        c.setAnswer(isAns);
        return  c;
    }

    private void addQuestion(final boolean saveAndExistBol){
        Service service = new Service("Adding Question...", AddQuestionActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    if (resp.getString("message").equals("Success")) {
                        clear();
                        if(saveAndExistBol){
                            finish();
                        }
                    }else{
                        addQuestionErrMsg.setTextColor(Color.RED);
                        addQuestionErrMsg.setVisibility(View.VISIBLE);
                        addQuestionErrMsg.setText(resp.getString("message"));
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
        QuestionService.create(storyId, createQuestion(), service);
    }

}



