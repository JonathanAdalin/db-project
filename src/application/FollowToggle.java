package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import model.CurrentUser;
import model.DBConnection;

public class FollowToggle implements MenuChoice {

	@Override
	public void execute() {
		Scanner uInput = new Scanner(System.in);
		
		while (true) {
			System.out.println("Username: ");
			
			String username = uInput.nextLine();
			
			if (alreadyFollowing(username)) {
				removeFollowing(username);
				System.out.println("Stopped following user: " + username);
				break;
			} 
			
			if (userExists(username)) {
				addFollowing(username);
				System.out.println("Started following user: " + username);
				break;
			}
			
			System.out.println("Error: user does not exist, please try again");
		}
		
		uInput.close();
	}
	
	private boolean userExists(String username) {
		String sqlQuery = "SELECT * "
				+ "FROM users "
				+ "WHERE username = \"" + username + "\";";

		ResultSet result = DBConnection.getInstance().query(sqlQuery);
		
		boolean exists;
		
		try {
			exists = result.next();
		} catch (SQLException e) {
			System.out.println("Error: database access error or closed result set");
			throw new RuntimeException(e);
		}
		
		DBConnection.closeResultSet(result);
		
		return exists;
	}
	
	private void addFollowing(String username) {
		String sqlUpdate = "INSERT INTO followings VALUES ('" + CurrentUser.getInstance().getUsername() + "', '" + username + "');";
		DBConnection.getInstance().insert(sqlUpdate);

	}
	
	private void removeFollowing(String username) {
		String sqlString = "DELETE FROM followings WHERE p1 = \"" + CurrentUser.getInstance().getUsername() + "\" AND p2 = \"" + username + "\";";
		DBConnection.getInstance().delete(sqlString);
	}
	
	private boolean alreadyFollowing(String username) {
		String sqlQuery = "SELECT * "
				+ "FROM followings "
				+ "WHERE p1 = \"" + CurrentUser.getInstance().getUsername() + "\" AND p2 = \"" + username + "\";";

		ResultSet result = DBConnection.getInstance().query(sqlQuery);
		
		boolean alreadyFollowing;
		
		try {
			alreadyFollowing = result.next();
		} catch (SQLException e) {
			System.out.println("Error: database access error or closed result set");
			throw new RuntimeException(e);
		}
		
		DBConnection.closeResultSet(result);
		
		return alreadyFollowing;
	}

	@Override
	public String getDescription() {
		return "Follow/Unfollow";
	}

}
