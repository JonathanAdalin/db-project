package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import model.CurrentUser;
import model.DBConnection;

public class StartSession implements MenuChoice {

	@Override
	public void execute() {
		Scanner uInput = new Scanner(System.in);
		int questionCount;
		while (true) {
			System.out.println("Desired number of questions: ");
			String input = uInput.nextLine();
			
			try {
				questionCount = Integer.parseInt(input);
				
				if (questionCount <= 0 || questionCount > 9000) {
					throw new NumberFormatException();
				}
				
				break;
			} catch (NumberFormatException nfe) {
				System.out.println("Error: input must be greater than zero and cannot be over 9000");
			}
		}
		uInput.close();
		
		DBConnection.getInstance().executeCreateSessionStoredProcedure(questionCount);
		int sessionId = findNewSession();
		List<Integer> questionIds = getQuestions(sessionId);
		
		for (int qid : questionIds) {
			List<String> contents = getChoicesContents(qid);
			List<Integer> cids = getChoicesIds(qid);

			int counter = 0;
			MenuChoice[] choices = new MenuChoice[contents.size()];
			choices[counter] = new CorrectAnswer(contents.get(counter), cids.get(counter), sessionId);
			
			for ( ; counter < contents.size(); counter++) {
				choices[counter] = new IncorrectAnswer(contents.get(counter), cids.get(counter), sessionId); 
			}
			
			new Menu(choices);
		}
	}
	
	private List<String> getChoicesContents(int questionId) {
		List<String> ids = new LinkedList<String>();

		String sqlQuery = "SELECT content FROM choices WHERE qid = " + questionId + " ORDER BY is_correct DESC;"; 
		ResultSet result = DBConnection.getInstance().query(sqlQuery);
		
		try {
			while (result.next()) {
				ids.add(result.getString("content"));
			}
		} catch (SQLException e) {
			System.out.println("Error: failed to extract data from ResultSet");
			throw new RuntimeException(e);
		}

		return ids;
	}
	
	private List<Integer> getChoicesIds(int questionId) {
		List<Integer> ids = new LinkedList<Integer>();

		String sqlQuery = "SELECT cid FROM choices WHERE qid = " + questionId + " ORDER BY is_correct DESC;"; 
		ResultSet result = DBConnection.getInstance().query(sqlQuery);
		
		try {
			while (result.next()) {
				ids.add(result.getInt("cid"));
			}
		} catch (SQLException e) {
			System.out.println("Error: failed to extract data from ResultSet");
			throw new RuntimeException(e);
		}

		return ids;
	}
	
	private List<Integer> getQuestions(int sessionId) {
		List<Integer> ids = new LinkedList<Integer>();
		
		String sqlQuery = "SELECT qid FROM containments WHERE sid = " + sessionId + ";"; 
		ResultSet result = DBConnection.getInstance().query(sqlQuery);
		
		try {
			while (result.next()) {
				ids.add(result.getInt("qid"));
			}
		} catch (SQLException e) {
			System.out.println("Error: failed to extract data from ResultSet");
			throw new RuntimeException(e);
		}

		return ids;
	}
	
	private int findNewSession() {
		String sqlQuery = "SELECT sid FROM sessions WHERE username = '" + CurrentUser.getInstance().getUsername() + "' ORDER BY time DESC LIMIT 1;";
		
		ResultSet result = DBConnection.getInstance().query(sqlQuery);
		
		try {
			if (result != null) {
				return result.getInt("sid");
			} else { 
				throw new RuntimeException("Error: Session was not found");
			}
		} catch (SQLException e) {
			System.out.println("Error: failed to extract data from ResultSet");
			throw new RuntimeException(e);
		} finally {
			DBConnection.closeResultSet(result);
		}
	}

	@Override
	public String getDescription() {
		return "Start New Session";
	}

}
