package com.read.storybook.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.read.storybook.model.User;
import com.read.storybook.util.Encryptor;

public class UserDao extends Dao{
	
	public User save(User user){
		if(getUserByUsername(user.getUsername()) != null){
			return null;
		}else{
			executeSave(user);
			return user;
		}
	}
	
	public User update(User userParam){
		User user = getUserById(userParam.getId());
		if(user == null){
			return null;
		}else{
			executeUpdate(user);
			return user;
		}
	}
	
	public User updatePassword(User userParam){
		User user = getUserById(userParam.getId());
		if(user == null){
			return null;
		}else{
			executeUpdatePassword(user);
			return user;
		}
	}
	
	public User delete(User user){
		if(getUserById(user.getId()) != null){
			executeDelete("user",user.getId());
			return user;
		}else{
			return null;
		}
	}
	private void executeSave(User user){
		PreparedStatement preparedStatement = null;
		String insertTableSQL = "INSERT INTO USER "
				+ "(USER_ID, USERNAME, PASSWORD, ISACTIVE, IS_ADMIN) VALUES"
				+ "(?,?,?,?)";
		try{
			preparedStatement = getConnection().prepareStatement(insertTableSQL);
			user.setId(generateId());
			preparedStatement.setString(1, user.getId());
			preparedStatement.setString(2, user.getUsername());
			preparedStatement.setString(3, Encryptor.encrypt(user.getPassword()));
			preparedStatement.setBoolean(4, user.isActive());
			preparedStatement.setBoolean(5, user.isAdmin());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
	
	
	public User getUserById(String id){
		PreparedStatement preparedStatement = null;
		User user = null;
		String selectSQL = "SELECT IS_ADMIN, USER_ID, USERNAME, PASSWORD FROM USER WHERE USER_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(selectSQL);
			preparedStatement.setString(1, id);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				user = new User();
				user.setId(rs.getString("USER_ID"));
				user.setUsername(rs.getString("USERNAME"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setAdmin(rs.getBoolean("IS_ADMIN"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
		return user;
	}
	
	public User getUserByUsername(String username){
		User user = null;
		PreparedStatement preparedStatement = null;
		String selectSQL = "SELECT IS_ADMIN, USER_ID, USERNAME, PASSWORD FROM USER WHERE USERNAME = ?";
		try{
			preparedStatement = getConnection().prepareStatement(selectSQL);
			preparedStatement.setString(1, username);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				user = new User();
				user.setId(rs.getString("USER_ID"));
				user.setUsername(rs.getString("USERNAME"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setAdmin(rs.getBoolean("IS_ADMIN"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
		return user;
	}
	
	public List<User> getAllUsers(){
		List<User> users = new ArrayList<User>();
		PreparedStatement preparedStatement = null;
		String selectSQL = "SELECT IS_ADMIN, USER_ID, USERNAME, PASSWORD FROM USER";
		try{
			preparedStatement = getConnection().prepareStatement(selectSQL);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setId(rs.getString("USER_ID"));
				user.setUsername(rs.getString("USERNAME"));
				user.setPassword(rs.getString("PASSWORD"));
				user.setAdmin(rs.getBoolean("IS_ADMIN"));
				users.add(user);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
		return users;
	}
	
	private void executeUpdatePassword(User user){
		PreparedStatement preparedStatement = null;
		String updateTableSQL = "UPDATE USER SET PASSWORD = ? "
                + " WHERE USER_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(updateTableSQL);
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, Encryptor.encrypt(user.getPassword()));
			preparedStatement.setString(3, user.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
	
	private void executeUpdate(User user){
		PreparedStatement preparedStatement = null;
		String updateTableSQL = "UPDATE USER SET USERNAME = ?"
                + " WHERE USER_ID = ?";
		try{
			preparedStatement = getConnection().prepareStatement(updateTableSQL);
			preparedStatement.setString(1, user.getUsername());
			preparedStatement.setString(2, user.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			close(preparedStatement);
		}
	}
}
