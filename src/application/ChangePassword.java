package application;

import java.util.Scanner;

import model.CurrentUser;
import model.DBConnection;

public class ChangePassword implements MenuChoice{

	@Override
	public void execute() {
		Scanner uInput = new Scanner(System.in);

		while (true) {
			System.out.println("Old Password: ");
			String oldPassword = uInput.nextLine();
			
			if (!oldPassword.equals(CurrentUser.getInstance().getPassword())) {
				System.out.println("Error: Incorrect password");
				continue;
			}
			
			System.out.println("New Password: ");
			String newPassword = uInput.nextLine();

			System.out.println("Confirm New Password: ");
			String confirmedNewPassword = uInput.nextLine();
			
			if (!newPassword.equals(confirmedNewPassword)) {
				System.out.println("Error: Mismatched passwords");
				continue;
			}
			
			if (!SignUp.validPassword(newPassword)) {
				System.out.println("Error: Incorrect username or password");
				continue;
			} 
			
			updatePassword(newPassword);
			
			System.out.println("Password updated successfully");
									
			break;
		}

		uInput.close();
	}

	private void updatePassword(String newPassword) {
		String sqlUpdate = "UPDATE users SET password = " + newPassword + " WHERE username = \"" + CurrentUser.getInstance().getUsername() + "\" ;";
		DBConnection.getInstance().update(sqlUpdate);
		
		CurrentUser.getInstance().setPassword(newPassword);
	}
	
	@Override
	public String getDescription() {
		return "Change Password";
	}

}
