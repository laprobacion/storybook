package com.read.storybook.service;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.loopj.android.http.RequestParams;
import com.read.storybook.R;
import com.read.storybook.RegisterActivity;
import com.read.storybook.dao.UserAchievementDao;
import com.read.storybook.dao.UserDao;
import com.read.storybook.dao.UserStoryDao;
import com.read.storybook.model.Achievement;
import com.read.storybook.model.User;
import com.read.storybook.util.Encryptor;
import com.read.storybook.util.Storage;
import com.read.storybook.util.Util;

import org.json.JSONObject;

public class UserService {

	public static void register(final User user, Service service){
		RequestParams params = new RequestParams();
		params.put("id", Util.generateId());
		params.put("username", user.getUsername());
		String pass = Encryptor.encrypt(user.getPassword());
		params.put("password", pass);
		params.put("isAdmin", user.isAdmin());
		params.put("isActive", user.isActive());
		service.post("http://jabahan.com/storybook/user/create.php", params);
		service.execute();
	}

	public static void login(final User user, Service service){
		RequestParams params = new RequestParams();
		params.put("id", Util.generateId());
		params.put("username", user.getUsername());
		String pass = Encryptor.encrypt(user.getPassword());
		params.put("password", pass);
		params.put("isAdmin", user.isAdmin());
		params.put("isActive", user.isActive());
		String url = "http://jabahan.com/storybook/user/login.php?username="+user.getUsername()+"&password="+pass;
		service.get(url, params);
		service.execute();
	}

	public static void search(final User user, Service service){
		RequestParams params = new RequestParams();
		params.put("id", Util.generateId());
		params.put("username", user.getUsername());
		String pass = Encryptor.encrypt(user.getPassword());
		params.put("password", pass);
		params.put("isAdmin", user.isAdmin());
		params.put("isActive", user.isActive());
		String url = "http://jabahan.com/storybook/user/search.php?username="+user.getUsername();
		service.get(url, params);
		service.execute();
	}

	public static User getUserByUserId(String id){
		return new UserDao().getUserById(id);
	}

	
	public static void update(User user){
		if(new UserDao().update(user) == null){
			throw new RuntimeException("User not found!");
		}
	}
	
	public static void updatePassword(User user){
		if(user.getPassword() != null && !user.getPassword().trim().equals("")){
			if(new UserDao().updatePassword(user) == null){
				throw new RuntimeException("User not found!");
			}
		}else{
			throw new RuntimeException("Password is empty!");
		}
	}
	
	public static List<Achievement> getUsersAchievements(User user){
		return new UserAchievementDao().getAchievementsByUserId(user);
	}
	
	public static int getUsersTotalScore(User user){
		int totalScore = 0;
		for(int i : new UserStoryDao().getStoriesAndScores(user).values()){
			totalScore += i;
		}
		return totalScore;
	}
	
	public static List<User> getAllUsers(){
		return new UserDao().getAllUsers();
	}
	
	public static List<User> getTopUsers(int top){
		Map<User, Integer> users = new TreeMap<User, Integer>();
		for(User u : getAllUsers()){
			users.put(u, getUsersTotalScore(u));
		}
		List<User> topUsers = new ArrayList<User>();
		int i =0;
		for(User u : users.keySet()){
			if(i < top){
				topUsers.add(u);
				i++;
			}else{
				break;
			}
		}
		return topUsers;
	}
}
