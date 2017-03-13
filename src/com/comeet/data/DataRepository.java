package com.comeet.data;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;

import com.comeet.Utilities.ApiLogger;
import com.mysql.cj.jdbc.MysqlDataSource;

public class DataRepository {
	
	private Connection con;
	
	private void setupConn() throws Exception{
		
		Properties prop = new Properties();
	    InputStream input = null;
		
			
		input = new FileInputStream("config.properties");
	    prop.load(input);
			
		MysqlDataSource dataSource = new MysqlDataSource();
		dataSource.setUser(prop.getProperty("dbpassword"));
		dataSource.setPassword("tiger");
		dataSource.setServerName(prop.getProperty("dbendpoint"));
		
		con = dataSource.getConnection();
	}
	
	private void closeConnection() throws Exception{
		con.close();
	}
	
	private void getRooms(){
		try{
			
			setupConn();
			
			Statement stmt=con. createStatement();  
			ResultSet rs =stmt.executeQuery("select * from rooms");
			
			//TO DO - process result set to appropriate result
			

			
		}catch(Exception e){
			ApiLogger.logger.log(Level.SEVERE, "Error getting rooms from database database", e);
		}
		
	}
	
	private void retrieveRoomInfo(int Id){
		try{
			
			setupConn();
			
			PreparedStatement stmt=con.prepareStatement("select * from rooms where id = ?");
			stmt.setInt(1, Id);
			stmt.execute();
			ResultSet rs = stmt.getResultSet();
			
			//TO DO - process result set to apppriate result
			
			closeConnection();
		}catch(Exception e){
			ApiLogger.logger.log(Level.SEVERE, "Error getting rooms from database database", e);
		}
	}
	
}
