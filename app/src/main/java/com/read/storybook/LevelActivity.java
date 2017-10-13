package com.read.storybook;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.read.storybook.model.Level;
import com.read.storybook.model.User;
import com.read.storybook.service.LevelService;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.UserService;
import com.read.storybook.util.AppCache;
import com.read.storybook.util.Storage;

import org.json.JSONObject;

public class LevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        final TextView levelActErrMsg = (TextView) findViewById(R.id.levelActErrMsg);
        final TextView levelTxt = (TextView) findViewById(R.id.levelTxt);
        Button levelAddBtn = (Button)findViewById(R.id.levelAddBtn);
        levelAddBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //Log.i("MyApp",username.getText().toString() + password.getText().toString() + password2.getText().toString());
                String err = validate(levelTxt.getText().toString());
                if(err != null){
                    levelActErrMsg.setVisibility(View.VISIBLE);
                    levelActErrMsg.setText(err);
                    levelActErrMsg.setTextColor(Color.RED);
                }else {
                    Level level = new Level();
                    level.setActive(true);
                    level.setName(levelTxt.getText().toString());
                    register(level);
                }
            }
        });
        User user = AppCache.getInstance().getUser();
        if(!user.isAdmin()){
            levelAddBtn.setVisibility(View.INVISIBLE);
        }
    }

    private String validate(String name){
        if( !(name != null && !name.trim().equals("")) ){
            return "Name cannot be empty.";
        }
        return null;
    }
    private void register(final Level level){
        final TextView errorMsg = (TextView) findViewById(R.id.levelActErrMsg);
        Service service = new Service("Creating Level...", LevelActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    if (resp.getString("message").equals("Level was created.")) {
                        finish();
                    }else{
                        errorMsg.setTextColor(Color.RED);
                        errorMsg.setVisibility(View.VISIBLE);
                        errorMsg.setText(resp.getString("message"));
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
        LevelService.create(level, service);
    }
}
