package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import model.CurrentUser;
import model.DBConnection;

public class SignUp implements MenuChoice {
	private static final int MIN_PASSWORD_SIZE = 8;
	private static final int MAX_PASSWORD_SIZE = 32;

	@Override
	public void execute() {
		Scanner uInput = new Scanner(System.in);

		while (true) {
			System.out.println("Username: ");
			String username = uInput.nextLine();
			
			System.out.println("Password: ");
			String password = uInput.nextLine();
			
			System.out.println("Confirm Password: ");
			String confirmedPassword = uInput.nextLine();

			
			if (!validUsername(username)) {
				System.out.println("Error: Your username is not unique");
				continue;
			}
			
			if (!confirmedPassword.equals(password)) {
				System.out.println("Error: Mismatched password");
				continue;
			}
			
			if (!validPassword(password)) {
				System.out.println("Error: Your password must contain between "
						+ MIN_PASSWORD_SIZE + " and " + MAX_PASSWORD_SIZE
						+ " characters and have at least 1 special character");
				
				continue;
			}
			
			addUser(username, password);
			
			CurrentUser.getInstance().setCredentials(username, password);
			
			break;
		}

		uInput.close();
		
		new Menu(Menu.PLAYER_MENU).start();
	}
	
	private void addUser(String username, String password) {
		String sqlUpdate = "INSERT INTO users VALUES ('" + username + "', '" + password + "', now(), 0);";
		DBConnection.getInstance().insert(sqlUpdate);
		
		sqlUpdate = "INSERT INTO players VALUES ('" + username + "', 0, 0, 0);";
		DBConnection.getInstance().insert(sqlUpdate);
	}
	
	private boolean validUsername(String username) {
		String sqlQuery = "SELECT username FROM Users WHERE username=\"" + username + "\";";
		ResultSet result = DBConnection.getInstance().query(sqlQuery);
	
		try {
			return result == null || !result.next();
		} catch (SQLException e) {
			//wat?
		} finally {
			DBConnection.closeResultSet(result);
		}
	
		return false;
	}
	
	public static boolean validPassword(String password) {
		boolean goodLength = password.length() >= MIN_PASSWORD_SIZE && password.length() <= MAX_PASSWORD_SIZE;
		boolean containsSpecialCharacters = password.contains("[^A-Za-z0-9]");
		
		return goodLength && containsSpecialCharacters;
	}

	@Override
	public String getDescription() {
		return "Sign up";
	}

}
