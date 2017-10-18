package com.read.storybook.service;

import com.read.storybook.dao.ChoiceDao;
import com.read.storybook.model.Choice;
import com.read.storybook.model.Question;
import com.read.storybook.model.Story;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChoiceService {

	public static void save(Choice choice){
		new ChoiceDao().save(choice);
	}

	public static List<Choice> randomizeChoice(Question question){
		List<Integer> items = new ArrayList<Integer>();
		populateItems(items,question.getChoices().size(),question.getChoices().size());
		List<Choice> choices = new ArrayList<Choice>();
		Collections.shuffle(items);
		for(int i : items){
			choices.add(question.getChoices().get(i));
		}
		return choices;
	}

	private static void populateItems(List<Integer> items , int maxItems, int maxRange){
		for(int i=0; i<maxItems; i++){
			items.add(i);
		}
	}
	
}
