package com.zerobase.misson1.service;

import com.google.gson.*;
import com.zerobase.misson1.domain.WifiInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WifiService {

	public static int getInfoFromApi() throws IOException {
		int totalCount = 0;
		String targetUrl = "http://openapi.seoul.go.kr:8088/524979496768616836325656415a51/json/TbPublicWifiInfo/1/1";

		OkHttpClient client = new OkHttpClient();
		Request.Builder builder = new Request.Builder().url(targetUrl).get();
		Request request = builder.build();

		Response response = client.newCall(request).execute();
		if (response.isSuccessful()) {
			ResponseBody body = response.body();
			if (body != null) {
				String responseString = body.string();
				JsonElement element = JsonParser.parseString(responseString);
				totalCount = element.getAsJsonObject().get("TbPublicWifiInfo").getAsJsonObject().get("list_total_count").getAsInt();
				saveTotalInfo(totalCount);
			}
		}
		return totalCount;

	}

	public static List<WifiInfo> getWifiAllInfo() {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<WifiInfo> wifiInfoList = new ArrayList<>();
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mariadb://localhost/misson1", "cth", "cth12345!!");
			stmt = con.createStatement();

			rs = stmt.executeQuery("SELECT * FROM WIFI_INFO LIMIT 1");
			while (rs.next()) {

				int seq = rs.getInt(1);
				double LAT = rs.getDouble(2);
				double LNT = rs.getDouble(3);
				String WORK_DTTM = rs.getString(4);
				String X_SWIFI_ADRES1 = rs.getString(5); //rs.getString("email");
				String X_SWIFI_ADRES2 = rs.getString(6); //rs.getString("email");
				String X_SWIFI_CMCWR = rs.getString(7); //rs.getString("email");
				String X_SWIFI_CNSTC_YEAR = rs.getString(8); //rs.getString("email");
				String X_SWIFI_INOUT_DOOR = rs.getString(9); //rs.getString("email");
				String X_SWIFI_INSTL_FLOOR = rs.getString(10); //rs.getString("email");
				String X_SWIFI_INSTL_MBY = rs.getString(11); //rs.getString("email");
				String X_SWIFI_INSTL_TY = rs.getString(12); //rs.getString("email");
				String X_SWIFI_MAIN_NM = rs.getString(13); //rs.getString("email");
				String X_SWIFI_MGR_NO = rs.getString(14); //rs.getString("email");
				String X_SWIFI_REMARS3 = rs.getString(15); //rs.getString("email");
				String X_SWIFI_SVC_SE = rs.getString(16); //rs.getString("email");
				String X_SWIFI_WRDOFC = rs.getString(17); //rs.getString("email");

				WifiInfo wifiInfo = new WifiInfo();
				wifiInfo.SEQ = seq;
				wifiInfo.LAT = LAT;
				wifiInfo.LNT = LNT;
				wifiInfo.WORK_DTTM = WORK_DTTM;
				wifiInfo.X_SWIFI_ADRES1 = X_SWIFI_ADRES1;
				wifiInfo.X_SWIFI_ADRES2 = X_SWIFI_ADRES2;
				wifiInfo.X_SWIFI_CMCWR = X_SWIFI_CMCWR;
				wifiInfo.X_SWIFI_CNSTC_YEAR = X_SWIFI_CNSTC_YEAR;
				wifiInfo.X_SWIFI_INOUT_DOOR = X_SWIFI_INOUT_DOOR;
				wifiInfo.X_SWIFI_INSTL_FLOOR = X_SWIFI_INSTL_FLOOR;
				wifiInfo.X_SWIFI_INSTL_MBY = X_SWIFI_INSTL_MBY;
				wifiInfo.X_SWIFI_INSTL_TY = X_SWIFI_INSTL_TY;
				wifiInfo.X_SWIFI_MAIN_NM = X_SWIFI_MAIN_NM;
				wifiInfo.X_SWIFI_MGR_NO = X_SWIFI_MGR_NO;
				wifiInfo.X_SWIFI_REMARS3 = X_SWIFI_REMARS3;
				wifiInfo.X_SWIFI_SVC_SE = X_SWIFI_SVC_SE;
				wifiInfo.X_SWIFI_WRDOFC = X_SWIFI_WRDOFC;
				wifiInfoList.add(wifiInfo);

			}
		} catch (ClassNotFoundException | SQLException ignored) {
			System.out.println("ignored = " + ignored);

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
		return wifiInfoList;
	}

	public static List<WifiInfo> getNearWifi(double latParam, double lntParam) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<WifiInfo> wifiInfoList = new ArrayList<>();
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mariadb://localhost/misson1", "cth", "cth12345!!");
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT SEQ," +
					"LAT," +
					"LNT," +
					"WORK_DTTM," +
					"X_SWIFI_ADRES1," +
					"X_SWIFI_ADRES2," +
					"X_SWIFI_CMCWR," +
					"X_SWIFI_CNSTC_YEAR," +
					"X_SWIFI_INOUT_DOOR," +
					"X_SWIFI_INSTL_FLOOR," +
					"X_SWIFI_INSTL_MBY," +
					"X_SWIFI_INSTL_TY," +
					"X_SWIFI_MAIN_NM," +
					"X_SWIFI_MGR_NO," +
					"X_SWIFI_REMARS3," +
					"X_SWIFI_SVC_SE," +
					"X_SWIFI_WRDOFC," +
					"(" +
					"      6371 * acos (" +
					"      cos ( radians( LAT ) )" +
					"      * cos( RADIANS( " + latParam + ") )" +
					"      * cos( radians( LNT ) - RADIANS(" + lntParam + " ) )" +
					"      + sin ( radians( LAT ) )" +
					"      * sin( RADIANS( " + latParam + "))" +
					"    )" +
					" ) AS DISTANCE " +
					"FROM WIFI_INFO " +
					"HAVING DISTANCE < 0.5");
			while (rs.next()) {

				int seq = rs.getInt(1);
				double LAT = rs.getDouble(2);
				double LNT = rs.getDouble(3);
				String WORK_DTTM = rs.getString(4);
				String X_SWIFI_ADRES1 = rs.getString(5);
				String X_SWIFI_ADRES2 = rs.getString(6);
				String X_SWIFI_CMCWR = rs.getString(7);
				String X_SWIFI_CNSTC_YEAR = rs.getString(8);
				String X_SWIFI_INOUT_DOOR = rs.getString(9);
				String X_SWIFI_INSTL_FLOOR = rs.getString(10);
				String X_SWIFI_INSTL_MBY = rs.getString(11);
				String X_SWIFI_INSTL_TY = rs.getString(12);
				String X_SWIFI_MAIN_NM = rs.getString(13);
				String X_SWIFI_MGR_NO = rs.getString(14);
				String X_SWIFI_REMARS3 = rs.getString(15);
				String X_SWIFI_SVC_SE = rs.getString(16);
				String X_SWIFI_WRDOFC = rs.getString(17);
				double DISTANCE = rs.getDouble(18);

				WifiInfo wifiInfo = new WifiInfo();
				wifiInfo.SEQ = seq;
				wifiInfo.LAT = LAT;
				wifiInfo.LNT = LNT;
				wifiInfo.WORK_DTTM = WORK_DTTM;
				wifiInfo.X_SWIFI_ADRES1 = X_SWIFI_ADRES1;
				wifiInfo.X_SWIFI_ADRES2 = X_SWIFI_ADRES2;
				wifiInfo.X_SWIFI_CMCWR = X_SWIFI_CMCWR;
				wifiInfo.X_SWIFI_CNSTC_YEAR = X_SWIFI_CNSTC_YEAR;
				wifiInfo.X_SWIFI_INOUT_DOOR = X_SWIFI_INOUT_DOOR;
				wifiInfo.X_SWIFI_INSTL_FLOOR = X_SWIFI_INSTL_FLOOR;
				wifiInfo.X_SWIFI_INSTL_MBY = X_SWIFI_INSTL_MBY;
				wifiInfo.X_SWIFI_INSTL_TY = X_SWIFI_INSTL_TY;
				wifiInfo.X_SWIFI_MAIN_NM = X_SWIFI_MAIN_NM;
				wifiInfo.X_SWIFI_MGR_NO = X_SWIFI_MGR_NO;
				wifiInfo.X_SWIFI_REMARS3 = X_SWIFI_REMARS3;
				wifiInfo.X_SWIFI_SVC_SE = X_SWIFI_SVC_SE;
				wifiInfo.X_SWIFI_WRDOFC = X_SWIFI_WRDOFC;
				wifiInfo.DISTANCE = DISTANCE;
				wifiInfoList.add(wifiInfo);

			}
		} catch (ClassNotFoundException | SQLException ignored) {
			System.out.println("ignored = " + ignored);

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
		return wifiInfoList;
	}

	private static void insertToWifiInfo(JsonArray jsonArray) {
		Connection con = null;
		PreparedStatement stmt = null;
		String sql = "INSERT INTO WIFI_INFO(LAT,LNT,WORK_DTTM,X_SWIFI_ADRES1,X_SWIFI_ADRES2,X_SWIFI_CMCWR," +
				"X_SWIFI_CNSTC_YEAR,X_SWIFI_INOUT_DOOR,X_SWIFI_INSTL_FLOOR,X_SWIFI_INSTL_MBY,X_SWIFI_INSTL_TY,X_SWIFI_MAIN_NM," +
				"X_SWIFI_MGR_NO,X_SWIFI_REMARS3,X_SWIFI_SVC_SE,X_SWIFI_WRDOFC) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			Class.forName("org.mariadb.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mariadb://localhost/misson1", "cth", "cth12345!!");
			stmt = con.prepareStatement(sql);

			for (int j = 0; j < jsonArray.size(); j++) {
				JsonObject jsonObject = jsonArray.get(j).getAsJsonObject();

				stmt.setDouble(1, jsonObject.get("LAT").getAsDouble());
				stmt.setDouble(2, jsonObject.get("LNT").getAsDouble());
				stmt.setString(3, jsonObject.get("WORK_DTTM").getAsString());
				stmt.setString(4, jsonObject.get("X_SWIFI_ADRES1").getAsString());
				stmt.setString(5, jsonObject.get("X_SWIFI_ADRES2").getAsString());
				stmt.setString(6, jsonObject.get("X_SWIFI_CMCWR").getAsString());
				stmt.setString(7, jsonObject.get("X_SWIFI_CNSTC_YEAR").getAsString());
				stmt.setString(8, jsonObject.get("X_SWIFI_INOUT_DOOR").getAsString());
				stmt.setString(9, jsonObject.get("X_SWIFI_INSTL_FLOOR").getAsString());
				stmt.setString(10, jsonObject.get("X_SWIFI_INSTL_MBY").getAsString());
				stmt.setString(11, jsonObject.get("X_SWIFI_INSTL_TY").getAsString());
				stmt.setString(12, jsonObject.get("X_SWIFI_MAIN_NM").getAsString());
				stmt.setString(13, jsonObject.get("X_SWIFI_MGR_NO").getAsString());
				stmt.setString(14, jsonObject.get("X_SWIFI_REMARS3").getAsString());
				stmt.setString(15, jsonObject.get("X_SWIFI_SVC_SE").getAsString());
				stmt.setString(16, jsonObject.get("X_SWIFI_WRDOFC").getAsString());

				stmt.executeUpdate();
			}

		} catch (ClassNotFoundException | SQLException ignored) {
			System.out.println("ignored = " + ignored);
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


	private static void saveTotalInfo(int totalCount) throws IOException {
		for (int i = 0; i < totalCount; i += 999) {
			int lastIndex;
			if (totalCount - i < 999) {
				lastIndex = totalCount;
			} else {
				lastIndex = (i + 999);
			}
			String targetUrl = "http://openapi.seoul.go.kr:8088/524979496768616836325656415a51/json/TbPublicWifiInfo/" + i + "/" + lastIndex;
			System.out.println("targetUrl = " + targetUrl);
			OkHttpClient client = new OkHttpClient();
			Request.Builder builder = new Request.Builder().url(targetUrl).get();
			Request request = builder.build();

			Response response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				ResponseBody body = response.body();
				if (body != null) {
					String responseString = body.string();
					JsonElement element = JsonParser.parseString(responseString);
					JsonArray rows = element.getAsJsonObject().get("TbPublicWifiInfo").getAsJsonObject().get("row").getAsJsonArray();
					insertToWifiInfo(rows);

				}
			}
		}
	}
}
