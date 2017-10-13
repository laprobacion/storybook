package com.read.storybook.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.read.storybook.model.Achievement;
import com.read.storybook.model.User;

public class UserAchievementDao extends Dao{

	public void executeSave(User user, Achievement achievement){
		PreparedStatement preparedStatement = null;
		String insertTableSQL = "INSERT INTO USERACHIEVEMENT "
				+ "(USER_ID, ACHIEVEMENT_ID) VALUES"
				+ "(?,?)";
		try{
			preparedStatement = getConnection().prepareStatement(insertTableSQL);
			user.setId(generateId());
			preparedStatement.setString(1, user.getId());
			preparedStatement.setString(2, achievement.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
	public void deleteUserAchievement(User user, Achievement achievement){
		PreparedStatement preparedStatement = null;
		String deleteSQL = "DELETE USERACHIEVEMENT WHERE USER_ID = ? AND ACHIEVEMENT_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(deleteSQL);
			preparedStatement.setString(1, user.getId());
			preparedStatement.setString(2, achievement.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
	public List<Achievement> getAchievementsByUserId(User user){
		PreparedStatement preparedStatement = null;
		List<Achievement> achievements = new ArrayList<Achievement>();
		String selectSQL = "SELECT USER_ID, ACHIEVEMENT_ID FROM USERACHIEVEMENT WHERE USER_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(selectSQL);
			preparedStatement.setString(1, user.getId());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				achievements.add(new AchievementDao().getAchievementById(rs.getString("ACHIEVEMENT_ID")));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
		return achievements;
	}
}
