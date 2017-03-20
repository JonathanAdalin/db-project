package model;

public class CurrentUser {
	private String username;
	private String password;
	
	//singleton instance
	private static CurrentUser instance;
	
	private CurrentUser() {	
	}
	
	/**
	 * @return the singleton instance
	 */
	public static CurrentUser getInstance() {
		if (instance == null) {
			instance = new CurrentUser();
		}
		
		return instance;
	}
	
	//SETTERS
	
	public void setCredentials(String username, String password) {
		setUsername(username);
		setPassword(password);
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	//CLEARERS
	
	public void clearCredentials() {
		clearUsername();
		clearPassword();
	}
	
	public void clearUsername() {
		this.username = null;
	}
	
	public void clearPassword() {
		this.password = null;
	}
	
	//GETTERS
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
}
