package com.read.storybook;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.read.storybook.model.Image;
import com.read.storybook.model.Story;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.StoryService;
import com.read.storybook.util.AppConstants;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AddStoryActivity extends AppCompatActivity {
    Button uploadBtn;
    EditText arrImageView [];
    EditText arrEditTxt [];
    EditText addStoryName ;
    Image[] image;
    TextView addStoryErrMsg;
    String levelId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);
        levelId = getIntent().getStringExtra(AppConstants.LEVEL_ID);
        addStoryName = (EditText)findViewById(R.id.addStoryName);
        addStoryErrMsg = (TextView)findViewById(R.id.addStoryErrMsg);

        uploadBtn = (Button) findViewById(R.id.uploadImageBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);
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
                    story.setTitle(addStoryName.getText().toString().trim());
                    for(int i=0; i< arrImageView.length; i++){
                        image[i].setPriority(arrEditTxt[i].getText().toString().trim());
                        story.addImage(image[i]);
                    }
                    create(story);
                    saveStoryAddBtn.setEnabled(true);
                }
            }

        });

    }

    private void create(final Story story){
        Service service = new Service("Creating Story...", AddStoryActivity.this, new ServiceResponse() {
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
        StoryService.create(levelId, story, service);
    }

    private String validate(){
        if(addStoryName.getText().toString().trim().equals("")){
            return "Story Name cannot be empty.";
        }
        if(arrImageView == null){
            return "No image selected.";
        }
        Set<String> prs = new HashSet<String>();
        for(EditText et: arrEditTxt){
            if(et.getText().toString().trim().equals("")){
               return "Priority cannot be empty";
            }
            try{
                Integer.parseInt(et.getText().toString().trim());
            }catch (NumberFormatException e){
                return "Priority must be a number";
            }
            if (!prs.add(et.getText().toString().trim())) {
                return "Duplicate Priority";
            }
        }
        return null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            if(arrImageView != null && arrImageView.length > 0){
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activityStoryLayout);
                for(int i=0; i< arrImageView.length; i++){
                    relativeLayout.removeView(arrImageView[i]);
                    relativeLayout.removeView(arrEditTxt[i]);
                }
            }
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.activityStoryLayout);
            int count = 1;
            if(data.getClipData() != null){
                count = data.getClipData().getItemCount();
            }
            arrImageView = new EditText[count];
            arrEditTxt = new EditText[count];
            image = new Image[count];
            int [] arrIds = new int [count];
            int top = 300;
            for(int i=0; i < count; i++) {
                Uri selectedImage = null;
                int parentId = uploadBtn.getId();
                if (count == 1) {
                    selectedImage = data.getData();
                } else {
                    selectedImage = data.getClipData().getItemAt(i).getUri();
                }
                String sizeValidate = validateSize(selectedImage);
                if(sizeValidate!=null){
                    addStoryErrMsg.setTextColor(Color.RED);
                    addStoryErrMsg.setVisibility(View.VISIBLE);
                    addStoryErrMsg.setText(sizeValidate);
                    throw new RuntimeException("sizeValidate");
                }
                if(i != 0){
                    parentId = arrIds[i-1];
                }

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                RelativeLayout.LayoutParams lpTxt = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                lpTxt.width = 100;
                lpTxt.height = 200;
                EditText txtPriority = new EditText(AddStoryActivity.this);
                txtPriority.setHint("1");
                lpTxt.setMargins(100,top + 65,0,0);
                txtPriority.setLayoutParams(lpTxt);
                EditText imageView = new EditText(AddStoryActivity.this);
                imageView.setEnabled(false);
                imageView.setId(i);
                imageView.setText(getFileName(selectedImage));
                //lp.addRule(RelativeLayout.BELOW, parentId);
                lp.setMargins(300,top+120,0,0);
                top+=200;
                imageView.setLayoutParams(lp);
                layout.addView(txtPriority);
                layout.addView(imageView);
                arrIds[i] = imageView.getId();
                arrImageView [i] =  imageView;
                arrEditTxt [i] = txtPriority;
                image[i] = new Image(getContentResolver().getType(selectedImage),bitmap,txtPriority.getText().toString());
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
}
