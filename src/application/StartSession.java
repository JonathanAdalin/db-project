package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import model.CurrentUser;
import model.DBConnection;
import model.QueryResult;

public class StartSession implements MenuChoice {

	private final static Logger logger = Logger.getLogger(StartSession.class);

	@Override
	public void execute() {
		int questionCount;
		while (true) {
			System.out.print("Desired number of questions: ");
			
			try {
				questionCount = UserInput.getInstance().getInt();
				
				if (questionCount <= 0 || questionCount > 9000) {
					throw new NumberFormatException();
				}
				
				break;
			} catch (NumberFormatException nfe) {
				System.out.println("Error: input must be greater than zero and cannot be over 9000");
			}
		}
		
		DBConnection.getInstance().executeCreateSessionStoredProcedure(questionCount);
		int sessionId = findNewSession();
		List<Integer> questionIds = getQuestionIds(sessionId);
		
		for (int qid : questionIds) {
			List<String> contents = getChoicesContents(qid);
			List<Integer> cids = getChoicesIds(qid);
			logger.debug(contents.toString());
			
			int counter = 0;
			List<MenuChoice> choices = new LinkedList<MenuChoice>();
			choices.add(new CorrectAnswer(contents.get(counter), cids.get(counter), sessionId));
			
			for (counter++; counter < contents.size(); counter++) {
				choices.add(new IncorrectAnswer(contents.get(counter), cids.get(counter), sessionId)); 
			}
		
			Collections.shuffle(choices);
			
			System.out.println(getQuestionContent(qid));
			
			MenuChoice[] c = new MenuChoice[choices.size()];
			for (int i = 0; i < choices.size(); i++) {
				c[i] = choices.get(i);
			}
			
			new Menu(c, false).start();
		}
	}
	
	private List<String> getChoicesContents(int questionId) {
		List<String> ids = new LinkedList<String>();

		String sqlQuery = "SELECT content FROM choices WHERE qid = " + questionId + " ORDER BY is_correct DESC;"; 
		QueryResult queryResult = DBConnection.getInstance().query(sqlQuery);
		ResultSet result = queryResult.getResult();
				
		try {
			while (result.next()) {
				ids.add(result.getString("content"));
			}
		} catch (SQLException e) {
			System.out.println("Error: failed to extract data from ResultSet");
			throw new RuntimeException(e);
		} finally {
			DBConnection.closeQueryResult(queryResult);
		}

		return ids;
	}
	
	private List<Integer> getChoicesIds(int questionId) {
		List<Integer> ids = new LinkedList<Integer>();

		String sqlQuery = "SELECT cid FROM choices WHERE qid = " + questionId + " ORDER BY is_correct DESC;"; 
		QueryResult queryResult = DBConnection.getInstance().query(sqlQuery);
		ResultSet result = queryResult.getResult();
				
		try {
			while (result.next()) {
				ids.add(result.getInt("cid"));
			}
		} catch (SQLException e) {
			System.out.println("Error: failed to extract data from ResultSet");
			throw new RuntimeException(e);
		} finally {
			DBConnection.closeQueryResult(queryResult);
		}

		return ids;
	}
	
	private List<Integer> getQuestionIds(int sessionId) {
		List<Integer> ids = new LinkedList<Integer>();
		
		String sqlQuery = "SELECT qid FROM containments WHERE sid = " + sessionId + ";"; 
		QueryResult queryResult = DBConnection.getInstance().query(sqlQuery);
		ResultSet result = queryResult.getResult();
		
		try {
			while (result.next()) {
				ids.add(result.getInt("qid"));
			}
		} catch (SQLException e) {
			System.out.println("Error: failed to extract data from ResultSet");
			throw new RuntimeException(e);
		} finally {
			DBConnection.closeQueryResult(queryResult);
		}

		return ids;
	}
	
	private String getQuestionContent(int questionId) {		
		String sqlQuery = "SELECT content FROM questions WHERE qid = " + questionId + ";"; 
		QueryResult queryResult = DBConnection.getInstance().query(sqlQuery);
		ResultSet result = queryResult.getResult();
		
		try {
			result.next();
			return result.getString("content");
		} catch (SQLException e) {
			System.out.println("Error: failed to extract data from ResultSet");
			throw new RuntimeException(e);
		} finally {
			DBConnection.closeQueryResult(queryResult);
		}
	}
	
	private int findNewSession() {
		String sqlQuery = "SELECT sid FROM sessions WHERE username = '" + CurrentUser.getInstance().getUsername() + "' ORDER BY time DESC, sid DESC LIMIT 1;";
		
		QueryResult queryResult = DBConnection.getInstance().query(sqlQuery);
		ResultSet result = queryResult.getResult();
		
		try {
			if (result != null) {
				result.next();
				return result.getInt("sid");
			} else { 
				throw new RuntimeException("Error: Session was not found");
			}
		} catch (SQLException e) {
			System.out.println("Error: failed to extract data from ResultSet");
			throw new RuntimeException(e);
		} finally {
			DBConnection.closeQueryResult(queryResult);
		}
	}

	@Override
	public String getDescription() {
		return "Start New Session";
	}

}
