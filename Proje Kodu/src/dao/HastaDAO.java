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
import istisna.HastaBulunamadi;
import istisna.HastaKayitli;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import uygulama.Uyari;

public class HastaDAO {
	private Connection con = null;
	final static String COL_ID = "Id";
	final static String COL_ADI = "Adi";
	final static String COL_SOYADI = "Soyadi";
	final static String COL_TCNO = "TCNo";
	final static String COL_NOTLAR = "Notlar";
	final static String COL_DTARIHI = "DTarihi";

	public HastaDAO() throws SQLException {
		con = DbManager.con;
	}

	/**
	 * DB'de kayýtlý tüm hastalarý bulup getiren metod.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ObservableList<Hasta> HastalariCek() throws SQLException {
		// Hastalarý bulup bu listeye dolduracaðým. Ve bu listeyi döneceðim.
		ObservableList<Hasta> lstHastalar = FXCollections.observableArrayList();

		String sqlSelect = new StringBuilder()
				.append("USE ERecete; ")
				.append("SELECT Id, Adi, Soyadi, TCNo, Notlar, DTarihi ")
				.append("FROM [Hasta] ;")
				.toString();

		System.out.println(sqlSelect);
		try (Statement statement = con.createStatement()) {

			ResultSet rset = statement.executeQuery(sqlSelect);
			// Sonuç kümesinin next metodunu çaðýrarak okunabilecek bir satýr
			// olup OLMADIÐINI öðreniyoruz
			// Eðer yeni satýr varsa Hasta türünde bir nesne yaratýp bilgileri
			// set ediyoruz.
			while (rset.next()) {
				int id = rset.getInt(COL_ID);
				String adi = rset.getString(COL_ADI);
				String soyadi = rset.getString(COL_SOYADI);
				String tcno = rset.getString(COL_TCNO);
				String not = rset.getString(COL_NOTLAR);
				LocalDate dtar = (rset.getObject(COL_DTARIHI) != null)
						? ((java.sql.Date) rset.getObject(COL_DTARIHI)).toLocalDate() : null;

				Hasta cekilenHasta = new Hasta(id, adi, soyadi, tcno, dtar, not);

				lstHastalar.add(cekilenHasta);
			}
		}

		return lstHastalar;
	}

	/**
	 * ID si verilen hasta bilgisini dönecek
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws HastaBulunamadi
	 */
	public Hasta HastaGetir(int id) throws SQLException, HastaBulunamadi {
		// dönüþ tipimiz baþlangýçta null olacak.
		// Eðer hasta bulabilirsek dolu, bulamazsak null dönecek
		Hasta hasta = null;
		String sqlSelect = new StringBuilder().append("USE ERecete; ").append("SELECT ")
				.append("Id, Adi, Soyadi, TCNo, Notlar, DTarihi ").append("FROM [Hasta] ").append("WHERE Id=?")
				.toString();

		try (PreparedStatement statement = con.prepareStatement(sqlSelect)) {
			statement.setInt(1, id);
			ResultSet rset = statement.executeQuery();
			if (rset.next()) {
				LocalDate dtar = (rset.getObject(COL_DTARIHI) != null)
						? ((java.sql.Date) rset.getObject(COL_DTARIHI)).toLocalDate() : null;
				hasta = new Hasta(rset.getInt(COL_ID), rset.getString(COL_ADI), rset.getString(COL_SOYADI),
						rset.getString(COL_TCNO), dtar, rset.getString(COL_NOTLAR));
			} else {
				throw new HastaBulunamadi("TC Numarasýna baðlý hasta bulunamadý!");
			}
		}
		return hasta;
	}

	/**
	 * TC Numarasý olan hastayý nesne olarak dönecek.
	 * 
	 * @param tcno
	 *            Hastanýn TC Numarasý
	 * @exception Hasta
	 *                bulunamadýysa {@link HastaBulunamadi} istisnasý alýnacak.
	 * @return Kayýtlýysa, Hasta tipinde deðilse null olarak dönecek.
	 * @throws SQLException
	 * @throws HastaBulunamadi
	 */
	public Hasta HastaGetir(String tcno) throws SQLException {
		// SQL ifadesini StringBuilder ile yapýlandýrdýðýmýz için her defasýnda
		// string toplamalarýndan kaynaklanan memory performansý düþmeyecek
		String sqlSelect = new StringBuilder().append("USE ERecete; ").append("SELECT ")
				.append("Id, Adi, Soyadi, TCNo, Notlar, DTarihi ").append("FROM [Hasta] ").append("WHERE TCNo=?")
				.toString();

		try (PreparedStatement statement = con.prepareStatement(sqlSelect)) {
			statement.setString(1, tcno);
			ResultSet rset = statement.executeQuery();
			if (rset.next()) {
				Object sqlDTarihi = rset.getObject(COL_DTARIHI);
				LocalDate dtar = (sqlDTarihi != null) ? ((java.sql.Date) sqlDTarihi).toLocalDate() : null;
				// Hasta bulundu, bilgilerinden nesne yaratýldý ve metottan
				// dönüyor
				return new Hasta(rset.getInt(COL_ID), rset.getString(COL_ADI), rset.getString(COL_SOYADI),
						rset.getString(COL_TCNO), dtar, rset.getString(COL_NOTLAR));
			}

			// Hasta bulunamadý, buraya kadar gelindi. Demekki null dönüyoruz!
			return null;
		}
	}

	/**
	 * Hasta kaydetme metodudur
	 * 
	 * @param hasta
	 *            Hasta tipinde bilgi alýr.
	 * @throws HastaKayitli
	 * @throws HastaBulunamadi
	 * @throws Exception
	 */
	public Hasta HastaKaydet(Hasta hasta) throws SQLException {
		// Eðer hasta kayýt edilemiyorsa null dönecek aksi halde ID bilgisiyle
		// dolu bir hasta dönecek
		Hasta sonucHasta = null;
		try {
			Hasta bulunanHasta = HastaGetir(hasta.getTCNo());
			if (bulunanHasta == null) {
				String sqlInsert = new StringBuilder().append("USE ERecete; ").append("INSERT INTO [Hasta] ")
						.append("(Adi, Soyadi, TCNo, Notlar, DTarihi) ").append("VALUES ").append("(?, ?, ?, ?, ?); ")
						.append("SELECT SCOPE_IDENTITY(); ").toString();

				try (PreparedStatement statement = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
					statement.setString(1, hasta.getAdi());
					statement.setString(2, hasta.getSoyadi());
					statement.setString(3, hasta.getTCNo());
					statement.setString(4, hasta.getNotlar());
					statement.setDate(5, java.sql.Date.valueOf(hasta.getDTarihi()));
					int iAffectedRows = statement.executeUpdate();
					// Eðer hasta kaydý yapýlamadýysa istisna döneceðiz.
					if (iAffectedRows == 0) {
						throw new SQLException("Hasta oluþturulamadý!");
					} else {
						// DB'den, gönderdiðimiz bilgiler için oluþturduðu ID
						// deðerini çekiyoruz
						try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
							if (generatedKeys.next()) {
								hasta.setId(generatedKeys.getInt(1));
								// Dönecek objemizi tekrar dolu bilgilerle set
								// ediyoruz.
								sonucHasta = hasta;
							} else {
								throw new SQLException("Hasta oluþturuldu ancak ID bilgisi alýnamadý!");
							}
						}
					}
				}
			}
		} catch (Exception e) {
			Uyari.Istisna(e);
		}
		return sonucHasta;
	}

	public Hasta HastaGuncelle(Hasta hasta) throws HastaBulunamadi, SQLException {
		String sqlUpdate = new StringBuilder().append("USE ERecete; ").append("UPDATE Hasta ").append("SET ")
				.append("Adi=?, ").append("Soyadi=?, ").append("TCNo=?, ").append("DTarihi=?, ").append("Notlar=? ")
				.append("WHERE Id=? ;").toString();

		try (PreparedStatement statement = con.prepareStatement(sqlUpdate)) {
			statement.setString(1, hasta.getAdi());
			statement.setString(2, hasta.getSoyadi());
			statement.setString(3, hasta.getTCNo());
			statement.setDate(4, java.sql.Date.valueOf(hasta.getDTarihi()));
			statement.setString(5, hasta.getNotlar());
			statement.setInt(6, hasta.getId());
			int iAffectedRows = statement.executeUpdate();
			// Eðer hasta kaydý yapýlamadýysa istisna döneceðiz.
			if (iAffectedRows == 0) {
				throw new HastaBulunamadi("Güncelleme yapýlabilecek hasta bulunamadý!");
			} else {
				return hasta;
			}
		}
	}

	/**
	 * ID ile gönderilen hasta bilgisini siler.
	 * 
	 * @param id
	 *            Hastanýn eþsiz Id deðeridir
	 * @return baþarýlýysa true, silinemediyse false döner
	 * @throws SQLException
	 * @throws HastaBulunamadi
	 */
	public boolean HastaSil(int id) throws SQLException, HastaBulunamadi {
		String sqlUpdate = new StringBuilder().append("USE ERecete; ").append("DELETE FROM Hasta ")
				.append("WHERE Id=? ;").toString();

		HastaGetir(id);

		try (PreparedStatement statement = con.prepareStatement(sqlUpdate)) {
			// Dönen deðeri için deðil, Id ile bir hasta olup olmadýðýnýn
			// kontrolü için metodu çaðýrýyoruz.
			// Eðer hasta yoksa zaten istisna yaratýlacak

			// SQL ifadesi yapýlandýrýlýyor

			statement.setInt(1, id);
			int iAffectedRows = statement.executeUpdate();

			// Hasta silindiyse etkilenen satýr 0'dan farklý olacak
			return iAffectedRows != 0;
		}
	}

	public List<Hasta> HastalarTop(int iTop) throws SQLException {
		if (iTop < 1) {
			return HastalariCek();
		}

		List<Hasta> lstHastalar = null;
		String sqlSelect = new StringBuilder().append("USE ERecete; ").append("SELECT TOP " + iTop + " ").append(" * ")
				.append("FROM [Hasta] ").toString();

		try (PreparedStatement statement = con.prepareStatement(sqlSelect)) {
			lstHastalar = new ArrayList<Hasta>();
			try (ResultSet rset = statement.executeQuery()) {
				while (rset.next()) {
					Object sqlDTarihi = rset.getObject(COL_DTARIHI);
					LocalDate dtar = (sqlDTarihi != null) ? ((java.sql.Date) sqlDTarihi).toLocalDate() : null;

					lstHastalar.add(new Hasta(rset.getInt(COL_ID), rset.getString(COL_ADI), rset.getString(COL_SOYADI),
							rset.getString(COL_TCNO), dtar, rset.getString(COL_NOTLAR)));
				}
			}
		}
		return lstHastalar;
	}

}
