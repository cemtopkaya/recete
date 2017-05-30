package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import istisna.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ICD10DAO
{
	private Connection con = null;
	final static String COL_ID = "Id";
	final static String COL_ADI = "Adi";
	final static String COL_KODU = "Kodu";

	public ICD10DAO() throws SQLException {
		con = DbManager.con;
	}

	public ObservableList<ICD10> ICD10KodlariniCek() throws SQLException {
		ObservableList<ICD10> lstIcd10 = FXCollections.observableArrayList();

		String sqlSelect = new StringBuilder().append("USE ERecete; ")
				.append("SELECT Id, Adi, Kodu ")
				.append("FROM ICD10 ;")
				.toString();

		System.out.println(sqlSelect);
		try (Statement statement = con.createStatement()) {

			ResultSet rset = statement.executeQuery(sqlSelect);
			while (rset.next()) {
				lstIcd10.add(new ICD10(
						rset.getInt(COL_ID),
						rset.getString(COL_ADI),
						rset.getString(COL_KODU)));
			}

			System.out.println(lstIcd10.size());
		}

		return lstIcd10;
	}

}
