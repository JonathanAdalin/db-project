package application;

import model.DBConnection;

public class IncorrectAnswer implements MenuChoice {
	
	private String label; 
	private int cid;
	private int sid;
	
	public IncorrectAnswer(String label, int cid, int sid) {
		this.label = label;
		this.cid = cid;
		this.sid = sid;
	}
	
	@Override
	public void execute() {
		System.out.println("Incorrect Answer... (sorry)");
		
		String sqlString = "INSERT INTO selections VALUES (" + sid + ", " + cid + ");";
		DBConnection.getInstance().insert(sqlString);
	}

	@Override
	public String getDescription() {
		return label;
	}
	
}
