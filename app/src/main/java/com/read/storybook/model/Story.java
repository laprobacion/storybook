package com.read.storybook.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Story extends BaseModel{
	
	private Sound sound;
	private List<Sound> soundList;
	private String title;
	private String cover;
	private List<Lesson> lessons;

	public void setSoundList(List<Sound> soundList) {
		this.soundList = soundList;
	}

	public void setLessons(List<Lesson> lessons) {		this.lessons = lessons;	}

	//-- Transient
	private List<Question> questions;
	private List<Image> images;
	private Bitmap coverBitmap;
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	private int maxQuestion;
	public void addImage(Image image){
		this.images.add(image);
	}
	public List<Image> getImages(){
		return this.images;
	}
	public void addLesson(Lesson lesson){
		this.lessons.add(lesson);
	}
	public List<Lesson> getLessons(){
		return this.lessons;
	}

	public List<Sound> getSoundList() {		return soundList;	}
	public void addSound(Sound sound){
		this.soundList.add(sound);
	}
	public Story(){
		questions = new ArrayList<Question>();
		images = new ArrayList<Image>();
		lessons = new ArrayList<Lesson>();
		soundList = new ArrayList<Sound>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void addQuestion(Question question) {
		this.questions.add(question);
	}

	public int getMaxQuestion() {
		return maxQuestion;
	}

	public void setMaxQuestion(int maxQuestion) {
		this.maxQuestion = maxQuestion;
	}

	public Bitmap getCoverBitmap() {
		return coverBitmap;
	}

	public void setCoverBitmap(Bitmap coverBitmap) {
		this.coverBitmap = coverBitmap;
	}
}
