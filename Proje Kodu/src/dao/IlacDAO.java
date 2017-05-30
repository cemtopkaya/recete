package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import istisna.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class IlacDAO
{
	private Connection con = null;

	final static String COL_BARKODU = "Barkodu";
	final static String COL_FIRMAADI = "FirmaAdi";
	final static String COL_ADI = "Adi";
	final static String COL_ID = "Id";

	public IlacDAO() throws SQLException {
		con = DbManager.con;
	}

	public ObservableList<Ilac> IlaclariGetir() throws SQLException, IlacBulunamadi {
		// dönüþ tipimiz baþlangýçta null olacak.
		// Eðer hasta bulabilirsek dolu, bulamazsak null dönecek
		ObservableList<Ilac> lstIlaclar = FXCollections.observableArrayList();
		String sqlSelect = new StringBuilder().append("USE ERecete; ")
				.append("SELECT ")
				.append("* ")
				.append("FROM [Ilac]; ")
				.toString();

		try (PreparedStatement statement = con.prepareStatement(sqlSelect)) {
			ResultSet rset = statement.executeQuery();
			while (rset.next()) {
				lstIlaclar.add(new Ilac(
						rset.getInt(COL_ID),
						rset.getString(COL_ADI),
						rset.getString(COL_FIRMAADI),
						rset.getString(COL_BARKODU)));
			} 
			if(lstIlaclar.size()==0){
				throw new IlacBulunamadi("Ýlaç bulunamadý!");
			}
		}

		return lstIlaclar;
	}
	
	public List<Ilac> IlacGetir(String adi) throws SQLException, IlacBulunamadi {
		// dönüþ tipimiz baþlangýçta null olacak.
		// Eðer hasta bulabilirsek dolu, bulamazsak null dönecek
		List<Ilac> lstIlaclar = new ArrayList<Ilac>();
		String sqlSelect = new StringBuilder().append("USE ERecete; ")
				.append("SELECT ")
				.append("* ")
				.append("FROM [Ilac] ")
				.append("WHERE Adi LIKE ?")
				.toString();

		try (PreparedStatement statement = con.prepareStatement(sqlSelect)) {
			statement.setString(1, "%" + adi + "%");
			ResultSet rset = statement.executeQuery();
			if (rset.next()) {
				lstIlaclar.add(new Ilac(
						rset.getInt(COL_ID),
						rset.getString(COL_ADI),
						rset.getString(COL_FIRMAADI),
						rset.getString(COL_BARKODU)));
			} else {
				throw new IlacBulunamadi(adi + " isimli ilaç bulunamadý!");
			}
		}
		return lstIlaclar;
	}

	public List<Ilac> IlaclarTop(int iTop) throws SQLException, IlacBulunamadi {

		if(iTop<1){
			return IlaclariGetir();
		}
		List<Ilac> lstIlaclar = null;
		String sqlSelect = new StringBuilder().append("USE ERecete; ")
				.append("SELECT TOP " + iTop + " ")
				.append(" * ")
				.append("FROM [Ilac] ")
				.toString();
		try (PreparedStatement statement = con.prepareStatement(sqlSelect)) {
			lstIlaclar = new ArrayList<Ilac>();
			try (ResultSet rset = statement.executeQuery()) {
				while (rset.next()) {
					lstIlaclar.add(new Ilac(
							rset.getInt(COL_ID),
							rset.getString(COL_ADI),
							rset.getString(COL_FIRMAADI),
							rset.getString(COL_BARKODU)));
				}
			}
		}
		return lstIlaclar;
	}

}
