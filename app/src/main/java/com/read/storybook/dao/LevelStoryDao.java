package com.read.storybook.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.read.storybook.model.Level;
import com.read.storybook.model.Story;

public class LevelStoryDao extends Dao{

	public void executeSave(Level level, Story story){
		PreparedStatement preparedStatement = null;
		String insertTableSQL = "INSERT INTO LEVELSTORY "
				+ "(LEVEL_ID, STORY_ID) VALUES"
				+ "(?,?)";
		try{
			preparedStatement = getConnection().prepareStatement(insertTableSQL);
			preparedStatement.setString(1, level.getId());
			preparedStatement.setString(2, story.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
	public List<Story> getStoriesByLevelId(Level level){
		PreparedStatement preparedStatement = null;
		List<Story> stories = null;
		String selectSQL = "SELECT LEVEL_ID, STORY_ID FROM LEVELSTORY WHERE LEVEL_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(selectSQL);
			preparedStatement.setString(1, level.getId());
			ResultSet rs = preparedStatement.executeQuery();
			stories = new ArrayList<Story>();
			while (rs.next()) {
				stories.add(new StoryDao().getStoryById(rs.getString("STORY_ID")));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
		return stories;
	}
	
	public void deleteStory(String id){
		PreparedStatement preparedStatement = null;
		String deleteSQL = "DELETE LEVELSTORY WHERE STORY_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(deleteSQL);
			preparedStatement.setString(1, id);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
}
