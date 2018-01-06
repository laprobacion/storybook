package com.read.storybook;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.read.storybook.model.Level;
import com.read.storybook.model.User;
import com.read.storybook.service.LevelService;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.util.AppCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LevelsFragment extends Fragment{

    ListView lv;
    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.levels_layout, container, false);
        Intent myIntent = new Intent(getActivity(), LevelsActivity.class);
        myIntent.putExtra(LevelsActivity.IS_LESSON,String.valueOf(isLesson));
        startActivity(myIntent);
        return myView;
    }
    private boolean isLesson;

    public void setLesson(boolean lesson) {
        isLesson = lesson;
    }
}
