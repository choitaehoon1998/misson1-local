package com.zerobase.misson1.service;

import com.zerobase.misson1.domain.LocationHistory;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationHistoryService {

	public static void newLocationHistory(double LAT, double LNT) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = new Date();
		Connection con = null;
		PreparedStatement stmt = null;
		String sql = "INSERT INTO LOCATION_HISTORY(LAT,LNT,CREATED_AT,IS_USE)" +
				" VALUES(?,?,?,?)";
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mariadb://localhost/misson1","cth","cth12345!!");
			stmt = con.prepareStatement(sql);

			stmt.setDouble(1, LAT);
			stmt.setDouble(2, LNT);
			stmt.setString(3, dateFormat.format(date));
			stmt.setString(4, "Y");
			stmt.executeUpdate();
		} catch (ClassNotFoundException | SQLException ignored) {
		} finally {
			if (stmt != null) try {
				stmt.close();
			} catch (SQLException ignored) {
			}
			if (con != null) try {
				con.close();
			} catch (SQLException ignored) {
			}
		}
	}

	public static void deleteLocationHistory(int seq) {
		Connection con = null;
		PreparedStatement stmt = null;
		String sql = "UPDATE LOCATION_HISTORY SET IS_USE='N'" +
				" WHERE SEQ = ?";
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mariadb://localhost/misson1","cth","cth12345!!");
			stmt = con.prepareStatement(sql);

			stmt.setInt(1, seq);
			stmt.executeUpdate();

		} catch (ClassNotFoundException | SQLException ignored) {
		} finally {
			if (stmt != null) try {
				stmt.close();
			} catch (SQLException ignored) {
			}
			if (con != null) try {
				con.close();
			} catch (SQLException ignored) {
			}
		}
	}

	public static List<LocationHistory> findUseHistory() {
		List<LocationHistory> locationHistoryList = new ArrayList<>();

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mariadb://localhost/misson1","cth","cth12345!!");
			stmt = con.createStatement();

			rs = stmt.executeQuery("SELECT * FROM LOCATION_HISTORY WHERE IS_USE = 'Y'");
			while (rs.next()) {

				int seq = rs.getInt(1);
				double LAT = rs.getDouble(2);
				double LNT = rs.getDouble(3);
				String CREATED_AT = rs.getString(4);
				String IS_USE = rs.getString(5);

				LocationHistory history = new LocationHistory();
				history.SEQ = seq;
				history.LAT = LAT;
				history.LNT = LNT;
				history.CREATED_AT = CREATED_AT;
				history.IS_USE = IS_USE;

				locationHistoryList.add(history);
			}
		} catch (ClassNotFoundException | SQLException ignored) {

		} finally {
			if (rs != null) try {
				rs.close();
			} catch (SQLException ignored) {
			}
			if (stmt != null) try {
				stmt.close();
			} catch (SQLException ignored) {
			}
			if (con != null) try {
				con.close();
			} catch (SQLException ignored) {
			}
		}


		return locationHistoryList;
	}
}
