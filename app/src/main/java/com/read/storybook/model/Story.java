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
	
	
	private String title;
	private String body;
	private List<Image> images;
	//-- Transient
	private List<Question> questions;

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
	public Story(){
		questions = new ArrayList<Question>();
		images = new ArrayList<Image>();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		//XXX: PUT IT BACK
		return "";
	}

	public void setBody(String body) {
		this.body = body;
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

	
}
