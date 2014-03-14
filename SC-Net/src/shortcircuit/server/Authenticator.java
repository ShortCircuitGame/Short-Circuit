package shortcircuit.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Authenticator {

    private Connection connection;

    public Authenticator() {
	try {
	    Class.forName("org.sqlite.JDBC");
	    connection = DriverManager.getConnection("jdbc:sqlite:users.db");
	    connection.setAutoCommit(false);
	} catch (Exception e) {
	    e.printStackTrace();
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	}
	System.out.println("Opened database successfully");
    }

    public void create() {
	Statement stmt;
	try {
	    stmt = connection.createStatement();
	    String sql = "CREATE TABLE IF NOT EXISTS COMPANY " + "(USERNAME TEXT PRIMARY KEY NOT NULL, " + " PASSWORD TEXT NOT NULL, " + " WINS INT NOT NULL, "
		    + " LOSSES INT NOT NULL);";
	    stmt.executeUpdate(sql);
	    stmt.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	}
    }

    public boolean addUser(String username, String password) {
	Statement stmt = null;
	try {
	    stmt = connection.createStatement();
	    String sql = "INSERT INTO COMPANY (USERNAME,PASSWORD,WINS,LOSSES) " + "VALUES ('" + username + "', '" + password + "', 0, 0);";
	    stmt.executeUpdate(sql);
	    stmt.close();
	    connection.commit();
	} catch (Exception e) {
	    e.printStackTrace();
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    return false;
	}
	System.out.println("Records created successfully");
	return true;
    }

    public boolean authorize(String username, String password) {
	Statement stmt = null;
	int results = 0;
	try {
	    stmt = connection.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM COMPANY WHERE USERNAME = '" + username + "' AND PASSWORD = '" + password + "' ;");
	    while (rs.next()) {
		results++;
	    }
	    rs.close();
	    stmt.close();
	} catch (Exception e) {
	    e.printStackTrace();
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    return false;
	}
	System.out.println("Operation done successfully");
	if (results == 1) {
	    return true;
	} else {
	    return false;
	}
    }

    public User getUser(String username) {
	Statement stmt = null;
	User user = null;
	try {
	    stmt = connection.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT * FROM COMPANY WHERE USERNAME = " + username + ";");
	    while (rs.next()) {
		int wins = rs.getInt("wins");
		int losses = rs.getInt("losses");
		user = new User(username, wins, losses);
	    }
	    rs.close();
	    stmt.close();
	} catch (Exception e) {
	    e.printStackTrace();
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    return null;
	}
	System.out.println("Operation done successfully");
	return user;
    }

    public boolean updateUser(String username, int wins, int losses) {
	Statement stmt = null;
	try {
	    stmt = connection.createStatement();
	    String sql = "UPDATE COMPANY set WINS=" + wins + " AND LOSSES=" + losses + " where USERNAME=" + username + ";";
	    stmt.executeUpdate(sql);
	    connection.commit();
	    stmt.close();
	} catch (Exception e) {
	    e.printStackTrace();
	    System.err.println(e.getClass().getName() + ": " + e.getMessage());
	    return false;
	}
	System.out.println("Operation done successfully");
	return true;
    }

}
