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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReceteIlacDAO {
	private static final String COL_ID = "Id";
	private static final String COL_BIRIM = "Birim";
	private static final String COL_DOZ = "Doz";
	private static final String COL_ILAC_ADI = "IlacAdi";
	private static final String COL_ILAC_ID = "IlacId";
	private static final String COL_PERIYOT = "Periyot";
	private static final String COL_RECETE_ID = "ReceteId";
	private static final String COL_TANI_ADI = "TaniAdi";
	private static final String COL_TANI_ID = "TaniId";
	private Connection con = null;

	public ReceteIlacDAO() throws SQLException {
		con = DbManager.con;
	}

	public ReceteIlac ReceteIlacKaydet(ReceteIlac _receteIlaci) throws SQLException {
		ReceteIlac receteIlaci = _receteIlaci;

		String sqlInsert = new StringBuilder().append("USE ERecete; ")
				.append("INSERT INTO [ReceteIlac] ")
				.append("(ReceteId, IlacId, TaniId, Doz, Periyot, Birim) ")
				.append("VALUES ")
				.append("(?, ?, ?, ?, ?, ?); ")
				.append("SELECT SCOPE_IDENTITY(); ").toString();

		try (PreparedStatement statement = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
			statement.setInt(1, receteIlaci.getReceteId());
			statement.setInt(2, receteIlaci.getIlacId());
			statement.setInt(3, receteIlaci.getTaniId());
			statement.setInt(4, receteIlaci.getDoz());
			statement.setInt(5, receteIlaci.getPeriyot());
			statement.setString(6, receteIlaci.getBirim());
			int iAffectedRows = statement.executeUpdate();
			// Eðer recete ilacý kaydý yapýlamadýysa istisna döneceðiz.
			if (iAffectedRows == 0) {
				throw new SQLException("Reçete Ýlacý oluþturulamadý!");
			} else {
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						receteIlaci.setId(generatedKeys.getInt(1));
					} else {
						throw new SQLException("Reçete Ýlacý oluþturuldu ancak ID bilgisi alýnamadý!");
					}
				}
			}
		}

		return receteIlaci;
	}

	public ObservableList<ReceteIlac> ReceteIlaclariniGetir(int iReceteId) throws SQLException {
		ObservableList<ReceteIlac> lstReceteIlaclari = FXCollections.observableArrayList();

		String sqlSelect = new StringBuilder().append("USE ERecete; ")
				.append("SELECT ri.* , i.Adi AS IlacAdi, t.Adi AS TaniAdi ")
				.append("FROM ReceteIlac AS ri ")
				.append("INNER JOIN Ilac AS i ON i.Id=ri.IlacId ")
				.append("INNER JOIN ICD10 AS t ON t.Id=ri.TaniId ")
				.append("WHERE ReceteId=" + iReceteId)
				.toString();

		try (PreparedStatement statement = con.prepareStatement(sqlSelect)) {
			ResultSet rset = statement.executeQuery();
			while (rset.next()) {
				ReceteIlac receteIlaci = new ReceteIlac();
				receteIlaci.setId(rset.getInt(COL_ID));
				receteIlaci.setBirim(rset.getString(COL_BIRIM));
				receteIlaci.setDoz(rset.getInt(COL_DOZ));
				receteIlaci.setIlacAdi(rset.getString(COL_ILAC_ADI));
				receteIlaci.setIlacId(rset.getInt(COL_ILAC_ID));
				receteIlaci.setPeriyot(rset.getInt(COL_PERIYOT));
				receteIlaci.setReceteId(rset.getInt(COL_RECETE_ID));
				receteIlaci.setTaniAdi(rset.getString(COL_TANI_ADI));
				receteIlaci.setTaniId(rset.getInt(COL_TANI_ID));

				lstReceteIlaclari.add(receteIlaci);
			}
		}

		return lstReceteIlaclari;
	}
}