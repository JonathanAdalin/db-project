package model;

import java.sql.ResultSet;
import java.sql.Statement;

public class QueryResult {
	private ResultSet rs;
	private Statement stmt;
	
	public QueryResult(ResultSet rs, Statement stmt) {
		this.rs = rs;
		this.stmt = stmt;
	}
	
	public ResultSet getResult() {
		return this.rs;
	}
	
	public Statement getStatement() {
		return this.stmt;
	}
}
