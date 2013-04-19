package com.gomotion;

import java.sql.*;
import java.util.LinkedList;
import java.util.ListIterator;

public class OnlineDatabase {
	static private final String url = "jdbc:mysql://hexdex.net:3306/hexdexne_csc2015";
	static private final String user = "hexdexne_csc2015";
	static private final String password = "g0m0t10n";
	static private Connection connection;

	static public Connection getConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		if (connection != null)
			if (!connection.isClosed())
				return connection;

		return connection = DriverManager.getConnection(url, user, password);
	}

	static public boolean add(BodyWeightExercise exercise) {
		try {
			// Try to get a connection
			getConnection();
			Statement s = connection.createStatement();
			s.executeUpdate("INSERT INTO bodyweight VALUES (" + "null,"
					+ exercise.getTimeStamp() + ","
					+ exercise.getUserID() + ","
					+ exercise.getSets() + ","
					+ exercise.getReps() + ","
					+ (exercise.getName() == null ? "null," : "\"" + exercise.getName().replace("\"", "\\\"") + "\",")
					+ exercise.getType().ordinal() + ");");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	static public boolean add(CardioExercise exercise) {
		try {
			// Try to get a connection
			System.out.println("Connecting...");
			Connection connection = getConnection();
			System.out.println("Connected!");
			Statement s = connection.createStatement();
			s.execute("INSERT INTO cardio VALUES (" + "null,"
					+ exercise.getTimeStamp() + "," + exercise.getUserID()
					+ "," + exercise.getTimeLength() + ","
					+ exercise.getDistance() + ","
					+ "\"" + exercise.getMapURL().replace("\"", "\\\"") + "\","
					+ exercise.getType().ordinal() + ");");
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	static public LinkedList<BodyWeightExercise> getBodyWeightExercises(LinkedList<String> friends, int num) {
		try {
			Connection connection = getConnection();
			Statement s = connection.createStatement();
			
			String whereClause = "";
			if(friends.size() == 0)
				return null;
			else
			{
				ListIterator<String> i = friends.listIterator();
				while(i.hasNext())
					whereClause += "b.user = \"" + i.next() + "\" OR ";
				whereClause = whereClause.substring(0, whereClause.length() - 4);
			}
			

			String query = "SELECT * FROM bodyweight b WHERE "
					+ whereClause + " LIMIT " + num + ";";

			ResultSet result = s
					.executeQuery(query);

			LinkedList<BodyWeightExercise> out = new LinkedList<BodyWeightExercise>();
			while(result.next())
				out.add(new BodyWeightExercise(result));

			return out;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}


	static public LinkedList<CardioExercise> getCardioExercises(LinkedList<String> friends, int num) {
		try {
			Connection connection = getConnection();
			Statement s = connection.createStatement();
			
			String whereClause = "";
			if(friends.size() == 0)
				return null;
			else
			{
				ListIterator<String> i = friends.listIterator();
				while(i.hasNext())
					whereClause += "c.user = \"" + i.next() + "\" OR ";
				whereClause = whereClause.substring(0, whereClause.length() - 4);
			}
			

			ResultSet result = s
					.executeQuery("SELECT * FROM cardio c WHERE "
							+ whereClause + " LIMIT " + num + ";");

			LinkedList<CardioExercise> out = new LinkedList<CardioExercise>();
			while(result.next())
				out.add(new CardioExercise(result));

			return out;
		} catch (SQLException e) {
			return null;
		}
	}

}