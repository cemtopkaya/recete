package dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;
import uygulama.Uyari;

public class DbManager
{

	static String DB_URL = "jdbc:jtds:sqlserver://127.0.0.1:1433/ERecete;instance=MSSQLSERVER";
	static String USER = "sa";
	static String PASS = "q1w2.e3r4";

	public static Connection con = null;
	
	static {
		FileInputStream fis;
		try {
			fis = new FileInputStream("prefs.xml");
			Preferences.importPreferences(fis);
			fis.close();

			Preferences prefsRoot = Preferences.userRoot();
			Preferences prefsCnn = prefsRoot.node("connectionString");
			DbManager.DB_URL = prefsCnn.get("DB_URL", "jdbc:jtds:sqlserver://127.0.0.1:1433/ERecete;instance=MSSQLSERVER");
			DbManager.USER = prefsCnn.get("USER", "sa");
			DbManager.PASS = prefsCnn.get("PASS", "q1w2.e3r4");

			con = DriverManager.getConnection(DbManager.DB_URL);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Uyari.Istisna(e);
		} catch (IOException e) {
			e.printStackTrace();
			Uyari.Istisna(e);
		} catch (InvalidPreferencesFormatException e) {
			e.printStackTrace();
			Uyari.Istisna(e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
