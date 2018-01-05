package com.read.storybook.util;


import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class Player extends AsyncTask<String, Void, Boolean> {
    private Button btn;
    private boolean isPlaying;
    private MediaPlayer mediaPlayer;
    private boolean initialStage = true;
    private TextView status;
    public Player(TextView status, Button btn){
        this.status = status;
        this.btn = btn;
    }
    @Override
    protected Boolean doInBackground(String... strings) {
        Boolean prepared = false;

        try {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(strings[0]);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    initialStage = true;
                    isPlaying = false;
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            });

            mediaPlayer.prepare();
            prepared = true;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MyAudioStreamingApp", e.getMessage());
            prepared = false;
        }

        return prepared;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(!aBoolean){
            status.setText("Error loading sound...");
            return;
        }
        status.setText("Narrative Playing...");
        btn.setEnabled(true);
        mediaPlayer.start();
        initialStage = false;
        isPlaying = true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        status.setText("Narrative buffering...");
        btn.setEnabled(false);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
    public void play(){
        isPlaying = true;
        status.setText("Narrative Playing...");
        mediaPlayer.start();
    }
    public void stop(){
        isPlaying = false;
        status.setText("Narrative Paused.");
        mediaPlayer.pause();
    }
}
