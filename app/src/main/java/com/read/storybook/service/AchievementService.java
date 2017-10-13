package com.read.storybook.service;

import java.util.List;

import com.read.storybook.dao.UserAchievementDao;
import com.read.storybook.model.Achievement;
import com.read.storybook.model.User;

public class AchievementService {

	public static List<Achievement> getAchievementsByUser(User user){
		return new UserAchievementDao().getAchievementsByUserId(user);
	}
}
