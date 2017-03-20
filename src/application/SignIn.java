package application;

import java.sql.ResultSet;
import java.sql.SQLException;

import model.CurrentUser;
import model.DBConnection;
import model.QueryResult;

public class SignIn implements MenuChoice {

	@Override
	public void execute() {
		
		while (true) {
			System.out.print("Username: ");
			String username = UserInput.getInstance().getString();;

			System.out.print("Password: ");
			String password = UserInput.getInstance().getString();
			
			if (!validCredentials(username, password)) {
				System.out.println("Error: Incorrect username or password");
				continue;
			} 
			
			CurrentUser.getInstance().setCredentials(username, password);
			System.out.println("Sign in successful");
			
			break;
		}
		
		new Menu(Menu.PLAYER_MENU).start();
	}

	private boolean validCredentials(String username, String password) {
		String sqlString = "SELECT password FROM Users WHERE username='" + username + "';";
		QueryResult queryResult = DBConnection.getInstance().query(sqlString);
		ResultSet result = queryResult.getResult();
		
		try {
			if (result != null) {
				result.next();
				String dbPassword = result.getString("password");
				return dbPassword.equals(password);
			} else {
				return false;
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
		return "Sign in";
	}

}
