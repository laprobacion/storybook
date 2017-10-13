package com.read.storybook.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import com.read.storybook.model.Story;
import com.read.storybook.model.User;

public class UserStoryDao extends Dao{

	public void executeSave(User user, Story story, int score){
		PreparedStatement preparedStatement = null;
		String insertTableSQL = "INSERT INTO USERSTORY "
				+ "(USER_ID, STORY_ID, SCORE) VALUES"
				+ "(?,?,?)";
		try{
			preparedStatement = getConnection().prepareStatement(insertTableSQL);
			preparedStatement.setString(1, user.getId());
			preparedStatement.setString(2, story.getId());
			preparedStatement.setInt(3, score);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
	public Map<String,Integer> getStoriesAndScores(User user){
		PreparedStatement preparedStatement = null;
		Map<String,Integer> map = new TreeMap<String, Integer>();
		String selectSQL = "SELECT USER_ID, STORY_ID, SCORE FROM USERSTORY WHERE USER_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(selectSQL);
			preparedStatement.setString(1, user.getId());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				map.put(rs.getString("STORY_ID"), rs.getInt("SCORE"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
		return map;
	}
	
}
