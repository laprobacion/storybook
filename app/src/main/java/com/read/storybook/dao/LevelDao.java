package com.read.storybook.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.read.storybook.model.Level;
import com.read.storybook.model.Story;

public class LevelDao extends Dao{
	
	public boolean save(Level level){
		if(getLevelByName(level.getName()) != null){
			return false;
		}else{
			executeSave(level);
			return true;
		}
	}
	public void saveChild(Level level){
		List<Story> stories = level.getStories();
		for(Story story : stories){
			LevelStoryDao lsDao = new LevelStoryDao();
			lsDao.executeSave(level, story);
		}
	}
	private void removeOldStory(LevelStoryDao dao, List<Story> dbStories){
		for( Story s: dbStories){
			dao.deleteStory(s.getId());
			new StoryDao().delete(s);
		}
	}
	private boolean isStoryUpdated(List<Story> dbStories, List<Story> childStories){
		if(dbStories.size() != childStories.size()){
			return true;
		}
		int count = dbStories.size();
		int index = 0;
		for(Story dbs: dbStories){
			boolean flag = false;
			for(Story s: childStories){
				if(dbs.getId().equals(s.getId()) && 
						dbs.getTitle().equals(s.getTitle()) && 
						dbs.getBody().equals(s.getBody())){
					flag = true;
				}
			}
			if(flag){
				index+= 1;
			}
		}
		return index != count;
	}
	public boolean update(Level levelParam){
		Level level = getLevelById(levelParam.getId());
		if(level == null){
			return false;
		}else{
			executeUpdate(levelParam);
			LevelStoryDao dao = new LevelStoryDao();
			List<Story> dbStories = dao.getStoriesByLevelId(level);
			if(isStoryUpdated(dbStories, levelParam.getStories())){
				removeOldStory(dao, dbStories);
				saveChild(levelParam);
			}
			return true;
		}
	}
	
	public boolean delete(Level level){
		if(getLevelById(level.getId()) != null){
			if(new LevelStoryDao().getStoriesByLevelId(level).size() != 0){
				return false;
			}else{
				executeDelete("level",level.getId());
				return true;
			}
		}else{
			return false;
		}
	}
	
	private void executeSave(Level level){
		PreparedStatement preparedStatement = null;
		String insertTableSQL = "INSERT INTO LEVEL "
				+ "(LEVEL_ID, NAME, ISACTIVE) VALUES"
				+ "(?,?,?)";
		try{
			preparedStatement = getConnection().prepareStatement(insertTableSQL);
			level.setId(generateId());
			preparedStatement.setString(1, level.getId());
			preparedStatement.setString(2, level.getName());
			preparedStatement.setBoolean(3, level.isActive());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
	private void executeUpdate(Level level){
		PreparedStatement preparedStatement = null;
		String updateTableSQL = "UPDATE LEVEL SET NAME = ? "
                + "WHERE LEVEL_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(updateTableSQL);
			preparedStatement.setString(1, level.getName());
			preparedStatement.setString(2, level.getId());
			String s = preparedStatement.toString();
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
	public Level getLevelById(String id){
		PreparedStatement preparedStatement = null;
		Level level = null;
		String selectSQL = "SELECT LEVEL_ID, NAME FROM LEVEL WHERE LEVEL_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(selectSQL);
			preparedStatement.setString(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				level = new Level();
				level.setId(rs.getString("LEVEL_ID"));
				level.setName(rs.getString("NAME"));
				for(Story story : new LevelStoryDao().getStoriesByLevelId(level)){
					level.addStory(story);
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
		return level;
	}
	
	public Level getLevelByName(String name){
		Level level = null;
		PreparedStatement preparedStatement = null;
		String selectSQL = "SELECT LEVEL_ID, NAME FROM LEVEL WHERE NAME = ?";
		try{
			preparedStatement = getConnection().prepareStatement(selectSQL);
			preparedStatement.setString(1, name);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				level = new Level();
				level.setId(rs.getString("LEVEL_ID"));
				level.setName(rs.getString("NAME"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
		return level;
	}
}
