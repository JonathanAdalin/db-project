package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import model.CurrentUser;
import model.DBConnection;

public class SignIn implements MenuChoice {

	@Override
	public void execute() {
		Scanner uInput = new Scanner(System.in);

		while (true) {
			System.out.println("Username: ");
			String username = uInput.nextLine();

			System.out.println("Password: ");
			String password = uInput.nextLine();
			
			if (!validCredentials(username, password)) {
				System.out.println("Error: Incorrect username or password");
				continue;
			} 
			
			CurrentUser.getInstance().setCredentials(username, password);
						
			break;
		}

		uInput.close();
		
		new Menu(Menu.PLAYER_MENU).start();
	}

	private boolean validCredentials(String username, String password) {
		String sqlString = "SELECT password FROM Users WHERE username=" + username + ";";
		ResultSet result = DBConnection.getInstance().query(sqlString);
		
		try {
			if (result != null) {
				String dbPassword = result.getString("password");
				return dbPassword.equals(password);
			} else {
				return false;
			}
		} catch (SQLException e) {
			return false;
		} finally {
			DBConnection.closeResultSet(result);
		}
	}
	
	@Override
	public String getDescription() {
		return "Sign in";
	}

}
