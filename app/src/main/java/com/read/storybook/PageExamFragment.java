package com.read.storybook;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.read.storybook.model.Question;
import com.read.storybook.model.Story;
import com.read.storybook.util.AppConstants;


public class PageExamFragment extends Fragment {

    public PageExamFragment() {
        // Required empty public constructor
    }

    public static final PageExamFragment newInstance(boolean isLast, Question question){
        PageExamFragment f = new PageExamFragment();
        Bundle bdl = new Bundle(2);
        bdl.putSerializable(AppConstants.QUESTIONS, question);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Question q = (Question) getArguments().getSerializable(AppConstants.QUESTIONS);
        View v = inflater.inflate(R.layout.fragment_page_exam, container, false);
        TextView textView = (TextView) v.findViewById(R.id.examTitle);
        textView.setText(q.getDescription());

        RelativeLayout relativeLayout = v.findViewById(R.id.examPagefragment_layout);

        return v;
    }

}
