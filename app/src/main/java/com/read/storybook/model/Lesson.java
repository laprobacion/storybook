package com.read.storybook.model;


import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Lesson extends BaseModel{

    private String ext;
    private Bitmap bitmap;
    private String priority;
    private String url;


    public Lesson(String url){
        this.url = url;
    }

    public Lesson(String mimeType, Bitmap bitmap, String priority){
        this.ext = mimeType;
        this.bitmap = bitmap;
        this.priority = priority;
    }

    public String getUrl() {
        return url;
    }

    public String getExtension(){
        if (ext.toUpperCase().indexOf("JPG") > 0 || ext.toUpperCase().indexOf("JPEG") > 0){
            return "jpg";
        }
        if (ext.toUpperCase().indexOf("PNG") > 0 || ext.toUpperCase().indexOf("PNG") > 0){
            return "png";
        }
        throw new RuntimeException("I dont know the extension");
    }

    public String getEncodedBitmap() {
        Bitmap.CompressFormat format = null;
        if (ext.toUpperCase().indexOf("JPG") > 0 || ext.toUpperCase().indexOf("JPEG") > 0){
            format = Bitmap.CompressFormat.JPEG;
        }
        if (ext.toUpperCase().indexOf("PNG") > 0 || ext.toUpperCase().indexOf("PNG") > 0){
            format = Bitmap.CompressFormat.PNG;
        }
        if(format == null) {
            throw new RuntimeException("I dont know the extension");
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(format, 90, stream);
        String converted = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
        return converted;
    }

    public String getPriority() {
        return priority;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
