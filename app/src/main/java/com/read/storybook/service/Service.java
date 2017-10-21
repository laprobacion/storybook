package com.read.storybook.service;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.SyncHttpClient;
import com.read.storybook.RegisterActivity;
import com.read.storybook.model.Image;
import com.read.storybook.model.Story;
import com.read.storybook.util.AppConstants;
import com.read.storybook.util.RequestHandler;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpResponseException;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;


public class Service extends AsyncTask<Void, Void, Void> {
    ProgressDialog progressBar;
    private String url;
    private HashMap<String,String> data;
    private RequestParams rp;
    private Context context;
    private ServiceResponse sr;
    private JSONObject response;
    private byte[] blobResp;
    private Story story;
    private String loadingTitle;
    private String method;
    public Service(String loadingTitle, Context context, ServiceResponse serviceResponse){
        this.context = context;
        this.sr = serviceResponse;
        this.loadingTitle = loadingTitle;
    }
    public void post(String url, HashMap<String,String> data){
        this.url = url;
        this.data = data;
        this.method = AppConstants.METHOD_IMG_UPLOAD;
    }
    public void post(String url, RequestParams params){
        this.url = url;
        this.rp = params;
        this.method = AppConstants.METHOD_POST;
    }
    public void get(String url, RequestParams params){
        this.url = url;
        this.rp = params;
        this.method = AppConstants.METHOD_GET;
    }
    public void getImages(Story story) {
        this.story = story;
        this.method = AppConstants.RETRIEVE_IMG;
    }
    @Override
    protected void onPreExecute() {
        progressBar = new ProgressDialog(this.context);
        progressBar.setCancelable(true);
        progressBar.setMessage(this.loadingTitle);
        progressBar.show();

    }

    @Override
    protected void onPostExecute(Void result) {
        if(response != null){
            sr.postExecute(response);
        }
        try {
            progressBar.dismiss();
        }catch (Exception e){
            //exit silently
        }
    }
    @Override
    protected Void doInBackground(Void... params) {
        //remove mock
        //if(true){
            //response = new ServiceMock(url).getResp();
            //return null;
       // }

        SyncHttpClient client = new SyncHttpClient();

        if(this.method.equals(AppConstants.METHOD_POST)) {
            client.post(url, rp, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject resp) {
                    super.onSuccess(statusCode, headers, resp);
                    response = resp;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    response = errorResponse;
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, String st, Throwable th){
                    String s = "";
                }
            });
        }else if(this.method.equals(AppConstants.METHOD_GET)){
            client.get(url, rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject resp) {
                    super.onSuccess(statusCode, headers, resp);
                    response = resp;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                    response = errorResponse;
                }
            });
        }else if (this.method.equals(AppConstants.METHOD_IMG_UPLOAD)){
            RequestHandler rh = new RequestHandler();
            String result = rh.sendPostRequest(url,data);
            try {
                response = new JSONObject(result);
            }catch (Exception e) { e.printStackTrace();}

        }else if (this.method.equals(AppConstants.RETRIEVE_IMG)){
            try {
                for(Image image : story.getImages()){
                    URL url = new URL(image.getUrl());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    image.setBitmap(BitmapFactory.decodeStream(input));
                }
                response = new JSONObject(" {\"dummy\":\"dummy\"} ");
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return null;
    }
}
