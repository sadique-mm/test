package com.zwayam.appliespuller.common;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;



public class QueryExecuter {

	public static ArrayList<HashMap<String, Object>> execute(String query) {
		ArrayList<HashMap<String, Object>> list =  new ArrayList<>();
		try {
			ResultSet rs = null;
			java.sql.Connection conn;
			conn = conn = DriverManager.getConnection(AppConstants.URL, AppConstants.USERNAME,
					AppConstants.PASSWORD);
			PreparedStatement statement = null;
			statement = conn.prepareStatement(query);
			rs = statement.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int rowCount = 0;
			int count = rsmd.getColumnCount();
			while (rs.next()) {
				if (rs.isLast()) {
					rowCount = rs.getRow();
				}
			}

			rs.beforeFirst();
			while (rs.next()) {

				HashMap<String, Object> row = new HashMap<String, Object>(count);
				try {

					for (int j = 1; j <= count; j++) {
						row.put(rsmd.getColumnLabel(j), rs.getObject(rsmd.getColumnLabel(j)));
					}
				} catch (Exception e) {
					continue;
				}
				list.add(row);
			}
			
		} catch (Exception e) {
			System.out.println(e);
			return list;
		}
		return list;
	}
}