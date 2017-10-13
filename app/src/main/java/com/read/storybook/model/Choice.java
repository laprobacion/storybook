package com.read.storybook.model;

public class Choice extends BaseModel {
	
	private String description;
	private boolean isAnswer;
	
	//Transient
	private boolean isSelected;
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isAnswer() {
		return isAnswer;
	}

	public void setAnswer(boolean answer) {
		isAnswer = answer;
	}
}
