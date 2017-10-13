package com.read.storybook.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.read.storybook.model.Choice;
import com.read.storybook.model.Question;

public class QuestionDao extends Dao{


	public void save(Question question){
		executeSave(question);
	}
	
	public void saveChild(Question question){
		for(Choice c : question.getChoices()){
			QuestionChoiceDao qcDao = new QuestionChoiceDao();
			qcDao.executeSave(question, c);
		}
	}
	public boolean update(Question q){
		Question question = getQuestionById(q.getId());
		if(question == null){
			return false;
		}else{
			executeUpdate(q);
			QuestionChoiceDao qcDao = new QuestionChoiceDao();
			List<Choice> dbChoices = qcDao.getChoicesByQuestionId(q);
			if(isChoicesUpdated(dbChoices, q.getChoices())){
				removeOldChoice(qcDao,dbChoices);
				saveChild(q);
			}
			return true;
		}
		
	}
	private void removeOldChoice(QuestionChoiceDao qcDao, List<Choice> dbChoices){
		for( Choice c: dbChoices){
			qcDao.deleteChoiceByChoiceId(c.getId());
			new ChoiceDao().delete(c);
		}
	}
	private boolean isChoicesUpdated(List<Choice> dbChoices, List<Choice> childChoices){
		if(dbChoices.size() != childChoices.size()){
			return true;
		}
		int count = dbChoices.size();
		int index = 0;
		for(Choice dbc: dbChoices){
			boolean flag = false;
			for(Choice c: childChoices){
				if(dbc.getId().equals(c.getId()) && dbc.getDescription().equals(c.getDescription())){
					flag = true;
				}
			}
			if(flag){
				index+= 1;
			}
		}
		return index != count;
	}
	
	public boolean delete(Question question){
		if(getQuestionById(question.getId()) != null){
			if(new QuestionChoiceDao().getChoicesByQuestionId(question).size() != 0 ){
				return false;
			}else{
				executeDelete("Question",question.getId());
				new StoryQuestionDao().deleteQuestion(question.getId());
				return true;
			}
		}else{
			return false;
		}
	}
	private void executeSave(Question question){
		PreparedStatement preparedStatement = null;
		String insertTableSQL = "INSERT INTO QUESTION "
				+ "(QUESTION_ID, DESCRIPTION, ANSWER, ISACTIVE) VALUES"
				+ "(?,?,?, ?)";
		try{
			preparedStatement = getConnection().prepareStatement(insertTableSQL);
			question.setId(generateId());
			preparedStatement.setString(1, question.getId());
			preparedStatement.setString(2, question.getDescription());
			preparedStatement.setBoolean(4, question.isActive());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
	private void executeUpdate(Question q){
		PreparedStatement preparedStatement = null;
		String updateTableSQL = "UPDATE QUESTION SET DESCRIPTION = ?,  ANSWER = ?"
                + " WHERE QUESTION_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(updateTableSQL);
			preparedStatement.setString(1, q.getDescription());
			preparedStatement.setString(3, q.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
	public Question getQuestionById(String id){
		PreparedStatement preparedStatement = null;
		Question q = null;
		String selectSQL = "SELECT QUESTION_ID, DESCRIPTION, ANSWER FROM QUESTION WHERE QUESTION_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(selectSQL);
			preparedStatement.setString(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				q = new Question();
				q.setId(rs.getString("QUESTION_ID"));
				q.setDescription(rs.getString("DESCRIPTION"));
				for(Choice c : new QuestionChoiceDao().getChoicesByQuestionId(q)){
					q.addChoice(c);
				}
				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
		return q;
	}
}
