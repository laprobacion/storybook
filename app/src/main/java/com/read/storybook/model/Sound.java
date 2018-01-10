package com.read.storybook.model;

import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Sound extends BaseModel{
    private String priority;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    private String ext;
    private Uri uri;
    private String url;
    private ByteArrayOutputStream byteArrayOutputStream;

    public String getUrl() {        return url;    }

    public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    public Sound(String url){
        this.url = url;
    }

    public String getExt() {        return ext;    }

    public void setExt(String ext) {
        this.ext = ext.split("/")[1];
    }

    public Sound(Uri uri){
        this.uri = uri;
    }

    public String getEncoded(){
        String converted = null;
        try {
            converted = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }
        return converted;
    }

}
