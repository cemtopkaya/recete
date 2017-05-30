package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import istisna.IlacBulunamadi;
import istisna.KullaniciBulunamadi;

public class KullaniciDAO {
	public static Connection con = null;

	final static String COL_ID = "Id";
	final static String COL_KULLANICI_ADI = "KullaniciAdi";
	final static String COL_SIFRE = "Sifre";
	final static String COL_DOKTOR = "Doktor";
	final static String COL_ECZACI = "Eczaci";

	public KullaniciDAO() throws SQLException {
		con = DbManager.con;
	}

	public Kullanici KullaniciGetir(String kullaniciAdi, String sifre) throws SQLException {
		Kullanici kullanici = null;

		String sqlSelect = new StringBuilder().append("USE ERecete; ")
				.append("SELECT ")
				.append("* ")
				.append("FROM [Kullanici] ")
				.append("WHERE KullaniciAdi=?")
				.toString();

		try (PreparedStatement statement = con.prepareStatement(sqlSelect)) {
			statement.setString(1, kullaniciAdi);
			ResultSet rset = statement.executeQuery();
			while (rset.next()) {
				if (!rset.getString(COL_SIFRE).equals(sifre))
					continue;

				kullanici = new Kullanici();
				kullanici.setId(rset.getInt(COL_ID));
				kullanici.setKullaniciAdi(rset.getString(COL_KULLANICI_ADI));
				kullanici.setSifre(rset.getString(COL_SIFRE));
				kullanici.setDoktor(rset.getBoolean(COL_DOKTOR));
				kullanici.setEczaci(rset.getBoolean(COL_ECZACI));
			}
		}

		return kullanici;
	}

	public Kullanici KullaniciGetir(int iKullaniciId) throws SQLException, KullaniciBulunamadi {
		Kullanici kullanici = null;

		String sqlSelect = new StringBuilder().append("USE ERecete; ")
				.append("SELECT ")
				.append("* ")
				.append("FROM [Kullanici] ")
				.append("WHERE Id=" + iKullaniciId)
				.toString();

		try (PreparedStatement statement = con.prepareStatement(sqlSelect)) {
			ResultSet rset = statement.executeQuery();
			if (rset.next()) {
				kullanici = new Kullanici();
				kullanici.setId(rset.getInt(COL_ID));
				kullanici.setKullaniciAdi(rset.getString(COL_KULLANICI_ADI));
				kullanici.setSifre(rset.getString(COL_SIFRE));
				kullanici.setDoktor(rset.getBoolean(COL_DOKTOR));
				kullanici.setEczaci(rset.getBoolean(COL_ECZACI));
			} else {
				throw new KullaniciBulunamadi(iKullaniciId + " ID'li kullanýcý bulunamadý!");
			}
		}

		return kullanici;
	}
}
