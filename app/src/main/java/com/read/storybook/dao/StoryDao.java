package com.read.storybook.dao;

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.read.storybook.model.Question;
import com.read.storybook.model.Story;

public class StoryDao extends Dao{
	
	public boolean update(Story story){
		Story s = getStoryById(story.getId());
		if(s == null){
			return false;
		}else{
			executeUpdate(story);
			StoryQuestionDao dao = new StoryQuestionDao();
			List<Question> dbQuestions = dao.getQuestionsByStoryId(story);
			if(isQuestionUpdated(dbQuestions, story.getQuestions())){
				removeOldQuestion(dao, dbQuestions);
				saveChild(story);
			}
			return true;
		}
	}
	private void removeOldQuestion(StoryQuestionDao dao, List<Question> dbQuestions){
		for( Question q: dbQuestions){
			dao.deleteQuestion(q.getId());
			new QuestionDao().delete(q);
		}
	}
	private boolean isQuestionUpdated(List<Question> dbQuestions, List<Question> childQuestions){
		if(dbQuestions.size() != childQuestions.size()){
			return true;
		}
		int count = dbQuestions.size();
		int index = 0;
		for(Question dbq: dbQuestions){
			boolean flag = false;
			for(Question q: childQuestions){
				if(dbq.getId().equals(q.getId()) && dbq.getDescription().equals(q.getDescription())){
					flag = true;
				}
			}
			if(flag){
				index+= 1;
			}
		}
		return index != count;
	}
	public boolean delete(Story story){
		if(getStoryById(story.getId()) != null){
			if(new StoryQuestionDao().getQuestionsByStoryId(story).size() != 0){
				return false;
			}else{
				executeDelete("story",story.getId());
				new LevelStoryDao().deleteStory(story.getId());
				return true;
			}
		}else{
			return false;
		}
	}
	
	public Story getStoryById(String id){
		PreparedStatement preparedStatement = null;
		Story story = null;
		String selectSQL = "SELECT STORY_ID, TITLE, BODY, MAXQUESTION FROM STORY WHERE STORY_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(selectSQL);
			preparedStatement.setString(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				story = new Story();
				story.setId(rs.getString("STORY_ID"));
				story.setTitle(rs.getString("TITLE"));
				for(Question q: new StoryQuestionDao().getQuestionsByStoryId(story)){
					story.addQuestion(q);
				}
				story.setMaxQuestion(rs.getInt("MAXQUESTION"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
		return story;
	}
	
	public Story save(Story story){
		executeSave(story);
		return story;
	}
	
	public void saveChild(Story story){
		for(Question q : story.getQuestions()){
			StoryQuestionDao sqDao = new StoryQuestionDao();
			sqDao.executeSave(story, q);
		}
	}
	private Story executeSave(Story story){
		PreparedStatement preparedStatement = null;
		String insertTableSQL = "INSERT INTO STORY "
				+ "(STORY_ID, TITLE, ISACTIVE, MAXQUESTION) VALUES "
				+ "(?,?,?,?)";
		try{
			//File file=new File(story.getBody());
            //FileInputStream fis=new FileInputStream(file);
            
			preparedStatement = getConnection().prepareStatement(insertTableSQL);
			story.setId(generateId());
			preparedStatement.setString(1, story.getId());
			preparedStatement.setString(2, story.getTitle());
			preparedStatement.setBoolean(3,story.isActive());
			preparedStatement.setInt(4, story.getMaxQuestion());
			//preparedStatement.setBinaryStream(4,fis,(int)file.length());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
		return story;
	}
	
	private void executeUpdate(Story story){
		PreparedStatement preparedStatement = null;
		String updateTableSQL = "UPDATE STORY SET TITLE = ?"//, BODY = ?"
                + " WHERE STORY_ID = ?";
		try{
			//File file=new File(story.getBody());
            //FileInputStream fis=new FileInputStream(file);
			preparedStatement = getConnection().prepareStatement(updateTableSQL);
			preparedStatement.setString(1, story.getTitle());
			//preparedStatement.setBinaryStream(2,fis,(int)file.length());
			preparedStatement.setString(2, story.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
}
