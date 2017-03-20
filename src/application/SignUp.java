package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import model.CurrentUser;
import model.DBConnection;
import model.QueryResult;

public class SignUp implements MenuChoice {
	private final static Logger logger = Logger.getLogger(MenuChoice.class);

	public static final int MIN_PASSWORD_SIZE = 8;
	public static final int MAX_PASSWORD_SIZE = 32;

	@Override
	public void execute() {

		while (true) {
			System.out.print("Username: ");
			String username = UserInput.getInstance().getString();
			
			System.out.print("Password: ");
			String password = UserInput.getInstance().getString();
			
			System.out.print("Confirm Password: ");
			String confirmedPassword = UserInput.getInstance().getString();

			
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
			
			System.out.println("Sign up successful");
			
			break;
		}
		
		new Menu(Menu.PLAYER_MENU).start();
	}
	
	private void addUser(String username, String password) {
		String sqlUpdate = "INSERT INTO users VALUES ('" + username + "', '" + password + "', now(), 0);";
		DBConnection.getInstance().insert(sqlUpdate);
		
		sqlUpdate = "INSERT INTO players VALUES ('" + username + "', 0, 0, 0);";
		DBConnection.getInstance().insert(sqlUpdate);
	}
	
	private boolean validUsername(String username) {
		String sqlQuery = "SELECT username FROM Users WHERE username='" + username + "';";
		
		QueryResult queryResult = DBConnection.getInstance().query(sqlQuery);
		ResultSet result = queryResult.getResult();
				
		try {
			return result == null || !result.next();
		} catch (SQLException e) {
			System.out.println("Error: failed to extract data from ResultSet");
			throw new RuntimeException(e);
		} finally {
			DBConnection.closeQueryResult(queryResult);
		}
	}
	
	public static boolean validPassword(String password) {
		boolean goodLength = password.length() >= MIN_PASSWORD_SIZE && password.length() <= MAX_PASSWORD_SIZE;
		
		Pattern p = Pattern.compile("[^A-Za-z0-9]");   
	    Matcher m = p.matcher(password);
	    boolean containsSpecialCharacters = m.find();
	    	
		return goodLength && containsSpecialCharacters;
	}

	@Override
	public String getDescription() {
		return "Sign up";
	}

}
