package com.read.storybook.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.read.storybook.model.Choice;
import com.read.storybook.model.Question;

public class QuestionChoiceDao extends Dao{

	public void executeSave(Question question, Choice choice){
		PreparedStatement preparedStatement = null;
		String insertTableSQL = "INSERT INTO QUESTIONCHOICE "
				+ "(QUESTION_ID, CHOICE_ID) VALUES"
				+ "(?,?)";
		try{
			preparedStatement = getConnection().prepareStatement(insertTableSQL);
			preparedStatement.setString(1, question.getId());
			preparedStatement.setString(2, choice.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
	public List<Choice> getChoicesByQuestionId(Question question){
		PreparedStatement preparedStatement = null;
		List<Choice> choices = null;
		String selectSQL = "SELECT QUESTION_ID, CHOICE_ID FROM QUESTIONCHOICE WHERE QUESTION_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(selectSQL);
			preparedStatement.setString(1, question.getId());
			ResultSet rs = preparedStatement.executeQuery();
			choices = new ArrayList<Choice>();
			while (rs.next()) {
				choices.add(new ChoiceDao().getChoiceById(rs.getString("CHOICE_ID")));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
		return choices;
	}
	
	public void deleteChoiceByQuestionId(String id){
		PreparedStatement preparedStatement = null;
		String deleteSQL = "DELETE QUESTIONCHOICE WHERE QUESTION_ID = ?";
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
	
	public void deleteChoiceByChoiceId(String id){
		PreparedStatement preparedStatement = null;
		String deleteSQL = "DELETE QUESTIONCHOICE WHERE CHOICE_ID = ?";
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
