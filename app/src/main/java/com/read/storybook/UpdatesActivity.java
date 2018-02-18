package com.read.storybook;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class UpdatesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updates);
        TextView btnUpdateCheck = (TextView) findViewById(R.id.btnUpdateCheck);
        btnUpdateCheck.setTextColor(Color.argb(255,255,255,255));
        btnUpdateCheck.setCompoundDrawablesWithIntrinsicBounds(R.drawable.download, 0, 0, 0);
        btnUpdateCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appId = "";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+appId+"&hl=en")));
            }
        });
    }
}
