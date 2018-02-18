package com.read.storybook;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.UserScoreService;
import com.read.storybook.service.UserStoryService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class TopScoresActivity extends AppCompatActivity {
    TextView usersScores;
    RelativeLayout topScoresLayout;
    int top = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_scores);
        usersScores = findViewById(R.id.usersScores);
        topScoresLayout = findViewById(R.id.topScoresLayout);
        search();
    }

    private void search(){
        Service service = new Service("Getting Top Scores", TopScoresActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    getUsers(resp);
                }catch (Exception e){e.printStackTrace();}
            }
        });
        UserStoryService.search(service);
    }

    private void getUsers(JSONObject resp){
        JSONArray arr = resp.optJSONArray("records");
        UserScoreService userScoreService = new UserScoreService();
        for( int i=0; i< arr.length(); i++){
            JSONObject obj = arr.optJSONObject(i);
            userScoreService.addUserScore(obj.optString("username"),obj.optString("USER_ID"),obj.optInt("SCORE"),obj.optInt("ITEM"));
            //obj.optString("STORY_ID")
        }
        populateList(userScoreService);
    }

    private void populateList(UserScoreService userScoreService){
        int ctr = 1;
        String space = "         ";
        for(Map.Entry<String,Integer> pair : userScoreService.getTopScores().entrySet()){
            renderList( space + ctr + space + space + userScoreService.getUsername(pair.getKey()) + " " + pair.getValue() + "%");
            ctr++;
        }
    }
    private void renderList(String text){
        createTextView(0,text,20,top,0);//.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        top += 75;
    }
    public TextView createTextView(int id, String str, int textSize, int top, int left){
        TextView tv = new TextView(this);

        ShapeDrawable sd = new ShapeDrawable();
        sd.setShape(new RectShape());
        sd.getPaint().setColor(Color.argb(255, 0, 90, 0));
        sd.getPaint().setStrokeWidth(10f);
        sd.getPaint().setStyle(Paint.Style.STROKE);

        tv.setBackground(sd);
        tv.setId(id);
        tv.setTextSize(textSize);
        tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextColor(Color.argb(255, 255, 255, 255));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(left,top,0,0);
        tv.setLayoutParams(lp);
        tv.setBackgroundColor(Color.argb(255, 0, 200, 0));
        tv.setText(str);
        topScoresLayout.addView(tv);
        return tv;
    }

}
