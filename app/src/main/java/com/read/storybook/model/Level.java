package com.read.storybook.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Level extends BaseModel{
	private String name;
	private Date created;
	//--Transient
	private List<Story> stories;
	
	public Level(){
		stories = new ArrayList<Story>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Story> getStories() {
		return stories;
	}
	public void addStory(Story story) {
		this.stories.add(story);
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setStories(List<Story> stories) {
		this.stories = stories;
	}
}
