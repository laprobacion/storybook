package com.read.storybook;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.read.storybook.model.User;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.UserService;
import com.read.storybook.util.Encryptor;
import com.read.storybook.util.Storage;

import org.json.JSONObject;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        final TextView signUsername = (TextView) findViewById(R.id.signUsername);
        final TextView signPassword = (TextView) findViewById(R.id.signPassword);
        final TextView signInErrMsg = (TextView) findViewById(R.id.signInErrMsg);
        Button signInButton = (Button)findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                String err = validate(signUsername.getText().toString(), signPassword.getText().toString());
                if( err == null){
                    User user = new User();
                    user.setUsername(signUsername.getText().toString());
                    user.setPassword(signPassword.getText().toString());
                    login(user);
                }else{
                    signInErrMsg.setTextColor(Color.RED);
                    signInErrMsg.setVisibility(View.VISIBLE);
                    signInErrMsg.setText(err);
                }

            }
        });
    }
    private void login(final User user){
        final TextView errorMsg = (TextView) findViewById(R.id.signInErrMsg);
        Service service = new Service("Signing in...", SignInActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    if (resp.optString("message") != null && resp.optString("message").equals("Incorrect username or password.")) {
                        errorMsg.setTextColor(Color.RED);
                        errorMsg.setVisibility(View.VISIBLE);
                        errorMsg.setText(resp.getString("message"));
                    }else{
                        user.setId(resp.optString("id"));
                        user.setUsername(resp.optString("username"));
                        user.setActive(resp.optInt("isActive") == 1);
                        user.setAdmin(resp.optInt("isAdmin") == 1);
                        Storage.save(SignInActivity.this.getApplicationContext(),user);
                        finish();
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
        UserService.login(user, service);
    }

    private String validate(String username, String password){
        if(username.trim().equals("")){
            return "Username cannot be empty";
        }
        if(password.trim().equals("")){
            return "Password cannot be empty";
        }
        return null;
    }
}
