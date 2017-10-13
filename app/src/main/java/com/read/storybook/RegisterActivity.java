package com.read.storybook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.mysql.jdbc.ExceptionInterceptor;
import com.read.storybook.model.Query;
import com.read.storybook.model.User;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.UserService;
import com.read.storybook.util.AppConstants;
import com.read.storybook.util.Encryptor;
import com.read.storybook.util.Storage;
import com.read.storybook.util.Util;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    ProgressDialog authProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final TextView errorMsg = (TextView) findViewById(R.id.errorMsg);


        final TextView username = (TextView) findViewById(R.id.username);
        final TextView password = (TextView) findViewById(R.id.password);
        final TextView password2 = (TextView) findViewById(R.id.password2);

        Button registerOkBtn = (Button)findViewById(R.id.registerOkBtn);
        registerOkBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                //Log.i("MyApp",username.getText().toString() + password.getText().toString() + password2.getText().toString());
                String err = validate(username.getText().toString(), password.getText().toString(), password2.getText().toString());
                if(err != null){
                    errorMsg.setTextColor(Color.RED);
                    errorMsg.setVisibility(View.VISIBLE);
                    errorMsg.setText(err);
                }else {
                    final User user = new User();
                    user.setUsername(username.getText().toString());
                    user.setAdmin(false);
                    user.setPassword(password.getText().toString());
                    user.setActive(true);

                    register(user);
                }
            }
        });
    }

    private void register(final User user){
        final TextView errorMsg = (TextView) findViewById(R.id.errorMsg);
        Service service = new Service("Adding user...", RegisterActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    if (resp.getString("message").equals("User was created.")) {
                        Storage.save(RegisterActivity.this.getApplicationContext(),user);
                        finish();
                    }else{
                        errorMsg.setTextColor(Color.RED);
                        errorMsg.setVisibility(View.VISIBLE);
                        errorMsg.setText(resp.getString("message"));
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
        UserService.register(user, service);
    }


    private String validate(final String username, String password1, String password2){
        if(username.trim().equals("")){
            return "Username cannot be empty";
        }
        if(password1.trim().equals("") || password2.trim().equals("")){
            return "Password cannot be empty";
        }
        if(!password1.equals(password2)){
            return "Password did not match.";
        }

        return null;
    }


}
