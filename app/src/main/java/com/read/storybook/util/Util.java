package com.read.storybook.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

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
}
