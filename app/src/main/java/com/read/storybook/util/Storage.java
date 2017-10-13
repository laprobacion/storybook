package com.read.storybook.util;


import android.content.Context;

import com.read.storybook.model.User;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class Storage {
    private static String FILENAME = "storage";
    public static void save(Context context, User user) {

        try {
            FileOutputStream fos = context.getApplicationContext().openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(user.getUsername().getBytes());
            fos.close();
            AppCache.getInstance().setUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String load(Context context) {
        StringBuilder sb = new StringBuilder();
        try {
            FileInputStream fis = context.getApplicationContext().openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
