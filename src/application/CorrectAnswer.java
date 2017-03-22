package application;

import model.CurrentUser;
import model.DBConnection;

public class CorrectAnswer implements MenuChoice {

	private String label; 
	private int cid; 
	private int sid; 
	
	public CorrectAnswer(String label, int cid, int sid) {
		this.label = label;
		this.cid = cid;
		this.sid = sid;
	}
	
	@Override
	public void execute() {
		System.out.println("Correct Answer!");
		
		String sqlString = "INSERT INTO selections VALUES (" + sid + ", " + cid + ");";
		DBConnection.getInstance().insert(sqlString);
		
		updateLevel();
	}
	
	public void updateLevel() {
		String sqlString = "UPDATE players SET total_points = total_points + 100 WHERE username = '" + CurrentUser.getInstance().getUsername() +"';";
		DBConnection.getInstance().update(sqlString);
	}

	@Override
	public String getDescription() {
		return label;
	}

}
