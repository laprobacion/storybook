package com.read.storybook;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.read.storybook.model.User;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.UserService;
import com.read.storybook.util.AppCache;
import com.read.storybook.util.Encryptor;

import org.json.JSONObject;

public class AdminActivity extends AppCompatActivity {
    CheckBox isAdminCheckBox;
    Button btnSearch, btnUpdate;
    EditText usernameTxt;
    TextView msg;
    User searchedUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        isAdminCheckBox = (CheckBox) findViewById(R.id.isAdminCheckBox);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        usernameTxt = (EditText) findViewById(R.id.usernameTxt);
        msg = (TextView) findViewById(R.id.msg);
        msg.setText("Search User");
        isAdminCheckBox.setEnabled(false);
        btnUpdate.setEnabled(false);
        usernameTxt.setText("Username");
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!usernameTxt.isEnabled()){
                    usernameTxt.setEnabled(true);
                    btnSearch.setText("Search");
                    isAdminCheckBox.setEnabled(false);
                    isAdminCheckBox.setChecked(false);
                    btnUpdate.setEnabled(false);
                    return;
                }
                if(!usernameTxt.getText().toString().trim().equals("")){
                    searchedUser = new User();
                    searchedUser.setUsername(usernameTxt.getText().toString().trim());
                    search(searchedUser);
                    msg.setTextColor(Color.BLACK);
                    msg.setText("Searching ...");
                }
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchedUser.isAdmin() == isAdminCheckBox.isChecked()){
                    String adminMsg = searchedUser.isAdmin() ? " an admin" : " not an admin";
                    msg.setTextColor(Color.RED);
                    msg.setText("User is already " + adminMsg);
                }else{
                    searchedUser.setAdmin(isAdminCheckBox.isChecked());
                    update();
                }
            }
        });
    }

    private void search(final User user){
        Service service = new Service("Searching user...", AdminActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    if (resp.optString("message") != null && resp.optString("message").equals("Incorrect username or password.")) {
                        msg.setTextColor(Color.RED);
                        msg.setText("Unable to find user " + usernameTxt.getText().toString().trim());
                        isAdminCheckBox.setEnabled(false);
                        isAdminCheckBox.setChecked(false);
                        searchedUser = null;
                        btnUpdate.setEnabled(false);
                    }else{
                        usernameTxt.setEnabled(false);
                        btnSearch.setText("Search Again");
                        user.setAdmin(resp.optInt("isAdmin") == 1);
                        user.setId(resp.optString("id"));
                        msg.setText("User found!");
                        msg.setTextColor(Color.GREEN);
                        isAdminCheckBox.setEnabled(true);
                        isAdminCheckBox.setChecked(user.isAdmin());
                        btnUpdate.setEnabled(true);
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
        UserService.search(user, service);
    }

    private void update(){
        Service service = new Service("Updating user...", AdminActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    if (resp.optString("message") != null && resp.optString("message").equals("Error")) {
                        msg.setTextColor(Color.RED);
                        msg.setText("Unable to update user " + usernameTxt.getText().toString().trim());
                    }else{
                        msg.setTextColor(Color.GREEN);
                        msg.setText("User updated!");
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
        UserService.updateAdmin(searchedUser, service);
    }
}
