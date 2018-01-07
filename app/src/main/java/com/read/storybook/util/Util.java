package com.read.storybook.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.read.storybook.LessonActivity;
import com.read.storybook.LevelsActivity;
import com.read.storybook.StoryActivity;
import com.read.storybook.model.Story;
import com.read.storybook.service.Service;

import java.util.UUID;

public class Util {

    public static String generateId(){
        return UUID.randomUUID().toString();
    }

    public static void delete(final Context activity, final Service service){
        new AlertDialog.Builder(activity)
                .setTitle("Delete.")
                .setMessage("Are you sure you want to delete?")
                .setNegativeButton(android.R.string.cancel, null) // dismisses by default
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        service.execute();
                        ((Activity)activity).finish();
                    }
                })
                .create()
                .show();
    }

    public static RelativeLayout createWindowRelativeLayout(RelativeLayout parent, boolean isLesson){
        RelativeLayout rl = new RelativeLayout(parent.getContext());
        rl.setBackgroundColor(Color.argb(130, 0, 0, 0));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        if(isLesson){
            lp.height = 320;
        }else{
            lp.height = 300;
        }
        lp.width = 800;
        lp.setMargins(500,300,0,0);
        rl.setLayoutParams(lp);
        parent.addView(rl);
        return rl;
    }
    public static TextView createTextView(String str, int textSize, int top, int left, RelativeLayout layout, int w){
        TextView tv = new TextView(layout.getContext());
        tv.setTextSize(textSize);
        tv.setText(str);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(w,RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = top;
        lp.leftMargin = left;
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setLayoutParams(lp);
        layout.addView(tv);
        return tv;
    }
    public static RelativeLayout createFadingWindow(RelativeLayout parent, String msg, int height, int width, int left, int top){
        RelativeLayout rl = new RelativeLayout(parent.getContext());
        rl.setBackgroundColor(Color.argb(130, 0, 0, 0));
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        lp.height = height;
        lp.width = width;
        lp.setMargins(left,top,0,0);
        rl.setLayoutParams(lp);
        parent.addView(rl);
        TextView tv = createTextView(msg,30,0,0,rl,RelativeLayout.LayoutParams.MATCH_PARENT);
        tv.setTextColor(Color.argb(200,255,255,255));
        createFadeAnimation(rl);
        rl.setVisibility(View.INVISIBLE);
        return rl;
    }

    public static void createFadeAnimation(View v){
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2000);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
        fadeOut.setStartOffset(4000);
        fadeOut.setDuration(1000);

        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        animation.addAnimation(fadeOut);
        v.setAnimation(animation);
    }

}
