package model;

import java.sql.*;

import org.apache.log4j.Logger;

public class DBConnection {

	private final static Logger logger = Logger.getLogger(DBConnection.class);
	
	private Connection conn;
	private static DBConnection instance;
	
	private DBConnection() {
	}
	
	public static DBConnection getInstance() {
		if (instance == null) {
			instance = new DBConnection();
		}
		
		return instance;
	}
	
	public QueryResult query(String sqlString) {
		logger.debug("query: " + sqlString);
		
		connect();
		
		if (conn != null) {
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sqlString);
				return new QueryResult(rs, stmt);
			} catch (SQLException e) {
				System.out.println("Error: Failed to query the Database");
				throw new RuntimeException(e);
			} finally {
				close();
			}	
		} else {
			throw new RuntimeException("Error, Connection conn is null");
		}		
	}
	
	public void delete(String sqlString) {
		update(sqlString);
	}
	
	public void insert(String sqlString) {
		update(sqlString);
	}
	
	public void update(String sqlString) {
		logger.debug("update/insert/delete: " + sqlString);
		
		connect();
		
		if (conn != null) {
			try {
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(sqlString);
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Error: Failed to update the Database");
				throw new RuntimeException(e);
			} finally {
				close();
			}	
		} else {
			throw new RuntimeException("Error, Connection conn is null");
		}
	}
	
	public void executeCreateSessionStoredProcedure(int questionCount) {
		connect();
		
		if (conn != null) {
			ResultSet rs = null;
			CallableStatement cs = null;
			try {
				cs = conn.prepareCall("{call create_session(?,?)}"); 
				cs.setString(1, CurrentUser.getInstance().getUsername()); 
				cs.setInt(2, questionCount);
				rs = cs.executeQuery();
			} catch (SQLException e) {
				System.out.println("Error: Failed to update the Database");
				throw new RuntimeException(e);
			} finally {
				if(cs != null) {
					try {
						cs.close();
					} catch (SQLException e) {
						//oh well...
					}
				}
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						//oh well...
					}
				}
				
				close();
			}	
		} else {
			throw new RuntimeException("Error, Connection conn is null");
		}
	}
	
	private void connect() {
		try {
			
			DriverManager.registerDriver(new org.postgresql.Driver());

			String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
			
			conn = DriverManager.getConnection(url, "cs421g10", "n@n0_suX");
			
		} catch (SQLException e) {
			System.out.println("Error: Failed to connect to the Database.");
			throw new RuntimeException(e);
		}
	}
	
	private void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			//oh well
		}
	}
	
	public static void closeQueryResult(QueryResult result) {
		try {
			result.getStatement().close();
		} catch (SQLException e) {
			//oh well...
		}
	}
}
