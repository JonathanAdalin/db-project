package application;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.DBConnection;
import model.QueryResult;

public class ViewLeaderboard implements MenuChoice {

	@Override
	public void execute() {
		String sqlQuery = "SELECT username, level, total_points"
						+ " FROM players"
						+ " ORDER BY level DESC, total_points DESC"
						+ " LIMIT 10;";
		
		QueryResult queryResult = DBConnection.getInstance().query(sqlQuery);
		ResultSet result = queryResult.getResult();
				
		try {
			System.out.println("Username, Level, Total Points");
			
			int counter = 1;
			while (result.next()) {
				String username = result.getString("username");
				int level = result.getInt("level");
				int total_points = result.getInt("total_points");
				
				System.out.println(counter++ + ". " + username + ", " + level + ", " + total_points);
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
		return "View Leaderboard";
	}

}
