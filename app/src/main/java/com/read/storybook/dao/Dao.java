package com.read.storybook.dao;

import android.app.Activity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class Dao {
	Connection connection = null;
	protected Connection getConnection(){
		try {
            String url = "jdbc:mysql://jabahan.com:3306/jabahanc_read?autoReconnect=true";
            String urlLocal = "jdbc:mysql://120.29.117.200:3306/jabahanc_read?autoReconnect=true";
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(urlLocal,"jabahanc_frank", "qv9ahX8HJ(y(");
		} catch (ClassNotFoundException e) {
			System.out.println("JDBC Driver not found");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Connection Failed!");
			e.printStackTrace();
		}
		return connection;
	}
	
	protected void close(PreparedStatement preparedStatement){
		try{
			//connection.commit();
			if (preparedStatement != null) {
				preparedStatement.close();
			}
	
			if (connection != null) {
				connection.close();
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	protected String generateId(){
		return UUID.randomUUID().toString();
	}
	protected void executeDelete(String table, String id){
		PreparedStatement preparedStatement = null;
		String deleteSQL = "DELETE "+table.toUpperCase()+" WHERE "+table.toUpperCase()+"_ID = ?";
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
