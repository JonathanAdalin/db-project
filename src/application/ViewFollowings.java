package application;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.CurrentUser;
import model.DBConnection;

public class ViewFollowings implements MenuChoice {

	@Override
	public void execute() {
		String sqlQuery = "SELECT username, level, total_points "
				+ "FROM players "
				+ "WHERE EXISTS ("
				+ "SELECT * FROM followings "
				+ "WHERE p1 = '" + CurrentUser.getInstance().getUsername() + "' AND p2 = username);";

		ResultSet result = DBConnection.getInstance().query(sqlQuery);
		
		try {
			System.out.println("Username, Level, Total Points");
			
			while (result.next()) {
				String username = result.getString("username");
				int level = result.getInt("level");
				int total_points = result.getInt("total_points");
				
				System.out.println(username + ", " + level + ", " + total_points);
			}
		} catch (SQLException e) {
			System.out.println("Error: database access error or closed result set");
			throw new RuntimeException(e);
		}
		
		DBConnection.closeResultSet(result);
	}

	@Override
	public String getDescription() {
		return "View Followed Players";
	}

}
