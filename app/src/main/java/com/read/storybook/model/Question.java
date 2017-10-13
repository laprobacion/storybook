package com.read.storybook.model;

import java.util.List;
import java.util.ArrayList;

public class Question extends BaseModel{
	
	private String description;
	
	private List<Choice> choices;
	public Question(){
		choices = new ArrayList<Choice>();
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Choice> getChoices() {
		return choices;
	}
	public void addChoice(Choice choice) {
		this.getChoices().add(choice);
	}

	
}
