package com.read.storybook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.read.storybook.model.User;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.UserService;
import com.read.storybook.util.AppCache;
import com.read.storybook.util.Encryptor;
import com.read.storybook.util.Storage;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onResume() {
        super.onResume();
        // Checks if the user has previously signed in, no need to ask to register/sign in again.
        if( isSignedIn() ){
            ((Button)findViewById(R.id.registerBtn)).setVisibility(View.INVISIBLE); // Show Register button
            ((Button)findViewById(R.id.signInBtn)).setVisibility(View.INVISIBLE);   // Show SignIn button
            ((DrawerLayout) findViewById(R.id.drawer_layout)).setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED ); // Unlock the Nagivation Drawer

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View header=navigationView.getHeaderView(0);
            // Set the username in Navigation Drawer header
            TextView name = (TextView)header.findViewById(R.id.headerName);
            name.setText(Storage.load(getApplicationContext()));
        }else{

        }

    }

    //Checks if the user has previously signed in.
    private boolean isSignedIn(){
        String username = Storage.load(getApplicationContext());
        if(AppCache.getInstance().getUser() == null){
            User user = new User();
            user.setUsername(username);
            login(user);
        }
        return( username != null && !username.equals(""));
    }

    // Get the username saved in the mobile and retrieve the User information.
    private void login(final User user){
        final TextView errorMsg = (TextView) findViewById(R.id.mainContentErrMsg);
        Service service = new Service("Searching user...", MainActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    if (resp.optString("message") != null && resp.optString("message").equals("Incorrect username or password.")) {
                       // errorMsg.setTextColor(Color.RED);
                        //errorMsg.setVisibility(View.VISIBLE);
                        //errorMsg.setText("Something is wrong!");
                    }else{
                        user.setActive(resp.optInt("isActive") == 1);
                        user.setAdmin(resp.optInt("isAdmin") == 1);
                        user.setId(resp.optString("id"));
                        String pass = resp.optString("password").replace("\n","");
                        user.setPassword(Encryptor.decrypt(pass));

                        AppCache.getInstance().setUser(user);
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
        UserService.search(user, service);
    }

    //Draw the Page/Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button registerBtn = (Button)findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent myIntent = new Intent(MainActivity.this, RegisterActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });


        Button signInBtn = (Button)findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent myIntent = new Intent(MainActivity.this, SignInActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        if( !isSignedIn()){
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            User user = AppCache.getInstance().getUser();
            if(user.isAdmin()){
                startActivity(new Intent(this, AdminActivity.class));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Navigation Drawer links
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_profile_layout) {
            LevelsFragment levelsFragment = new LevelsFragment();
            levelsFragment.setLesson(true);
            fragmentManager.beginTransaction().replace(R.id.content_frame, levelsFragment).commit();
        } else if (id == R.id.nav_score_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ScoreFragment()).commit();
        } else if (id == R.id.nav_stories_layout) {
            LevelsFragment levelsFragment = new LevelsFragment();
            levelsFragment.setLesson(false);
            fragmentManager.beginTransaction().replace(R.id.content_frame, levelsFragment).commit();
        } else if (id == R.id.nav_top_scores_layout) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new TopScoresFragment()).commit();
        } else if (id == R.id.nav_log_out_layout) {
            new AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialog, int which) {
                            Storage.remove(MainActivity.this,new User());
                            startActivity(new Intent(MainActivity.this,MainActivity.class));
                            finish();
                        }
                    })
                    .create()
                    .show();

        }else if (id == R.id.nav_updates) {
            startActivity(new Intent(MainActivity.this,UpdatesActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
