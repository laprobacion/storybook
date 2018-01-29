package com.read.storybook.service;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.loopj.android.http.RequestParams;
import com.read.storybook.dao.QuestionDao;
import com.read.storybook.model.Choice;
import com.read.storybook.model.Image;
import com.read.storybook.model.Question;
import com.read.storybook.model.Story;
import com.read.storybook.util.Util;

public class QuestionService {

	public static void getQuestions(String storyId, Service service){

		RequestParams params = new RequestParams();
		params.put("storyId", storyId);
		service.post("http://jabahan.com/storybook/question/search.php", params);
		service.execute();
	}
	public static void deleteQuestion(Context act, String questionId, Service service){
		RequestParams params = new RequestParams();
		params.put("questionId", questionId);
		service.post("http://jabahan.com/storybook/question/delete.php", params);
		Util.delete(act,service);
	}
	public static void create(String storyId, Question q, Service service){
		HashMap<String,String> data = new HashMap<String,String> ();
		data.put("id", q.getId());
		data.put("title", q.getDescription());
		data.put("isActive", "true");
		data.put("storyId", storyId);
		data.put("choiceCount", String.valueOf(q.getChoices().size()));

		int i=0;
		for (Choice c : q.getChoices()){
			int id = i+1;
			data.put("choiceId"+id, c.getId());
			data.put("choiceDesc"+id, c.getDescription());
			data.put("choiceIsActive"+id, String.valueOf(c.isActive()));
			data.put("choiceIsAnswer"+id, String.valueOf(c.isAnswer()));
			i++;
		}
		service.post("http://jabahan.com/storybook/question/create.php", data);
		service.execute();
	}
	public static void saveChild(Question question){
		new QuestionDao().saveChild(question);
	}
	public static List<Question> randomizeQuestions(Story story){
		List<Integer> items = new ArrayList<Integer>();
		populateItems(items,story.getQuestions().size(),story.getQuestions().size());
		List<Question> questions = new ArrayList<Question>();
		Collections.shuffle(items);
		for(int i : items){
			questions.add(story.getQuestions().get(i));
		}
		return questions;
	}
	
	private static int generateRandomNumber(int max){
		return new Random().nextInt(max);
	}
	
	private static void populateItems(List<Integer> items , int maxItems, int maxRange){
		for(int i=0; i<maxItems; i++){
			items.add(i);
		}
	}
	
}
