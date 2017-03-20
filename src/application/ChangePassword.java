package application;

import model.CurrentUser;
import model.DBConnection;

public class ChangePassword implements MenuChoice{

	@Override
	public void execute() {
		while (true) {
			System.out.print("Old Password: ");
			String oldPassword = UserInput.getInstance().getString();
			
			if (!oldPassword.equals(CurrentUser.getInstance().getPassword())) {
				System.out.println("Error: Incorrect password");
				continue;
			}
			
			System.out.print("New Password: ");
			String newPassword = UserInput.getInstance().getString();

			System.out.print("Confirm New Password: ");
			String confirmedNewPassword = UserInput.getInstance().getString();
			
			if (!newPassword.equals(confirmedNewPassword)) {
				System.out.println("Error: Mismatched passwords");
				continue;
			}
			
			if (!SignUp.validPassword(newPassword)) {
				System.out.println("Error: Your password must contain between "
						+ SignUp.MIN_PASSWORD_SIZE + " and " + SignUp.MAX_PASSWORD_SIZE
						+ " characters and have at least 1 special character");
				continue;
			} 
			
			updatePassword(newPassword);
			
			System.out.println("Password updated successfully");
									
			break;
		}
	}

	private void updatePassword(String newPassword) {
		String sqlUpdate = "UPDATE users SET password = '" + newPassword + "' WHERE username = '" + CurrentUser.getInstance().getUsername() + "' ;";
		DBConnection.getInstance().update(sqlUpdate);
		
		CurrentUser.getInstance().setPassword(newPassword);
	}
	
	@Override
	public String getDescription() {
		return "Change Password";
	}

}
