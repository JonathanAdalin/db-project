package application;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.CurrentUser;
import model.DBConnection;
import model.QueryResult;

public class FollowToggle implements MenuChoice {

	@Override
	public void execute() {		
		while (true) {
			System.out.print("Username: ");
			
			String username = UserInput.getInstance().getString();
			
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
	}
	
	private boolean userExists(String username) {
		String sqlQuery = "SELECT * "
				+ "FROM users "
				+ "WHERE username = '" + username + "';";

		QueryResult queryResult = DBConnection.getInstance().query(sqlQuery);
		ResultSet result = queryResult.getResult();
		
		boolean exists;
		
		try {
			exists = result.next();
		} catch (SQLException e) {
			System.out.println("Error: failed to extract data from ResultSet");
			throw new RuntimeException(e);
		}
		
		DBConnection.closeQueryResult(queryResult);
		
		return exists;
	}
	
	private void addFollowing(String username) {
		String sqlUpdate = "INSERT INTO followings VALUES ('" + CurrentUser.getInstance().getUsername() + "', '" + username + "');";
		DBConnection.getInstance().insert(sqlUpdate);

	}
	
	private void removeFollowing(String username) {
		String sqlString = "DELETE FROM followings WHERE p1 = '" + CurrentUser.getInstance().getUsername() + "' AND p2 = '" + username + "';";
		DBConnection.getInstance().delete(sqlString);
	}
	
	private boolean alreadyFollowing(String username) {
		String sqlQuery = "SELECT * "
				+ "FROM followings "
				+ "WHERE p1 = '" + CurrentUser.getInstance().getUsername() + "' AND p2 = '" + username + "';";

		QueryResult queryResult = DBConnection.getInstance().query(sqlQuery);
		ResultSet result = queryResult.getResult();
		
		boolean alreadyFollowing;
		
		try {
			alreadyFollowing = result.next();
		} catch (SQLException e) {
			System.out.println("Error: failed to extract data from ResultSet");
			throw new RuntimeException(e);
		}
		
		DBConnection.closeQueryResult(queryResult);
		
		return alreadyFollowing;
	}

	@Override
	public String getDescription() {
		return "Follow/Unfollow";
	}

}
