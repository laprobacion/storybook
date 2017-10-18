package com.read.storybook;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.read.storybook.model.Choice;
import com.read.storybook.model.Question;
import com.read.storybook.model.Story;
import com.read.storybook.service.ChoiceService;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.UserStoryService;
import com.read.storybook.util.AppCache;
import com.read.storybook.util.AppConstants;

import org.json.JSONObject;

import java.util.List;


public class PageExamFragment extends Fragment {
    Button exampPageSubmitBtn;
    RadioButton bt1;
    RadioButton bt2;
    RadioButton bt3;
    RadioButton bt4;
    RadioButton bt5;
    List<Choice> choices;
    TextView examPagePaging;
    boolean isCorrect;
    boolean isLast;
    String storyId;
    public PageExamFragment() {
        // Required empty public constructor
    }

    public static final PageExamFragment newInstance(String storyId, String paging, boolean isLast, Question question){
        PageExamFragment f = new PageExamFragment();
        Bundle bdl = new Bundle(2);
        bdl.putSerializable(AppConstants.QUESTIONS, question);
        bdl.putString(AppConstants.EXAM_PAGING, paging);
        bdl.putString(AppConstants.STORY_ID, storyId);
        bdl.putBoolean(AppConstants.EXAM_IS_LAST, isLast);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Question q = (Question) getArguments().getSerializable(AppConstants.QUESTIONS);
        storyId = getArguments().getString(AppConstants.STORY_ID);
        this.isLast = getArguments().getBoolean(AppConstants.EXAM_IS_LAST);
        View v = inflater.inflate(R.layout.fragment_page_exam, container, false);
        TextView textView = (TextView) v.findViewById(R.id.examQuestion);
        textView.setText(q.getDescription());
        bt1 = v.findViewById(R.id.radioButton1);
        bt2 = v.findViewById(R.id.radioButton2);
        bt3 = v.findViewById(R.id.radioButton3);
        bt4 = v.findViewById(R.id.radioButton4);
        bt5 = v.findViewById(R.id.radioButton5);
        examPagePaging = v.findViewById(R.id.examPagePaging);
        examPagePaging.setText(getArguments().getString(AppConstants.EXAM_PAGING));
        choices = ChoiceService.randomizeChoice(q);
        populateChoices();
        exampPageSubmitBtn = v.findViewById(R.id.exampPageSubmitBtn);
        if(isLast){
            exampPageSubmitBtn.setVisibility(View.VISIBLE);
        }
        RelativeLayout relativeLayout = v.findViewById(R.id.examPagefragment_layout);
        exampPageSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ExamActivity act =(ExamActivity) getActivity();
                int correctCount = 0;
                for(Fragment f : act.getFragments()){
                    ((PageExamFragment)f).setCorrect();
                    if(((PageExamFragment)f).isCorrect){
                        correctCount++;
                    }
                }
                save(correctCount, act.getFragments().size());
            }
        });
        return v;
    }
    private void save(int score, int item){
        Service service = new Service("Saving Exam", getActivity(), new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    if (resp.getString("message").equals("Success")) {
                        getActivity().finish();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
        UserStoryService.save(storyId, AppCache.getInstance().getUser().getId(), score, item, service);
    }
    public boolean isCorrect(){
        return isCorrect;
    }
    private void setCorrect(){
        if(bt1.isChecked()){
            isCorrect = checkAnswer(bt1.getText().toString());
        }
        if(bt2.isChecked()){
            isCorrect = checkAnswer(bt2.getText().toString());
        }
        if(bt3.isChecked()){
            isCorrect = checkAnswer(bt3.getText().toString());
        }
        if(bt4.isChecked()){
            isCorrect = checkAnswer(bt4.getText().toString());
        }
        if(bt5.isChecked()){
            isCorrect = checkAnswer(bt5.getText().toString());
        }
    }

    private boolean checkAnswer(String txt){
        for(Choice c : choices){
            if(c.getDescription().equals(txt) && c.isAnswer()){
                return true;
            }
        }
        return false;
    }
    private void populateChoices(){
        if(choices.size() > 0){
            bt1.setText(choices.get(0).getDescription());
        }
        if(choices.size() > 1){
            bt2.setText(choices.get(1).getDescription());
            bt2.setVisibility(View.VISIBLE);
        }
        if(choices.size() > 2){
            bt3.setText(choices.get(2).getDescription());
            bt3.setVisibility(View.VISIBLE);
        }
        if(choices.size() > 3){
            bt4.setText(choices.get(3).getDescription());
            bt4.setVisibility(View.VISIBLE);
        }
        if(choices.size() > 4){
            bt5.setText(choices.get(4).getDescription());
            bt5.setVisibility(View.VISIBLE);
        }
    }
}
