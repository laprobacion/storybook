package com.read.storybook.dao;

import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.read.storybook.model.Achievement;
import com.read.storybook.model.User;

public class AchievementDao extends Dao{

	
	private void executeSave(Achievement achivement){
		PreparedStatement preparedStatement = null;
		String insertTableSQL = "INSERT INTO ACHIEVEMENT "
				+ "(ACHIEVEMENT_ID, NAME, IMAGE) VALUES"
				+ "(?,?,?)";
		try{
			File file=new File(achivement.getImage());
            FileInputStream fis=new FileInputStream(file);
            
			preparedStatement = getConnection().prepareStatement(insertTableSQL);
			preparedStatement.setString(1, generateId());
			preparedStatement.setString(2, achivement.getName());
			preparedStatement.setBinaryStream(3,fis,(int)file.length());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
	private void executeUpdate(Achievement achivement){
		PreparedStatement preparedStatement = null;
		String updateTableSQL = "UPDATE ACHIEVEMENT SET NAME = ?, IMAGE = ? "
                + " WHERE USER_ID = ?";
		try{
			File file=new File(achivement.getImage());
            FileInputStream fis=new FileInputStream(file);
			preparedStatement = getConnection().prepareStatement(updateTableSQL);
			preparedStatement.setString(1, achivement.getName());
			preparedStatement.setBinaryStream(2,fis,(int)file.length());
			preparedStatement.setString(3, achivement.getId());
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
	public Achievement getAchievementById(String id){
		PreparedStatement preparedStatement = null;
		Achievement achievement = null;
		String selectSQL = "SELECT ACHIEVEMENT_ID, NAME FROM ACHIEVEMENT WHERE USER_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(selectSQL);
			preparedStatement.setString(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				achievement = new Achievement();
				achievement.setId(rs.getString("ACHIEVEMENT_ID"));
				achievement.setName(rs.getString("NAME"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
		return achievement;
	}
}
