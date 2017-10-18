package com.read.storybook.service;

import com.loopj.android.http.RequestParams;
import com.read.storybook.dao.LevelDao;
import com.read.storybook.model.Level;
import com.read.storybook.model.Story;
import com.read.storybook.model.User;
import com.read.storybook.util.Encryptor;
import com.read.storybook.util.Util;

public class LevelService {

	public static void read(Service service){
		RequestParams params = new RequestParams();
		service.get("http://jabahan.com/storybook/level/read.php", params);
		service.execute();
	}

	public static void create(final Level level, Service service){
		RequestParams params = new RequestParams();
		params.put("id", Util.generateId());
		params.put("name", level.getName());
		params.put("isActive", level.isActive());
		service.post("http://jabahan.com/storybook/level/create.php", params);
		service.execute();
	}

	public static int getScore(User user, Level level){
		int score = 0;
		for(Story story : level.getStories()){
			score += StoryService.getScore(user, story);
		}
		return score;
	}
	
	public static void save(Level level){
		new LevelDao().save(level);
	}
	
	public static void saveChild(Level level){
		new LevelDao().saveChild(level);
	}
}