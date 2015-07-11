package dk.jbk.JMine.controller;

import java.sql.*;

public class GameEndHandler {
	private static final String DB_ADDRESS = "jdbc:sqlite:jmine.db";

	public GameEndHandler() {
		Connection connection = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection(DB_ADDRESS);
			setupTableIfNonExistent(connection);

			connection.close();
		}
		catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			System.exit(0);
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public void reportGameResult(int timestamp, boolean isSuccess, int timeSpent, String difficulty) {

		try (Connection connection = DriverManager.getConnection(DB_ADDRESS);
		Statement statement = connection.createStatement()) {

		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	private void setupTableIfNonExistent(Connection connection) throws SQLException {
		String createResultTableSql = "CREATE TABLE IF NOT EXISTS RESULT " +
				"(TIMESTAMP 	INT 	PRIMARY KEY," +
				" IS_SUCCESS 	INT 	NOT NULL," +
				" TIME_SPENT 	INT 	NOT NULL," +
				" DIFFICULTY 	CHARACTER NOT NULL)";

		Statement statement = connection.createStatement();
		statement.execute(createResultTableSql);
		statement.close();
	}
}
