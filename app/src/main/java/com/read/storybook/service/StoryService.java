package com.read.storybook.service;

import android.app.Activity;
import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.read.storybook.dao.StoryDao;
import com.read.storybook.dao.UserStoryDao;
import com.read.storybook.model.Image;
import com.read.storybook.model.Sound;
import com.read.storybook.model.Story;
import com.read.storybook.model.User;
import com.read.storybook.util.Encryptor;
import com.read.storybook.util.Util;

import java.util.HashMap;
import java.util.Map;

public class StoryService {
	public static void setStoryIcon(Story story, Service service){
		if(!story.getCover().equals("null")) {
			service.postStory(story);
			service.execute();
		}
	}
	public static void populateImages(Story story, Service service){
		service.getImages(story);
		service.execute();
	}

	public static void create(String levelId, final Story story, Service service){
		//service.post("http://jabahan.com/storybook/story/create.php", params);
		HashMap<String,String> data = new HashMap<String,String> ();
		data.put("id", Util.generateId());
		data.put("title", story.getTitle());
		data.put("isActive", "true");
		data.put("imageCount", String.valueOf(story.getImages().size()));
		data.put("levelId", levelId);
		int i=0;
		for (Image image : story.getImages()){
			int id = i+1;
			data.put("priority"+id, image.getPriority());
			data.put("image"+id, image.getEncodedBitmap());
			data.put("ext"+id, image.getExtension());
			i++;
		}
		service.post("http://jabahan.com/storybook/story/create.php", data);
		service.execute();
	}
	public static void addLesson(final Story story, Service service, String php){
		HashMap<String,String> data = new HashMap<String,String> ();
		data.put("id", story.getId());
		data.put("imageCount", String.valueOf(story.getImages().size()));
		int i=0;
		for (Image image : story.getImages()){
			int id = i+1;
			data.put("imageid"+id, Util.generateId());
			data.put("priority"+id, image.getPriority());
			data.put("image"+id, image.getEncodedBitmap());
			data.put("ans"+id, image.getAns());
			data.put("ext"+id, image.getExtension());
			i++;
		}
		service.post("http://jabahan.com/storybook/story/"+php+".php", data);
		service.execute();
	}

	public static void addNarrative(final Story story, Service service, boolean isStory){
		HashMap<String,String> data = new HashMap<String,String> ();
		data.put("id", story.getId());
		data.put("imageCount", String.valueOf(story.getSoundList().size()));
		String table = isStory ? "STORYNARRATIVE" : "LESSONNARRATIVE";
		data.put("table", table);

		int i=0;
		for (Sound sound : story.getSoundList()){
			int id = i+1;
			data.put("imageid"+id, Util.generateId());
			data.put("priority"+id, sound.getPriority());
			data.put("image"+id, sound.getEncoded());
			data.put("ext"+id, sound.getExt());
			i++;
		}
		service.post("http://jabahan.com/storybook/story/addNarrative.php", data);
		service.execute();
	}
    public static void search(String levelId, Service service, boolean isStory){
        RequestParams params = new RequestParams();
		String table = isStory ? "STORYNARRATIVE" : "LESSONNARRATIVE";
        service.post("http://jabahan.com/storybook/story/search.php?levelId="+levelId+"&table="+table, params);
        service.execute();
    }
	public static void searchImages(String storyId, Service service){
		RequestParams params = new RequestParams();
		service.get("http://jabahan.com/storybook/story/searchimages.php?id="+storyId, params);
		service.execute();
	}
	public static void searchLessons(String storyId, Service service){
		RequestParams params = new RequestParams();
		service.get("http://jabahan.com/storybook/story/searchLessons.php?id="+storyId, params);
		service.execute();
	}
	public static void searchNarratives(String storyId, Service service){
		RequestParams params = new RequestParams();
		service.get("http://jabahan.com/storybook/story/searchNarratives.php?id="+storyId, params);
		service.execute();
	}
	public static void delete(Context activity, String storyId, Service service){
		RequestParams params = new RequestParams();
		params.put("storyId", storyId);
		service.post("http://jabahan.com/storybook/story/delete.php", params);
		Util.delete(activity,service);
	}
	public static void deleteImage(Context activity, String imageId,String page,boolean isLesson, Service service){
		RequestParams params = new RequestParams();
		params.put("imageId", imageId);
		params.put("page", page);
		String table = "";
		if(isLesson){
			table = "STORYLESSON";
		}else{
			table = "STORYIMAGE";
		}
		params.put("table", table);
		service.post("http://jabahan.com/storybook/story/deleteImage.php", params);
		Util.delete(activity,service);
	}
	public static int getScore(User user, Story story){
		return new UserStoryDao().getStoriesAndScores(user).get(story.getId());
	}
	
	public static void save(Story story){
		new StoryDao().save(story);
	}
	
	public static void saveChild(Story story){
		new StoryDao().saveChild(story);
	}
}
