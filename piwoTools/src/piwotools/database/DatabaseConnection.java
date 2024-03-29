package piwotools.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import piwotools.log.Log;

public final class DatabaseConnection {
	
	public final static int LOGINTIMEOUT = 5;  //in seconds
	public final static String URL = "jdbc:postgresql://localhost:5432/";

	private static Connection connection = null;
	
	public static void setup(final String connectionURL) throws Exception {
		Log.debug("DatabaseConnection: Connecting to " + safeConnectionURL(connectionURL));
		Class.forName("org.postgresql.Driver");
		DriverManager.setLoginTimeout(LOGINTIMEOUT);
		connection = DriverManager.getConnection(connectionURL);
		Log.info("DatabaseConnection: New database connection established: " + safeConnectionURL(connectionURL));
	}
	
	public static void setup() throws Exception {
		setup(URL);
	}
	
	public static String getConnectionURL() throws FileNotFoundException, IOException {
		Properties config = new Properties();
		config.load(new FileInputStream("dbconfig.txt"));
		return config.getProperty("URL");
	}

	public static Connection getConnection() throws Exception {
		if (connection == null) {
			setup();
		}
		return connection;
	}
	
	public static void close() throws Exception {
		if(connection != null && !connection.isClosed()) {
			connection.close();
			Log.info("DatabaseConnection: Database connection closed!");
		}
	}
	
	protected static String safeConnectionURL(String url) {
		int index = url.indexOf("?");
		if (index < 0) {
			return url;
		}
		return url.substring(0, index);
	}
	
}
