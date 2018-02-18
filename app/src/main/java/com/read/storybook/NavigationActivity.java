package com.read.storybook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.read.storybook.model.User;
import com.read.storybook.util.Storage;

public class NavigationActivity extends AppCompatActivity {
    Button stories,lesson, myScores,topScores,updates,about,logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        stories = (Button) findViewById(R.id.stories);
        Drawable storiesDrawable = ContextCompat.getDrawable(NavigationActivity.this,R.drawable.nav_stories);
        storiesDrawable.setBounds(0,0,100,100);
        stories.setCompoundDrawables(storiesDrawable,null, null, null);
        stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NavigationActivity.this, LevelsActivity.class);
                myIntent.putExtra(LevelsActivity.IS_LESSON,String.valueOf(false));
                startActivity(myIntent);
            }
        });

        lesson = (Button) findViewById(R.id.lesson);
        Drawable lessonDrawable = ContextCompat.getDrawable(NavigationActivity.this,R.drawable.nav_lessons);
        lessonDrawable.setBounds(0,0,100,100);
        lesson.setCompoundDrawables(lessonDrawable,null, null, null);
        lesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(NavigationActivity.this, LevelsActivity.class);
                myIntent.putExtra(LevelsActivity.IS_LESSON,String.valueOf(true));
                startActivity(myIntent);
            }
        });

        myScores = (Button) findViewById(R.id.myScores);
        Drawable myScoresDrawable = ContextCompat.getDrawable(NavigationActivity.this,R.drawable.nav_myscores);
        myScoresDrawable.setBounds(0,0,100,100);
        myScores.setCompoundDrawables(myScoresDrawable,null, null, null);
        myScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavigationActivity.this,ScoreActivity.class));
            }
        });

        topScores = (Button) findViewById(R.id.topScores);
        Drawable topScoresDrawable = ContextCompat.getDrawable(NavigationActivity.this,R.drawable.nav_topscores);
        topScoresDrawable.setBounds(0,0,100,100);
        topScores.setCompoundDrawables(topScoresDrawable,null, null, null);
        topScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavigationActivity.this,TopScoresActivity.class));
            }
        });

        updates = (Button) findViewById(R.id.updates);
        Drawable updatesDrawable = ContextCompat.getDrawable(NavigationActivity.this,R.drawable.nav_update);
        updatesDrawable.setBounds(0,0,80,80);
        updates.setCompoundDrawables(updatesDrawable,null, null, null);
        updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NavigationActivity.this,UpdatesActivity.class));
            }
        });
        about = (Button) findViewById(R.id.about);
        Drawable aboutDrawable = ContextCompat.getDrawable(NavigationActivity.this,R.drawable.nav_about);
        aboutDrawable.setBounds(0,0,100,100);
        about.setCompoundDrawables(aboutDrawable,null, null, null);

        logout = (Button) findViewById(R.id.logout);
        Drawable logoutDrawable = ContextCompat.getDrawable(NavigationActivity.this,R.drawable.nav_exit);
        logoutDrawable.setBounds(0,0,80,80);
        logout.setCompoundDrawables(logoutDrawable,null, null, null);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NavigationActivity.this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override public void onClick(DialogInterface dialog, int which) {
                                Storage.remove(NavigationActivity.this,new User());
                                startActivity(new Intent(NavigationActivity.this,MainActivity.class));
                                finish();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }
}
