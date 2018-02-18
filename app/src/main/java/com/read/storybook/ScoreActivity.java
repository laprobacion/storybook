package com.read.storybook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.UserStoryService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class ScoreActivity extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        tv = findViewById(R.id.scorePerStory);
        viewScore();
    }

    private void viewScore(){
        Service service = new Service("Getting Score...", ScoreActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    getUsers(resp);
                }catch (Exception e){e.printStackTrace();}
            }
        });
        UserStoryService.searchByName( service);
    }

    private void getUsers(JSONObject resp){
        StringBuilder sb = new StringBuilder();
        JSONArray arr = resp.optJSONArray("records");
        for( int i=0; i< arr.length(); i++){
            JSONObject obj = arr.optJSONObject(i);
            int percent = getPercentage(obj.optInt("SCORE"), obj.optInt("ITEM"));
            sb.append(obj.optString("TITLE") + " = " + percent + "% \r\n");
        }
        tv.setText(sb.toString());
    }

    public int getPercentage(int s, int i){
        Double score = Double.valueOf((double)s / (double)i);
        DecimalFormat df = new DecimalFormat("#.####");

        return ((Double)(Double.parseDouble(df.format(score)) * ((double)100))).intValue();
    }
}
