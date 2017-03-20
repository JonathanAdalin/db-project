package application;

import java.sql.ResultSet;

import model.DBConnection;

public class Application {
	private static String prompt = "";
	
	//1. sign up
		//1.1 Username
		//1.2 Password
	//2. sign in
		//2.1 Username
		//2.2 Password
	//3. quit
	
	//users
	//----------------------------------
	//x. start a session
	//		x.1 pick number of questions
	//x.	 view leaderboard
	//x. view followed users
	//x. follow/unfollow a player
	//x. change password
	//x. quit
	
	public static void main(String args[]) {
		
		
				
		String sqlQuery = "SELECT * FROM questions;";
		ResultSet rs = DBConnection.getInstance().query(sqlQuery);
		System.out.println(rs);
				
	}
}
