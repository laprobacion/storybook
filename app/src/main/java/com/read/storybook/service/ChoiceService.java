package com.read.storybook.service;

import com.read.storybook.dao.ChoiceDao;
import com.read.storybook.model.Choice;

public class ChoiceService {

	public static void save(Choice choice){
		new ChoiceDao().save(choice);
	}
	
}
