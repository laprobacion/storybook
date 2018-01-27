package com.read.storybook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.read.storybook.model.Image;
import com.read.storybook.model.Sound;
import com.read.storybook.model.Story;
import com.read.storybook.service.Service;
import com.read.storybook.service.ServiceResponse;
import com.read.storybook.service.StoryService;
import com.read.storybook.util.AppCache;
import com.read.storybook.util.AppConstants;
import com.read.storybook.util.Player;
import com.read.storybook.util.Util;

import org.json.JSONObject;

import java.util.List;

import static com.read.storybook.util.AppConstants.LESSON_FOR;


public class PageStoryFragment extends Fragment {

    public static final String PAGE_TITLE = "PAGE_TITLE";
    public static final String IMAGE = "IMAGE";
    public static final String IS_LAST = "IS_LAST";
    public static final String STORY = "STORY";
    public static final String PAGING = "PAGING";
    public static final String PAGE_TO_SHOW = "PAGE_TO_SHOW";
    public static final String PAGE_LEFT = "PAGE_LEFT";
    public static final String IS_LESSON = "IS_LESSON";
    public static final String IMAGE_ID = "IMAGE_ID";
    public static final String PRIORITY = "PRIORITY";
    public static final String LESSONNARRATIVE_STORY = "LESSONNARRATIVE_STORY";
    Player audio;
    Button audioButton;
    TextView status;
    Sound soundToLoad = null;
    String currentPage;
    Story story = null;
    Story lessonNarrativeStory = null;
    boolean fragmentResume,fragmentVisible,fragmentOnCreated,isSoundExecuted;
    public PageStoryFragment() {


    }

    public static final PageStoryFragment newInstance(String message, Image i, boolean isLast, Story story, String paging, boolean hasLesson, boolean isLesson, String pageLeft, List<Sound> soundList)
    {
        PageStoryFragment f = new PageStoryFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(PAGE_TITLE, message);
        bdl.putParcelable(IMAGE, i.getBitmap());
        bdl.putString(IMAGE_ID, i.getId());
        bdl.putString(PRIORITY, i.getPriority());
        bdl.putBoolean(IS_LAST,isLast);
        bdl.putString(PAGING, paging);
        bdl.putString(PAGE_LEFT, pageLeft);
        bdl.putBoolean(PAGE_TO_SHOW,hasLesson);
        bdl.putBoolean(IS_LESSON,isLesson);
        bdl.putSerializable(STORY, story);
        if(soundList != null){
            Story lessonNarrativeStory = new Story();
            lessonNarrativeStory.setSoundList(soundList);
            bdl.putSerializable(LESSONNARRATIVE_STORY,lessonNarrativeStory);
        }
        f.setArguments(bdl);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String message = getArguments().getString(PAGE_TITLE);
        Bitmap bitmap = getArguments().getParcelable(IMAGE);
        final String id = getArguments().getString(IMAGE_ID);
        boolean isLast = getArguments().getBoolean(IS_LAST);
        String pageLeft = getArguments().getString(PAGE_LEFT);
        View v = inflater.inflate(R.layout.fragment_page_story, container, false);
        TextView messageTextView = (TextView)v.findViewById(R.id.storyTitle);
        messageTextView.setText("");
        ImageView iv = (ImageView) v.findViewById(R.id.imageStory);
        story = (Story)getArguments().getSerializable(STORY);
        lessonNarrativeStory = (Story)getArguments().getSerializable(LESSONNARRATIVE_STORY);
        currentPage = getArguments().getString(PAGING).split(" of ")[0];
        status = (TextView) v.findViewById(R.id.status);
        audioButton = (Button) v.findViewById(R.id.playAudio);

        final RelativeLayout parent = (RelativeLayout)v.findViewById(R.id.myfragment_layout);
        iv.setImageBitmap(bitmap);
        iv.setMinimumWidth(3000);
        iv.setMinimumHeight(1000);
        Button takeExam =  (Button)v.findViewById(R.id.takeExam);
        if(isLast && story !=null && story.getQuestions() != null && story.getQuestions().size() > 0){
            takeExam.setVisibility(View.VISIBLE);
        }
        takeExam.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(getActivity(), ExamActivity.class);
                myIntent.putExtra(AppConstants.QUESTIONS,story);
                startActivity(myIntent);
               getActivity().finish();
            }
        });
        final TextView pageNo = v.findViewById(R.id.paging);

        pageNo.setText(getArguments().getString(PAGING));
        initAudio();
        if(currentPage.equals("1")){
            if(!AppCache.getInstance().isPageOneDestroyed()){
                Util.createFadingWindow(parent,"Swipe left or right to turn page.",120,1400,200,350);
            }
        }
        if(getArguments().getBoolean(PAGE_TO_SHOW)){
            createWindow(40,parent, "Lesson Time!",story,pageNo.getText().toString(), false,null);
            parent.invalidate();
        }

        if(getArguments().getBoolean(IS_LESSON) && isLast){
            createWindow(30,parent, "You have finished the ",story,pageNo.getText().toString(), true,pageLeft);
            parent.invalidate();
        }
        if(AppCache.getInstance().getUser().isAdmin() && id != null && id.length() > 0){
            iv.setLongClickable(true);
            iv.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    Service service = new Service("Deleting Image...", parent.getContext(), new ServiceResponse() {
                        @Override
                        public void postExecute(JSONObject resp) {
                        }
                    });
                    boolean isPageLesson = getArguments().getString(PAGE_TITLE).indexOf(LESSON_FOR) > -1;
                    StoryService.deleteImage(parent.getContext(), id,getArguments().getString(PRIORITY),isPageLesson, service);
                    return true;
                }
            });
        }
        return v;
    }
    @Override
    public void setUserVisibleHint(boolean visible){
        super.setUserVisibleHint(visible);
        if (visible && isResumed()){   // only at fragment screen is resumed
            fragmentResume=true;
            fragmentVisible=false;
            fragmentOnCreated=true;
            playNarrative();
        }else  if (visible){        // only at fragment onCreated
            fragmentResume=false;
            fragmentVisible=true;
            fragmentOnCreated=true;
        }
        else if(!visible && fragmentOnCreated){// only when you go out of fragment screen
            fragmentVisible=false;
            fragmentResume=false;
            stopAudio();
        }else{
            stopAudio();
        }
    }
    private void initAudio(){
        if(getArguments().getBoolean(IS_LESSON)){
            if(lessonNarrativeStory != null){
                for(Sound sound : lessonNarrativeStory.getSoundList()){
                    if(sound.getPriority().equals(currentPage)){
                        soundToLoad = sound;
                        audio = new Player(status,audioButton,true);
                        addAudioButtonListener();
                        break;
                    }
                }
            }
        }else{
            if(story.getSoundList() != null){
                for(Sound sound : story.getSoundList()){
                    if(sound.getPriority().equals(currentPage)){
                        soundToLoad = sound;
                        audio = new Player(status,audioButton,true);
                        addAudioButtonListener();
                        break;
                    }
                }
            }
        }
        if(soundToLoad != null){
            audioButton.setVisibility(View.VISIBLE);
            status.setVisibility(View.VISIBLE);
            if (!fragmentResume && fragmentVisible){   //only when first time fragment is created
                playNarrative();
            }

        }else{
            audioButton.setVisibility(View.INVISIBLE);
            status.setVisibility(View.INVISIBLE);
        }
    }
    private void addAudioButtonListener(){
        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(audio.isPlaying()){
                    audioButton.setBackground(getActivity().getDrawable(R.drawable.mic_unmute));
                    audio.stop();
                }else{
                    audioButton.setBackground(getActivity().getDrawable(R.drawable.mic_mute));
                    audio.play();
                }
                audioButton.invalidate();
            }
        });
    }
    private void createWindow(int textSize, final RelativeLayout parent, String str, final Story s, final String page, final boolean isLesson, final String pageLeft){
        final RelativeLayout window = Util.createWindowRelativeLayout(parent,isLesson);
        TextView message = Util.createTextView(str,textSize,20,0,window,RelativeLayout.LayoutParams.MATCH_PARENT);
        message.setTextColor(Color.argb(200, 255, 255, 255));
        message.setAllCaps(false);
        String txtButton = "OK";
        TextView button = null;
        if(isLesson){
            TextView subMessage = Util.createTextView("lesson!",textSize,100,0,window,RelativeLayout.LayoutParams.MATCH_PARENT);
            subMessage.setTextColor(Color.argb(200, 255, 255, 255));
            subMessage.setAllCaps(false);
            txtButton = " Return to story ";
            button = Util.createTextView(txtButton,20,215,220,window,RelativeLayout.LayoutParams.WRAP_CONTENT);
        }else{
            button = Util.createTextView(txtButton,30,170,320,window,RelativeLayout.LayoutParams.WRAP_CONTENT);
            button.getLayoutParams().width = 150;
        }
        button.setTextColor(Color.argb(180, 255, 255, 255));
        button.setBackgroundColor(Color.argb(200,108,186,249));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = null;
                s.setLessons(null);
                if(!isLesson){
                    myIntent = new Intent(parent.getContext(), LessonActivity.class);
                    myIntent.putExtra(LessonActivity.CURRENT_PAGE,page.split(" of ")[0]);
                }else{
                    myIntent = new Intent(parent.getContext(), StoryActivity.class);
                    myIntent.putExtra(LevelsActivity.IS_LESSON,"false");
                    myIntent.putExtra(LessonActivity.CURRENT_PAGE,pageLeft);
                }
                myIntent.putExtra(AppConstants.STORY_OBJ,s);
                parent.getContext().startActivity(myIntent);
                getActivity().finish();

            }
        });
    }


    public void playNarrative(){
        if(soundToLoad != null ){
            if(audio.getStatus() == AsyncTask.Status.PENDING ){
                audio.execute(soundToLoad.getUrl());
            }else{
                audio.play();
            }

        }
    }

    public void stopAudio(){
        if(audioButton != null && soundToLoad != null){
            if(audio.isPlaying()){
                audioButton.setBackground(getActivity().getDrawable(R.drawable.mic_unmute));
                audio.stop();
            }
        }
    }

    public void destroyAudio(){
        if(audioButton != null && audio != null){
            audio.cancel(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        destroyAudio();
        stopAudio();
    }


}
