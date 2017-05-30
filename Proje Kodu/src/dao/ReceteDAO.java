package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import istisna.HastaBulunamadi;
import istisna.HastaKayitli;
import istisna.HastaRecetesiBulunamadi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReceteDAO {
	private Connection con = null;
	final static String COL_ID = "Id";
	final static String COL_TARIHI = "Tarihi";
	final static String COL_HASTA_ID = "HastaId";
	final static String COL_VERILDI = "Verildi";

	public ReceteDAO() throws SQLException {
		con = DbManager.con;
	}

	public Recete ReceteKaydet(Recete _recete) throws SQLException {
		Recete recete = _recete;

		String sqlInsert = new StringBuilder()
				.append("USE ERecete; ")
				.append("INSERT INTO [Recete] ")
				.append("(HastaId) ")
				.append("VALUES ")
				.append("(?); ")
				.append("SELECT SCOPE_IDENTITY(); ")
				.toString();

		try (PreparedStatement statement = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
			// statement.setDate(1, java.sql.Date.valueOf(recete.getTarihi()));
			statement.setInt(1, recete.getHastaId());
			int iAffectedRows = statement.executeUpdate();
			// E�er recete kayd� yap�lamad�ysa istisna d�nece�iz.
			if (iAffectedRows == 0) {
				throw new SQLException("Re�ete olu�turulamad�!");
			} else {
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						recete.setId(generatedKeys.getInt(1));
					} else {
						throw new SQLException("Re�ete olu�turuldu ancak ID bilgisi al�namad�!");
					}
				}
			}
		}

		return recete;
	}

	ObservableList<Recete> ReceteleriCek(String sqlSelect) throws SQLException {
		if (sqlSelect.isEmpty()) {
			throw new IllegalArgumentException("SQL ifadesi bo� olamaz!");
		}
		// D�necek de�er
		ObservableList<Recete> lstReceteler = FXCollections.observableArrayList();

		try (PreparedStatement statement = con.prepareStatement(sqlSelect)) {
			ResultSet rset = statement.executeQuery();
			while (rset.next()) {
				Recete recete = new Recete();
				LocalDate tarihi = (rset.getObject(COL_TARIHI) != null)
						? ((java.sql.Date) rset.getObject(COL_TARIHI)).toLocalDate() : null;
				recete.setId(rset.getInt(COL_ID));
				recete.setHastaId(rset.getInt(COL_HASTA_ID));
				recete.setTarihi(tarihi);
				recete.setVerildi(rset.getBoolean(COL_VERILDI));
				lstReceteler.add(recete);
			}
		}

		return lstReceteler;
	}

	public ObservableList<Recete> ReceteleriCek() throws SQLException {

		String sqlSelect = new StringBuilder()
				.append("USE ERecete; ")
				.append("SELECT * FROM [Recete];")
				.toString();

		ObservableList<Recete> lstReceteler = ReceteleriCek(sqlSelect);
		return lstReceteler;
	}

	public Recete ReceteCek(int iReceteId) throws SQLException, HastaRecetesiBulunamadi {

		String sqlSelect = new StringBuilder()
				.append("USE ERecete; ")
				.append("SELECT * FROM [Recete] ")
				.append("WHERE Id=" + iReceteId)
				.toString();

		ObservableList<Recete> lstReceteler = ReceteleriCek(sqlSelect);
		if(lstReceteler.size()==0){
			throw new HastaRecetesiBulunamadi();
		}
		return lstReceteler.get(0);
	}

	public ObservableList<Recete> HastaReceteleriniGetir(int iHastaId) throws SQLException {
		if (iHastaId < 1) {
			throw new IllegalArgumentException("Hasta ID pozitif ve 0'dan b�y�k bir de�er olmal�d�r!");
		}

		String sqlSelect = new StringBuilder()
				.append("USE ERecete; ")
				.append("SELECT * FROM [Recete] ")
				.append("WHERE HastaID=" + iHastaId)
				.toString();

		ObservableList<Recete> lstReceteler = ReceteleriCek(sqlSelect);
		return lstReceteler;
	}

	public boolean VerildiOlarakIsaretle(int iReceteId) throws SQLException {
		if (iReceteId < 1) {
			throw new IllegalArgumentException("Re�ete ID pozitif ve 0'dan b�y�k bir de�er olmal�d�r!");
		}

		String sqlUpdate = new StringBuilder()
				.append("USE ERecete; ")
				.append("UPDATE [Recete] ")
				.append("SET Verildi=1 ")
				.append("WHERE Id=" + iReceteId)
				.toString();
		
		try (PreparedStatement statement = con.prepareStatement(sqlUpdate)) {
			int iAffectedRows = statement.executeUpdate();
			return iAffectedRows>0;
		}
	}

}
