package com.read.storybook.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.read.storybook.model.Choice;

public class ChoiceDao extends Dao{
	
	public void save(Choice choice){
		executeSave(choice);
	}
	
	public boolean update(Choice choiceParam){
		Choice choice = getChoiceById(choiceParam.getId());
		if(choice == null){
			return false;
		}else{
			executeUpdate(choiceParam);
			return true;
		}
	}
	
	public boolean delete(Choice choice){
		if(getChoiceById(choice.getId()) != null){
			executeDelete("choice",choice.getId());
			new QuestionChoiceDao().deleteChoiceByChoiceId(choice.getId());
			return true;
		}else{
			return false;
		}
	}

	private void executeSave(Choice choice){
		PreparedStatement preparedStatement = null;
		String insertTableSQL = "INSERT INTO CHOICE "
				+ "(CHOICE_ID, DESCRIPTION, ISACTIVE) VALUES"
				+ "(?,?,?)";
		try{
			preparedStatement = getConnection().prepareStatement(insertTableSQL);
			choice.setId(generateId());
			preparedStatement.setString(1, choice.getId());
			preparedStatement.setString(2, choice.getDescription());
			preparedStatement.setBoolean(3, choice.isActive());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
	private void executeUpdate(Choice choice){
		PreparedStatement preparedStatement = null;
		String updateTableSQL = "UPDATE CHOICE SET DESCRIPTION = ? "
                + "WHERE CHOICE_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(updateTableSQL);
			preparedStatement.setString(1, choice.getDescription());
			preparedStatement.setString(2, choice.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
	public Choice getChoiceById(String id){
		PreparedStatement preparedStatement = null;
		Choice choice = null;
		String selectSQL = "SELECT CHOICE_ID, DESCRIPTION FROM CHOICE WHERE CHOICE_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(selectSQL);
			preparedStatement.setString(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				choice = new Choice();
				choice.setId(rs.getString("CHOICE_ID"));
				choice.setDescription(rs.getString("DESCRIPTION"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
		return choice;
	}
	
}
