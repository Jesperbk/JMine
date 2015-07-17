package dk.jbk.JMine.controller;

import java.sql.*;

public class DatabaseManager {
	private static final String DB_ADDRESS = "jdbc:sqlite:jmine.db";

	public DatabaseManager() {
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

		String reportResultSql = String.format("INSERT INTO RESULT VALUES (%d, %d, %d, '%s')",
				timestamp, (isSuccess) ? 1 : 0, timeSpent, difficulty);

		try (Connection connection = DriverManager.getConnection(DB_ADDRESS)) {

			executeSql(connection, reportResultSql);

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private int getRecordForDifficulty(Connection connection, String difficulty) throws SQLException {

		String getRecordSql = String.format("SELECT MIN(TIME_SPENT) FROM RESULT WHERE DIFFICULTY = '%s'", difficulty);

		ResultSet resultSet = executeSql(connection, getRecordSql);

		int recordTime = resultSet.getInt(1);

		return recordTime;
	}

	private long getTimestampOfRecord(Connection connection, String difficulty) throws SQLException {

		String getRecordSql = String.format("SELECT TIMESTAMP, MIN(TIME_SPENT) FROM RESULT WHERE DIFFICULTY = '%s'", difficulty);

		ResultSet resultSet = executeSql(connection, getRecordSql);

		long recordTimestamp = resultSet.getLong(1);

		return recordTimestamp;
	}

	private int getGamesPlayed(Connection connection) throws SQLException {
		String getGamesPlayedSql = "SELECT COUNT(*) FROM RESULT";

		ResultSet resultSet = executeSql(connection, getGamesPlayedSql);

		int result = resultSet.getInt(1);

		return result;
	}

	private int getGamesWon(Connection connection) throws SQLException {
		String getGamesPlayedSql = "SELECT COUNT(*) FROM RESULT WHERE IS_SUCCESS = 1";

		ResultSet resultSet = executeSql(connection, getGamesPlayedSql);

		int result = resultSet.getInt(1);

		return result;
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

	private ResultSet executeSql(Connection connection, String statementString) {
		ResultSet resultSet = null;

		try (Statement statement = connection.createStatement()) {

			statement.execute(statementString);
			resultSet = statement.getResultSet();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		}

		return resultSet;
	}
}
