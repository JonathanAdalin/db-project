package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import model.DBConnection;

public class SignUp implements MenuChoice {
	private static final int MIN_PASSWORD_SIZE = 8;
	private static final int MAX_PASSWORD_SIZE = 32;

	@Override
	public void execute() {

		while (true) {
			Scanner uInput = new Scanner(System.in);

			System.out.println("Username: ");
			String username = uInput.nextLine();

			System.out.println("Password: ");
			String password = uInput.nextLine();
			
			if (!validUsername(username)) {
				System.out.println("Error: Your username is not unique");
				continue;
			}
			
			if (!validPassword(password)) {
				System.out.println("Error: Your password must contain between "
						+ MIN_PASSWORD_SIZE + " and " + MAX_PASSWORD_SIZE
						+ " characters and have at least 1 special character");
				
				continue;
			}
		}

	}
	
	private boolean validUsername(String username) {
		String sqlQuery = "SELECT username FROM Users WHERE username=\"" + username + "\";";
		ResultSet result = DBConnection.getInstance().query(sqlQuery);
	
		try {
			return result == null || result.next();
		} catch (SQLException e) {
			//wat?
		}
		//TODO
		return false;
	}
	
	private boolean validPassword(String password) {
		boolean goodLength = password.length() >= MIN_PASSWORD_SIZE && password.length() <= MAX_PASSWORD_SIZE;
		boolean containsSpecialCharacters = password.contains("[^A-Za-z0-9]");
		
		return goodLength && containsSpecialCharacters;
	}

	@Override
	public String getDescription() {
		return "Sign up";
	}

}
