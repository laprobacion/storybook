package com.read.storybook.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.read.storybook.model.Level;
import com.read.storybook.model.Question;
import com.read.storybook.model.Story;

public class StoryQuestionDao extends Dao{

	public void executeSave(Story story, Question question){
		PreparedStatement preparedStatement = null;
		String insertTableSQL = "INSERT INTO STORYQUESTION "
				+ "(STORY_ID, QUESTION_ID) VALUES"
				+ "(?,?)";
		try{
			preparedStatement = getConnection().prepareStatement(insertTableSQL);
			preparedStatement.setString(1, story.getId());
			preparedStatement.setString(2, question.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	public List<Question> getQuestionsByStoryId(Story story){
		PreparedStatement preparedStatement = null;
		List<Question> questions = null;
		String selectSQL = "SELECT STORY_ID, QUESTION_ID FROM STORYQUESTION WHERE STORY_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(selectSQL);
			preparedStatement.setString(1, story.getId());
			ResultSet rs = preparedStatement.executeQuery();
			questions = new ArrayList<Question>();
			while (rs.next()) {
				questions.add(new QuestionDao().getQuestionById(rs.getString("QUESTION_ID")));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
		return questions;
	}
	
	public void deleteQuestion(String id){
		PreparedStatement preparedStatement = null;
		String deleteSQL = "DELETE STORYQUESTION WHERE QUESTION_ID = ?";
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
