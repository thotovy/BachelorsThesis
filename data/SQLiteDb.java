package com.zotmer.heit.data;

import com.zotmer.heit.gui.AlertMessage;
import com.zotmer.heit.gui.Main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.Properties;

public class SQLiteDb {
	private Connection con;
	private Statement stmt;
	private ResultSet rs;
	private static String internalDBPath = "com/zotmer/heit/resources/EnglishForIT.db";
	private static String externalDBPath;

	public SQLiteDb(){
		try {
            Class.forName("org.sqlite.JDBC");
            if(isExternal()) con = DriverManager.getConnection("jdbc:sqlite:" + externalDBPath);
            else con = DriverManager.getConnection("jdbc:sqlite::resource:" + internalDBPath);
		} catch (SQLException | ClassNotFoundException e) {
				e.printStackTrace();
		}
	}

	public ResultSet executeResultQuery(String query) throws SQLException{
			stmt=con.createStatement();
			rs = stmt.executeQuery(query);
			return rs;
	}

	public void closeConnection() {
		try {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			if(con != null) con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void executeUpdate(String query) {
		try {
            stmt = con.createStatement();
            stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    /**
     * Creates a copy of internal database as a new external db.
     * @return true if database was successfuly copied and external is now in use
     */
	public static boolean createExternalIfNotPresent() {
	    if(isExternal()) return true;
	    else if(AlertMessage.confirmation(AlertMessage.CONFIRMATION_CREATE_DB)) {
            try {
                Files.copy(SQLiteDb.class.getResourceAsStream("/" + internalDBPath), Paths.get(externalDBPath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return isExternal();
        }
        else return false;
	}

	private static boolean isExternal(){
		try {
            File jarDir = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            externalDBPath = jarDir.getParentFile()+"/EnglishForIT.db";
            Class.forName("org.sqlite.JDBC");
			Properties config = new Properties();
			config.setProperty("open_mode", "1");
			Connection test = DriverManager.getConnection("jdbc:sqlite:" + externalDBPath,config);
			test.close();
			return true;
		} catch (SQLException | ClassNotFoundException | URISyntaxException e) { //External file does not exist.
			return false;
		}
    }
}
