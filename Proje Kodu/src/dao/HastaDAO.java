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
	 * DB'de kay�tl� t�m hastalar� bulup getiren metod.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ObservableList<Hasta> HastalariCek() throws SQLException {
		// Hastalar� bulup bu listeye dolduraca��m. Ve bu listeyi d�nece�im.
		ObservableList<Hasta> lstHastalar = FXCollections.observableArrayList();

		String sqlSelect = new StringBuilder()
				.append("USE ERecete; ")
				.append("SELECT Id, Adi, Soyadi, TCNo, Notlar, DTarihi ")
				.append("FROM [Hasta] ;")
				.toString();

		System.out.println(sqlSelect);
		try (Statement statement = con.createStatement()) {

			ResultSet rset = statement.executeQuery(sqlSelect);
			// Sonu� k�mesinin next metodunu �a��rarak okunabilecek bir sat�r
			// olup OLMADI�INI ��reniyoruz
			// E�er yeni sat�r varsa Hasta t�r�nde bir nesne yarat�p bilgileri
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
	 * ID si verilen hasta bilgisini d�necek
	 * @param id
	 * @return
	 * @throws SQLException
	 * @throws HastaBulunamadi
	 */
	public Hasta HastaGetir(int id) throws SQLException, HastaBulunamadi {
		// d�n�� tipimiz ba�lang��ta null olacak.
		// E�er hasta bulabilirsek dolu, bulamazsak null d�necek
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
				throw new HastaBulunamadi("TC Numaras�na ba�l� hasta bulunamad�!");
			}
		}
		return hasta;
	}

	/**
	 * TC Numaras� olan hastay� nesne olarak d�necek.
	 * 
	 * @param tcno
	 *            Hastan�n TC Numaras�
	 * @exception Hasta
	 *                bulunamad�ysa {@link HastaBulunamadi} istisnas� al�nacak.
	 * @return Kay�tl�ysa, Hasta tipinde de�ilse null olarak d�necek.
	 * @throws SQLException
	 * @throws HastaBulunamadi
	 */
	public Hasta HastaGetir(String tcno) throws SQLException {
		// SQL ifadesini StringBuilder ile yap�land�rd���m�z i�in her defas�nda
		// string toplamalar�ndan kaynaklanan memory performans� d��meyecek
		String sqlSelect = new StringBuilder().append("USE ERecete; ").append("SELECT ")
				.append("Id, Adi, Soyadi, TCNo, Notlar, DTarihi ").append("FROM [Hasta] ").append("WHERE TCNo=?")
				.toString();

		try (PreparedStatement statement = con.prepareStatement(sqlSelect)) {
			statement.setString(1, tcno);
			ResultSet rset = statement.executeQuery();
			if (rset.next()) {
				Object sqlDTarihi = rset.getObject(COL_DTARIHI);
				LocalDate dtar = (sqlDTarihi != null) ? ((java.sql.Date) sqlDTarihi).toLocalDate() : null;
				// Hasta bulundu, bilgilerinden nesne yarat�ld� ve metottan
				// d�n�yor
				return new Hasta(rset.getInt(COL_ID), rset.getString(COL_ADI), rset.getString(COL_SOYADI),
						rset.getString(COL_TCNO), dtar, rset.getString(COL_NOTLAR));
			}

			// Hasta bulunamad�, buraya kadar gelindi. Demekki null d�n�yoruz!
			return null;
		}
	}

	/**
	 * Hasta kaydetme metodudur
	 * 
	 * @param hasta
	 *            Hasta tipinde bilgi al�r.
	 * @throws HastaKayitli
	 * @throws HastaBulunamadi
	 * @throws Exception
	 */
	public Hasta HastaKaydet(Hasta hasta) throws SQLException {
		// E�er hasta kay�t edilemiyorsa null d�necek aksi halde ID bilgisiyle
		// dolu bir hasta d�necek
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
					// E�er hasta kayd� yap�lamad�ysa istisna d�nece�iz.
					if (iAffectedRows == 0) {
						throw new SQLException("Hasta olu�turulamad�!");
					} else {
						// DB'den, g�nderdi�imiz bilgiler i�in olu�turdu�u ID
						// de�erini �ekiyoruz
						try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
							if (generatedKeys.next()) {
								hasta.setId(generatedKeys.getInt(1));
								// D�necek objemizi tekrar dolu bilgilerle set
								// ediyoruz.
								sonucHasta = hasta;
							} else {
								throw new SQLException("Hasta olu�turuldu ancak ID bilgisi al�namad�!");
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
			// E�er hasta kayd� yap�lamad�ysa istisna d�nece�iz.
			if (iAffectedRows == 0) {
				throw new HastaBulunamadi("G�ncelleme yap�labilecek hasta bulunamad�!");
			} else {
				return hasta;
			}
		}
	}

	/**
	 * ID ile g�nderilen hasta bilgisini siler.
	 * 
	 * @param id
	 *            Hastan�n e�siz Id de�eridir
	 * @return ba�ar�l�ysa true, silinemediyse false d�ner
	 * @throws SQLException
	 * @throws HastaBulunamadi
	 */
	public boolean HastaSil(int id) throws SQLException, HastaBulunamadi {
		String sqlUpdate = new StringBuilder().append("USE ERecete; ").append("DELETE FROM Hasta ")
				.append("WHERE Id=? ;").toString();

		HastaGetir(id);

		try (PreparedStatement statement = con.prepareStatement(sqlUpdate)) {
			// D�nen de�eri i�in de�il, Id ile bir hasta olup olmad���n�n
			// kontrol� i�in metodu �a��r�yoruz.
			// E�er hasta yoksa zaten istisna yarat�lacak

			// SQL ifadesi yap�land�r�l�yor

			statement.setInt(1, id);
			int iAffectedRows = statement.executeUpdate();

			// Hasta silindiyse etkilenen sat�r 0'dan farkl� olacak
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
