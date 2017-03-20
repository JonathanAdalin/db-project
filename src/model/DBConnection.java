package model;

import java.sql.*;

public class DBConnection {
	private Connection conn;
	private static DBConnection instance;
	
	private DBConnection() {
	}
	
	public static DBConnection getInstance() {
		if (instance == null) {
			instance = new DBConnection();
		}
		
		return instance;
	}
	
	public ResultSet query(String sqlString) {	
		connect();
		
		if (conn != null) {
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlString);
				stmt.close();
				
				return rs;
			} catch (SQLException e) {
				System.out.println("Error: Failed to query the Database");
				throw new RuntimeException(e);
			} finally {
				close();
			}	
		} else {
			throw new RuntimeException("Error, Connection conn is null");
		}		
	}
	
	private void connect() {
		try {
			
			/*
			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			*/
			
			DriverManager.registerDriver(new org.postgresql.Driver());

			String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
			
			conn = DriverManager.getConnection(url, "cs421g10", "n@n0_suX");
			
		} catch (SQLException e) {
			System.out.println("Error: Failed to connect to the Database.");
			throw new RuntimeException(e);
		}
	}
	
	private void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			//oh well
		}
	}
}
