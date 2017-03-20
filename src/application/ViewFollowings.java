package application;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.CurrentUser;
import model.DBConnection;
import model.QueryResult;

public class ViewFollowings implements MenuChoice {

	@Override
	public void execute() {
		String sqlQuery = "SELECT username, level, total_points "
				+ "FROM players "
				+ "WHERE EXISTS ("
				+ "SELECT * FROM followings "
				+ "WHERE p1 = '" + CurrentUser.getInstance().getUsername() + "' AND p2 = username);";

		QueryResult queryResult = DBConnection.getInstance().query(sqlQuery);
		ResultSet result = queryResult.getResult();
				
		try {
			System.out.println("Username, Level, Total Points");
			
			while (result.next()) {
				String username = result.getString("username");
				int level = result.getInt("level");
				int total_points = result.getInt("total_points");
				
				System.out.println(username + ", " + level + ", " + total_points);
			}
		} catch (SQLException e) {
			System.out.println("Error: failed to extract data from ResultSet");
			throw new RuntimeException(e);
		} finally {
			DBConnection.closeQueryResult(queryResult);
		}
	}

	@Override
	public String getDescription() {
		return "View Followed Players";
	}

}
