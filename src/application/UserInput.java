package application;

import java.util.Scanner;

public class UserInput {
	private Scanner uInput;
	
	private static UserInput instance;
	
	private UserInput() {
		this.uInput = new Scanner(System.in);
	}
	
	public static UserInput getInstance() {
		if (instance == null) {
			instance = new UserInput();
		}
		
		return instance;
	}
	
	public int getInt() throws NumberFormatException{
		String temp = uInput.nextLine();
		int result = Integer.parseInt(temp);
		return result;
	}
	
	public String getString() {
		return uInput.nextLine();
	}
	
	public void close() {
		uInput.close();
		instance = null;
	}
}
