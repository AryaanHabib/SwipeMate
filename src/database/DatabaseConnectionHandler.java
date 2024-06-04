package database;

import model.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnectionHandler {
	private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
	private static final String JDBC_DRIVER = "oracle.jdbc.driver.OracleDriver";

	private Connection connection = null;

	public boolean login(String username, String password) {
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(ORACLE_URL, username, password);
			databaseSetup();

			System.out.println("Login and setup done.");
			return true;
		} catch (ClassNotFoundException e) {
			System.out.println("Oracle JDBC Driver not found.");
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			System.out.println("Connection failed.");
			e.printStackTrace();
			return false;
		}
	}

	public void closeConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void databaseSetup() {
		try (InputStream is = getClass().getResourceAsStream("/scripts/script.sql");
			 BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			 Statement stmt = connection.createStatement()) {

			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				if (line.trim().endsWith(";")) {
					String statementToExecute = sb.toString().replaceAll(";$", "").trim();
					if (!statementToExecute.isEmpty()) {
						try {
							stmt.execute(statementToExecute);
						} catch (SQLException e) {
							if (e.getErrorCode() == 942) {
								System.out.println("Table does not exist, no need to drop");
							} else {
								throw e;
							}
						}
					}
					sb.setLength(0);
				}
			}
		} catch (SQLException e) {
			System.out.println("[EXCEPTION] " + e.getMessage());
		} catch (IOException e) {
			System.out.println("[EXCEPTION] " + e.getMessage());
		} catch (NullPointerException e) {
			System.out.println("[EXCEPTION] Unable to load script file. Ensure the file path is correct.");
		}
	}

	public void printUsersTable() {
		String query = "SELECT * FROM Users";
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();

			for (int i = 1; i <= columnsNumber; i++) {
				System.out.print(String.format("%-30s", rsmd.getColumnName(i)));
			}
			System.out.println();

			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					String columnValue = rs.getString(i);
					System.out.print(String.format("%-30s", columnValue));
				}
				System.out.println();
			}
		} catch (SQLException e) {
			System.out.println("[EXCEPTION] " + e.getMessage());
		}
	}


	public void insertUser(userModel user) {
		String insertSql = "INSERT INTO Users (UserID, Name, Gender, Bio, Age) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
			ps.setString(1, user.getUserID());
			ps.setString(2, user.getName());
			ps.setString(3, user.getGender());
			ps.setString(4, user.getBio());
			ps.setInt(5, user.getAge());
			ps.executeUpdate();
			appendInsertStatementToFile(user);
			printUsersTable();
		} catch (SQLException e) {
			System.out.println("[EXCEPTION] " + e.getMessage());
		}
	}

	private void appendInsertStatementToFile(userModel model) {
		String sqlStatement = String.format("INSERT INTO Users VALUES ('%s', '%s', '%s', '%s', %d);\n",
				model.getUserID(), model.getName(), model.getGender(), model.getBio(), model.getAge());

		try(FileWriter fw = new FileWriter("src/scripts/script.sql", true);
			PrintWriter out = new PrintWriter(fw)) {
			out.println(sqlStatement);
		} catch (IOException e) {
			System.out.println("EXCEPTION + " + e.getMessage());
		}
	}

	public boolean checkUserId(String userIdInput) {
		String checkSql = "SELECT UserID FROM Users WHERE UserID = ?";
		boolean userIdExists = false;
		try (PreparedStatement ps = connection.prepareStatement(checkSql)) {
			ps.setString(1, userIdInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					userIdExists = true;
				}
			}
		} catch (SQLException e) {
			System.out.println("[EXCEPTION] " + e.getMessage());
		}
		return userIdExists;
	}

	public List<String> getResIds() {
		List<String> resIds = new ArrayList<>();
		String query = "SELECT ResID FROM Res";

		try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resIds.add(rs.getString("ResID"));
			}
		} catch (SQLException e) {
			System.out.println("[EXCEPTION] " + e.getMessage());
		}
		return resIds;
	}

	public List<String> getResName() {
		List<String> resName = new ArrayList<>();
		String query = "SELECT BuildingName FROM Building";

		try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				resName.add(rs.getString("BuildingName"));
			}
		} catch (SQLException e) {
			System.out.println("[EXCEPTION] " + e.getMessage());
		}
		return resName;
	}

	public List<String> getResIdsName() {
		List<String> id = getResIds();
		List<String> name = getResName();
		List<String> idName = new ArrayList<>();
		for (int i = 0; i < id.size(); i ++) {
			idName.add(id.get(i) + " (" + name.get(i) + ")");
		}
		return idName;
	}

	public void insertLister(listerModel lister) {
		String insertSql = "INSERT INTO Lister(ListerID, ResID, ListingType) VALUES (?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
			ps.setString(1, lister.getListerID());
			ps.setString(2, lister.getResID());
			ps.setString(3, lister.getListingType());
			ps.executeUpdate();
			appendLister(lister);
			System.out.println(getResIds());
		} catch (SQLException e) {
			System.out.println("[EXCEPTION] " + e.getMessage());
		}
	}

	private void appendLister(listerModel lister) {
		String sqlStatement = String.format("INSERT INTO Lister VALUES ('%s', '%s', '%s');\n",
				lister.getListerID(), lister.getResID(), lister.getListingType());
		try(FileWriter fw = new FileWriter("src/scripts/script.sql", true);
			PrintWriter out = new PrintWriter(fw)) {
			out.println(sqlStatement);
		} catch (IOException e) {
			System.out.println("EXCEPTION + " + e.getMessage());
		}
	}

	public void insertSeeker(seekerModel seeker) {
		String insertSql = "INSERT INTO Seeker (SeekerID, SeekingType) VALUES (?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
			ps.setString(1, seeker.getSeekerID());
			ps.setString(2, seeker.getSeekingType());
			ps.executeUpdate();
			appendSeeker(seeker);
			System.out.println(getResIds());
		} catch (SQLException e) {
			System.out.println("[EXCEPTION] " + e.getMessage());
		}
	}

	private void appendSeeker(seekerModel seeker) {
		String sqlStatement = String.format("INSERT INTO Seeker VALUES ('%s', '%s');\n",
				seeker.getSeekerID(), seeker.getSeekingType());
		try(FileWriter fw = new FileWriter("src/scripts/script.sql", true);
			PrintWriter out = new PrintWriter(fw)) {
			out.println(sqlStatement);
		} catch (IOException e) {
			System.out.println("EXCEPTION + " + e.getMessage());
		}
	}

	public void insertPreference(preferenceModel pref) {
		String insertSql = "INSERT INTO Preferences (PreferencesID, PreferredGender, PreferredAgeRange, PreferredLifestyle, PreferredLocation) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
			ps.setString(1, pref.getPreferencesID());
			ps.setString(2, pref.getGender());
			ps.setString(3, pref.getAgeRange());
			ps.setString(4, pref.getLifeStyle());
			ps.setString(5, pref.getLocation());
			ps.executeUpdate();
			appendPreference(pref);
			System.out.println(getResIds());
		} catch (SQLException e) {
			System.out.println("[EXCEPTION] " + e.getMessage());
		}
	}

	private void appendPreference(preferenceModel pref) {
		String sqlStatement = String.format("INSERT INTO Preferences VALUES ('%s', '%s', '%s', '%s', '%s');\n",
				pref.getPreferencesID(), pref.getGender(), pref.getAgeRange(), pref.getLifeStyle(), pref.getLocation());
		try(FileWriter fw = new FileWriter("src/scripts/script.sql", true);
			PrintWriter out = new PrintWriter(fw)) {
			out.println(sqlStatement);
		} catch (IOException e) {
			System.out.println("EXCEPTION + " + e.getMessage());
		}
	}

	public void insertRequirement(requirementModel req) {
		String insertSql = "INSERT INTO Requirements (RequirementID, RequiredGender, RequiredAgeRange, RequiredLifestyle, RequiredLocation) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
			ps.setString(1, req.getRequirementID());
			ps.setString(2, req.getGender());
			ps.setString(3, req.getAgeRange());
			ps.setString(4, req.getLifeStyle());
			ps.setString(5, req.getLocation());
			ps.executeUpdate();
			appendRequirement(req);
			System.out.println(getResIds());
		} catch (SQLException e) {
			System.out.println("[EXCEPTION] " + e.getMessage());
		}
	}

	private void appendRequirement(requirementModel req) {
		String sqlStatement = String.format("INSERT INTO Requirements VALUES ('%s', '%s', '%s', '%s', '%s');\n",
				req.getRequirementID(), req.getGender(), req.getAgeRange(), req.getLifeStyle(), req.getLocation());
		try(FileWriter fw = new FileWriter("src/scripts/script.sql", true);
			PrintWriter out = new PrintWriter(fw)) {
			out.println(sqlStatement);
		} catch (IOException e) {
			System.out.println("EXCEPTION + " + e.getMessage());
		}
	}

	public void insertSocial(socialModel social) {
		String insertSql = "INSERT INTO SocialPageHas (EmailID, UserID, PhoneNumber, InstagramUsername) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
			ps.setString(1, social.getEmailID());
			ps.setString(2, social.getUserID());
			ps.setString(3, social.getPhone());
			ps.setString(4, social.getInstaID());
			ps.executeUpdate();
			appendSocial(social);
			System.out.println(getResIds());
		} catch (SQLException e) {
			System.out.println("[EXCEPTION] " + e.getMessage());
		}
	}

	private void appendSocial(socialModel social) {
		String sqlStatement = String.format("INSERT INTO SocialPageHas VALUES ('%s', '%s', '%s', '%s');\n",
				social.getEmailID(), social.getUserID(), social.getPhone(), social.getInstaID());
		try(FileWriter fw = new FileWriter("src/scripts/script.sql", true);
			PrintWriter out = new PrintWriter(fw)) {
			out.println(sqlStatement);
		} catch (IOException e) {
			System.out.println("EXCEPTION + " + e.getMessage());
		}
	}
	public void updateSocialPageHas(String userID, String newEmailID, String newPhoneNumber, String newInstagramUsername) {
		ArrayList<String> updates = new ArrayList<>();
		if (newEmailID != null && !newEmailID.trim().isEmpty()) {

			updates.add("EmailID = '" + newEmailID + "'");
			System.out.println("1ST YES");
		}
		if (newPhoneNumber != null && !newPhoneNumber.trim().isEmpty()) {
			updates.add("PhoneNumber = '" + newPhoneNumber + "'");
			System.out.println("2ND YES");

		}
		if (newInstagramUsername != null && !newInstagramUsername.trim().isEmpty()) {
			updates.add("InstagramUsername = '" + newInstagramUsername + "'");
			System.out.println("3RD YES");

		}

		if (!updates.isEmpty()) {
			String updateSql = "UPDATE SocialPageHas SET " + String.join(", ", updates) + " WHERE UserID = ?";
			System.out.println(updateSql);
			try (PreparedStatement ps = connection.prepareStatement(updateSql)) {
				ps.setString(1, userID);
				ps.executeUpdate();
				appendUpdateSocialPageHas(userID, newEmailID, newPhoneNumber, newInstagramUsername);
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Update failed due to a SQL error.");
			}
		} else {
			System.out.println("No updates to perform.");
		}
	}
	private void appendUpdateSocialPageHas(String userID, String newEmailID, String newPhoneNumber, String newInstagramUsername) {
		ArrayList<String> updates = new ArrayList<>();

		if (newEmailID != null && !newEmailID.trim().isEmpty()) {
			updates.add("EmailID = '" + newEmailID + "'");
		}
		if (newPhoneNumber != null && !newPhoneNumber.trim().isEmpty()) {
			updates.add("PhoneNumber = '" + newPhoneNumber + "'");
		}
		if (newInstagramUsername != null && !newInstagramUsername.trim().isEmpty()) {
			updates.add("InstagramUsername = '" + newInstagramUsername + "'");
		}

		if (!updates.isEmpty()) {
			String updateSql = "UPDATE SocialPageHas SET " + String.join(", ", updates) + " WHERE UserID = '" + userID + "';\n";

			try(FileWriter fw = new FileWriter("src/scripts/script.sql", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
				out.println(updateSql);
			} catch (IOException e) {
				System.out.println("EXCEPTION: " + e.getMessage());
			}
		} else {
			System.out.println("No updates to append for UserID: " + userID);
		}
	}

	public void deleteUser(String userID) {
		String deleteSql = "DELETE FROM Users WHERE UserID = ?";
		try (PreparedStatement ps = connection.prepareStatement(deleteSql)) {
			ps.setString(1, userID);
			ps.executeUpdate();
			appendDeleteUser(userID);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void appendDeleteUser(String userID) {
		String sqlStatement = String.format("DELETE FROM Users WHERE UserID = '%s';\n", userID);
		try (FileWriter fw = new FileWriter("src/scripts/script.sql", true);
			 BufferedWriter bw = new BufferedWriter(fw);
			 PrintWriter out = new PrintWriter(bw)) {

			out.println(sqlStatement);
		} catch (IOException e) {
			System.out.println("EXCEPTION: " + e.getMessage());
		}
	}

	public List<List<Object>> queryResidences(String whereClause) {
		List<List<Object>> resultList = new ArrayList<>();
		String query = whereClause;
		System.out.println(query);

		try (PreparedStatement ps = connection.prepareStatement(query);
			 ResultSet rs = ps.executeQuery()) {

			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (rs.next()) {
				List<Object> row = new ArrayList<>();
				for (int i = 1; i <= columnCount; i++) {
					row.add(rs.getObject(i));
				}
				resultList.add(row);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Query failed due to a SQL error.");
		}

		return resultList;
	}

	public List<List<Object>> join() {
		List<List<Object>> resultList = new ArrayList<>();
		String joinQuery = "SELECT * FROM Users U, Preferences P, Requirements R WHERE U.UserID = P.PreferencesID AND U.UserID = R.RequirementID";

		try (PreparedStatement ps = connection.prepareStatement(joinQuery);
			 ResultSet rs = ps.executeQuery()) {

			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (rs.next()) {
				List<Object> row = new ArrayList<>();
				for (int i = 1; i <= columnCount; i++) {
					row.add(rs.getObject(i));
				}
				resultList.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Join query failed due to a SQL error.");
		}

		return resultList;
	}

	public List<List<Object>> projectUser(String query) {
		List<List<Object>> resultList = new ArrayList<>();
		System.out.println(query);
		try (Statement stmt = connection.createStatement();
			 ResultSet rs = stmt.executeQuery(query)) {

			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (rs.next()) {
				List<Object> row = new ArrayList<>();
				for (int i = 1; i <= columnCount; i++) {
					row.add(rs.getObject(i));
				}
				resultList.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	public List<List<Object>> aggGroupBy() {
		List<List<Object>> resultList = new ArrayList<>();
		String aggQuery = "SELECT Type, AVG(Rent) as AverageRent FROM Res GROUP BY Type";

		try (PreparedStatement ps = connection.prepareStatement(aggQuery);
			 ResultSet rs = ps.executeQuery()) {

			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();

			while (rs.next()) {
				List<Object> row = new ArrayList<>();
				for (int i = 1; i <= columnCount; i++) {
					row.add(rs.getObject(i));
				}
				resultList.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Aggregation with GROUP BY query failed due to a SQL error.");
		}

		return resultList;
	}

	public List<List<Object>> aggHavingWithJoin() {
		List<List<Object>> resultList = new ArrayList<>();
		int minAvgRent = 1000;

		String aggQuery = "SELECT B.BuildingName, AVG(R.Rent) as AvgRent FROM Floors F, Building B,Res R WHERE F.BUILDINGNAME = B.BuildingName and B.Address = R.Address GROUP BY B.BuildingName HAVING AVG(R.Rent) > ?";

		try (PreparedStatement ps = connection.prepareStatement(aggQuery)) {
			ps.setInt(1, minAvgRent);

			try (ResultSet rs = ps.executeQuery()) {
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();

				while (rs.next()) {
					List<Object> row = new ArrayList<>();
					for (int i = 1; i <= columnCount; i++) {
						row.add(rs.getObject(i));
					}
					resultList.add(row);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Aggregation with HAVING clause query failed due to a SQL error.");
		}

		return resultList;
	}
	public List<List<Object>> nestedAggGroupBy() {
		List<List<Object>> resultList = new ArrayList<>();
		int minAvgRent = 1200;

		String nestedAggQuery =
				"SELECT F.BuildingName, AVG(R.NumberOfRooms) AS AvgNumberOfRooms FROM Floors F, Building B, Res R WHERE F.BuildingName = B.BuildingName AND B.Address = R.Address GROUP BY F.BuildingName HAVING AVG(R.Rent) > (SELECT AVG(Rent) FROM Res WHERE Address IN (SELECT Address FROM Building) AND Rent > ?)";

		try (PreparedStatement ps = connection.prepareStatement(nestedAggQuery)) {
			ps.setInt(1, minAvgRent);

			try (ResultSet rs = ps.executeQuery()) {
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();

				while (rs.next()) {
					List<Object> row = new ArrayList<>();
					for (int i = 1; i <= columnCount; i++) {
						row.add(rs.getObject(i));
					}
					resultList.add(row);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Nested aggregation with GROUP BY query failed due to a SQL error.");
		}

		return resultList;
	}
	public List<List<Object>> performDivisionQuery() {
		List<List<Object>> results = new ArrayList<>();
		String divisionQuery =
				"SELECT U.UserID, U.Name " +
						"FROM Users U " +
						"WHERE NOT EXISTS ( " +
						"(SELECT DISTINCT Type FROM Res) " +
						"MINUS " +
						"(SELECT DISTINCT L.ListingType FROM Lister L WHERE L.ListerID = U.UserID)" +
						")";

		try (PreparedStatement ps = connection.prepareStatement(divisionQuery)) {
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				List<Object> row = new ArrayList<>();
				row.add(rs.getString("UserID"));
				row.add(rs.getString("Name"));
				results.add(row);
			}
		} catch (SQLException e) {
			System.err.println("SQL Exception: " + e.getMessage());
		}

		return results;
	}


}
