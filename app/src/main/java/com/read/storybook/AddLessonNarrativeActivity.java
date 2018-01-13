package com.read.storybook;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.read.storybook.model.Image;
import com.read.storybook.model.Sound;
import com.read.storybook.model.Story;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.StoryService;
import com.read.storybook.util.AppConstants;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class AddLessonNarrativeActivity extends AppCompatActivity {

    Button btnAction;
    EditText arrImageView [];
    EditText arrEditTxt [];
    Image[] image;
    Sound[] sound;
    TextView addStoryErrMsg,filename;
    String storyId;
    static final String IMAGE_STR = "Upload Image";
    static final String SOUND_STR = "Upload Sound";
    MediaPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lesson_narrative);
        storyId = getIntent().getStringExtra(AppConstants.STORY_ID);
        addStoryErrMsg = (TextView)findViewById(R.id.addStoryErrMsg);
        filename = (TextView)findViewById(R.id.filename);
        btnAction = (Button) findViewById(R.id.btnAction);

        if(getIntent().getStringExtra(AppConstants.STORY_LESSON) != null){
            btnAction.setText(IMAGE_STR);
            filename.setVisibility(View.INVISIBLE);
        }else{
            btnAction.setText(SOUND_STR);
            filename.setVisibility(View.VISIBLE);
        }
        player = new MediaPlayer();
        btnAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if(btnAction.getText().toString().equals(IMAGE_STR)){
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
                }else{
                    intent.setType("audio/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(Intent.createChooser(intent,"Select Audio"), 2);
                }

            }

        });

        final Button saveStoryAddBtn = (Button) findViewById(R.id.saveStoryAddBtn);
        saveStoryAddBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                saveStoryAddBtn.setEnabled(false);
                String err = validate();
                if(err != null){
                    addStoryErrMsg.setTextColor(Color.RED);
                    addStoryErrMsg.setVisibility(View.VISIBLE);
                    addStoryErrMsg.setText(err);
                    saveStoryAddBtn.setEnabled(true);
                }else{
                    Story story = new Story();
                    story.setId(getIntent().getStringExtra(AppConstants.STORY_ID));
                    if(getIntent().getStringExtra(AppConstants.STORY_LESSON) != null) {
                        for (int i = 0; i < arrImageView.length; i++) {
                            image[i].setPriority(arrEditTxt[i].getText().toString().trim());
                            story.addImage(image[i]);
                        }
                        addLesson(story);
                        saveStoryAddBtn.setEnabled(true);
                    }else{
                        for (int i = 0; i < arrImageView.length; i++) {
                            sound[i].setPriority(arrEditTxt[i].getText().toString().trim());
                            story.addSound(sound[i]);
                        }
                        addNarrative(story);
                        saveStoryAddBtn.setEnabled(true);
                    }
                }
            }

        });

    }

    private void addLesson(final Story story){
        Service service = new Service("Updating Story...", AddLessonNarrativeActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    if (resp.getString("message").equals("Success")) {
                        finish();
                    }else{
                        addStoryErrMsg.setTextColor(Color.RED);
                        addStoryErrMsg.setVisibility(View.VISIBLE);
                        addStoryErrMsg.setText(resp.getString("message"));
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
        StoryService.addLesson(story, service);
    }

    private void addNarrative(final Story story){
        Service service = new Service("Updating Story...", AddLessonNarrativeActivity.this, new ServiceResponse() {
            @Override
            public void postExecute(JSONObject resp) {
                try {
                    if (resp.getString("message").equals("Success")) {
                        finish();
                    }else{
                        addStoryErrMsg.setTextColor(Color.RED);
                        addStoryErrMsg.setVisibility(View.VISIBLE);
                        addStoryErrMsg.setText(resp.getString("message"));
                    }
                }catch (Exception e){e.printStackTrace();}
            }
        });
        boolean isStory = Boolean.valueOf(getIntent().getStringExtra(AppConstants.STORY_IS_STORY));
        StoryService.addNarrative(story, service, isStory);
    }
    private String validate(){
        String msg = "";
        if(btnAction.getText().toString().equals(IMAGE_STR)) {
            msg = "No image selected.";
        }else{
            msg = "No sound selected.";
        }
            if (arrImageView == null) {
                return msg;
            }
            Set<String> prs = new HashSet<String>();
            for (EditText et : arrEditTxt) {
                if (et.getText().toString().trim().equals("")) {
                    return "Page cannot be empty";
                }
                try {
                    Integer.parseInt(et.getText().toString().trim());
                } catch (NumberFormatException e) {
                    return "Page must be a number";
                }
                if (!prs.add(et.getText().toString().trim())) {
                    return "Duplicate Page";
                }
            }

        return null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && resultCode == Activity.RESULT_OK) {
            if (arrImageView != null && arrImageView.length > 0) {
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.addLessonNarrativeParent);
                for (int i = 0; i < arrImageView.length; i++) {
                    relativeLayout.removeView(arrImageView[i]);
                    relativeLayout.removeView(arrEditTxt[i]);
                }
            }

            RelativeLayout layout = (RelativeLayout) findViewById(R.id.addLessonNarrativeParent);
            int count = 1;
            if (data.getClipData() != null) {
                count = data.getClipData().getItemCount();
            }
            arrImageView = new EditText[count];
            arrEditTxt = new EditText[count];
            image = new Image[count];
            sound = new Sound[count];
            int[] arrIds = new int[count];
            int top = 300;
            for (int i = 0; i < count; i++) {
                Uri selectedImage = null;
                int parentId = btnAction.getId();
                if (count == 1) {
                    selectedImage = data.getData();
                } else {
                    selectedImage = data.getClipData().getItemAt(i).getUri();
                }
                String sizeValidate = null;
                if(getIntent().getStringExtra(AppConstants.STORY_LESSON) != null) {
                    sizeValidate = validateSize(selectedImage);
                }else{
                    sizeValidate = validateSoundSize(selectedImage);
                }
                if (sizeValidate != null) {
                    addStoryErrMsg.setTextColor(Color.RED);
                    addStoryErrMsg.setVisibility(View.VISIBLE);
                    addStoryErrMsg.setText(sizeValidate);
                    throw new RuntimeException("sizeValidate");
                }
                if (i != 0) {
                    parentId = arrIds[i - 1];
                }
                Bitmap bitmap = null;
                if(getIntent().getStringExtra(AppConstants.STORY_LESSON) != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                RelativeLayout.LayoutParams lpTxt = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lpTxt.width = 100;
                lpTxt.height = 200;
                EditText txtPriority = new EditText(AddLessonNarrativeActivity.this);
                txtPriority.setInputType(InputType.TYPE_CLASS_NUMBER);
                txtPriority.setHint("1");
                lpTxt.setMargins(100, top + 65, 0, 0);
                txtPriority.setLayoutParams(lpTxt);
                EditText imageView = new EditText(AddLessonNarrativeActivity.this);
                imageView.setEnabled(false);
                imageView.setId(i);
                imageView.setText(getFileName(selectedImage));
                //lp.addRule(RelativeLayout.BELOW, parentId);
                lp.setMargins(300, top + 120, 0, 0);
                top += 200;
                imageView.setLayoutParams(lp);
                layout.addView(txtPriority);
                layout.addView(imageView);
                arrIds[i] = imageView.getId();
                arrImageView[i] = imageView;
                arrEditTxt[i] = txtPriority;
                if(getIntent().getStringExtra(AppConstants.STORY_LESSON) != null) {
                    image[i] = new Image(getContentResolver().getType(selectedImage), bitmap, txtPriority.getText().toString());
                }else{
                    Sound s = new Sound(selectedImage);
                    s.setByteArrayOutputStream(audioStream(selectedImage));
                    s.setExt(getContentResolver().getType(selectedImage));
                    sound[i] = s;
                }
            }
            layout.getLayoutParams().height = layout.getHeight() + top + 500;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String validateSize(Uri uri){
        File file =new File(uri.getPath());
        double bytes = file.length();
        double kilobytes = (bytes / 1024);
        if(kilobytes > 401){
            return "Image must be less than 400kb";
        }
        return null;
    }
    public String validateSoundSize(Uri uri){
        File file =new File(uri.getPath());
        double bytes = file.length();
        double kilobytes = (bytes / 1024);
        double megabytes = (kilobytes / 1024);
        if(megabytes > 6){
            return "Image must be less than 6mb";
        }
        return null;
    }
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private ByteArrayOutputStream audioStream(Uri uri){
        String name = getFileName(uri);
        File file = new File(getCacheDir(),name);
        int maxBufferSize = 1 * 1024 * 1024;
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            int  bytesAvailable = inputStream.available();
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            final byte[] buffers = new byte[bufferSize];
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int read = 0;
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            return outputStream;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
