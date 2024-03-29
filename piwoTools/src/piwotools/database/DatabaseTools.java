package piwotools.database;

import java.sql.Statement;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import piwotools.log.Log;

/**
 * Returns an ArrayList of Rows that can be easily accessed iteratively.
 */
public class DatabaseTools {
	
	public static ArrayList<Row> getQueryResult(final String query, final Object... parameters) throws Exception {
		Connection con = DatabaseConnection.getConnection();
		PreparedStatement prepStatement = con.prepareStatement(query);
		
		for(int i = 0; i < parameters.length; i++) {
			prepStatement.setObject(i + 1, parameters[i]);
		}

		ResultSet rs = prepStatement.executeQuery();
		ArrayList<Row> result = new ArrayList<Row>();

		while(rs.next()) {
			ResultSetMetaData meta = rs.getMetaData();
			Row row = new Row();
			for(int i = 1; i <= meta.getColumnCount(); i++) {
				row.setValue(meta.getColumnName(i), rs.getObject(i));
			}
			result.add(row);
		}

		Log.debug("Getting query result: " + prepStatement.toString());

		return result;
	}
	
	public static ArrayList<Row> getQueryResult(final String query) throws Exception {
		return getQueryResult(query, new Object[0]);
	}
	
	public static Row getOneRowQueryResult(final String query, final Object... parameters) throws Exception {
		ArrayList<Row> data = getQueryResult(query, parameters);
		return (data.size() == 0) ? null : data.get(0);
	}

	public static Row getOneRowQueryResult(final String query) throws Exception {
		return getOneRowQueryResult(query, new Object[0]);
	}

	
	public static Object executeUpdate(final String query, final Object... parameters) throws Exception {
		Connection con = DatabaseConnection.getConnection();
		PreparedStatement prepStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		
		for(int i = 0; i < parameters.length; i++) {
			prepStatement.setObject(i + 1, parameters[i]);
		}
		
		int updatedLines = prepStatement.executeUpdate();
		
		Log.debug("Executing update (" + updatedLines + " rows): " + prepStatement.toString());

		ResultSet rs = prepStatement.getGeneratedKeys();
		if (rs.next()) {
		  return rs.getObject(1);
		}
		
		return null;
	}
	
	public static Object executeUpdate(final String query) throws Exception {
		return executeUpdate(query, new Object[0]);
	}
	
	public static Long getRandomID() {
		SecureRandom sr = new SecureRandom();
		return sr.nextLong();
	}
}
