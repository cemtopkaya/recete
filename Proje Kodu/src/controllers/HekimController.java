package controllers;

import uygulama.*;
import dao.*;
import istisna.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class HekimController implements Initializable {

	@FXML
	Button btnReceteOlustur;

	IlacDAO daoIlac;
	HastaDAO daoHasta;
	ICD10DAO daoICD10;
	ReceteDAO daoRecete;
	ReceteIlacDAO daoReceteIlac;

	@FXML
	TableView tvHekimEkrani;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			daoIlac = new IlacDAO();
			daoHasta = new HastaDAO();
			daoICD10 = new ICD10DAO();
			daoRecete = new ReceteDAO();
			daoReceteIlac = new ReceteIlacDAO();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void btnReceteOlusturOnAction(ActionEvent event) {
		try {
			ReceteStage.ShowStage(null);
		} catch (Exception e) {
			e.printStackTrace();
			Uyari.Istisna(e);
		}
	}

	@FXML
	public void btnHastaDuzenleOnAction(ActionEvent event) {
		try {
			HastaStage.ShowStage(null);
		} catch (Exception e) {
			e.printStackTrace();
			Uyari.Istisna(e);
		}
	}

	@FXML
	public void btnHastaListesiOnAction(ActionEvent event) throws SQLException {
		// id
		TableColumn<Hasta, Integer> idCol = new TableColumn<>("Id");
		idCol.setMinWidth(100);
		idCol.setCellValueFactory(new PropertyValueFactory("id"));

		// Adi
		TableColumn<Hasta, Integer> adiCol = new TableColumn<>("Adý");
		adiCol.setMinWidth(100);
		adiCol.setCellValueFactory(new PropertyValueFactory("Adi"));

		// Soyadi
		TableColumn<Hasta, Integer> soyadiCol = new TableColumn<>("Soyadý");
		soyadiCol.setMinWidth(100);
		soyadiCol.setCellValueFactory(new PropertyValueFactory("Soyadi"));

		// TCNo
		TableColumn<Hasta, Integer> tcnoCol = new TableColumn<>("TCNo");
		tcnoCol.setMinWidth(100);
		tcnoCol.setCellValueFactory(new PropertyValueFactory("TCNo"));

		// DTarihi
		TableColumn<Hasta, Integer> dtarihiCol = new TableColumn<>("Doðum Tarihi");
		dtarihiCol.setMinWidth(100);
		dtarihiCol.setCellValueFactory(new PropertyValueFactory("DTarihi"));

		tvHekimEkrani.getColumns().clear();
		tvHekimEkrani.getColumns().addAll(idCol, adiCol, soyadiCol, tcnoCol, dtarihiCol);
		tvHekimEkrani.setItems(daoHasta.HastalariCek());
	}

	@FXML
	public void btnYazilmisRecetelerOnAction(ActionEvent event) throws SQLException {
		// Reçeteleri çek
		ObservableList<Recete> lstReceteler = daoRecete.ReceteleriCek();
		// Hastalari çek
		ObservableList<Hasta> lstHastalar = daoHasta.HastalariCek();
		// Reçetedeki HastaId yi Hastalarda bul ve column'a baðla
		ObservableList<ReceteHasta> lstReceteHasta = FXCollections.observableArrayList();
		for (Recete recete : lstReceteler) {
			ReceteHasta receteHasta = new ReceteHasta();
			receteHasta.Id = recete.getId();
			receteHasta.Tarihi = recete.getTarihi();
			receteHasta.Verildi = recete.isVerildi();
			for (Hasta hasta : lstHastalar) {
				if (hasta.getId() == recete.getHastaId()) {
					receteHasta.setAdi(hasta.getAdi() + " " + hasta.getSoyadi());
					receteHasta.TCNo = hasta.getTCNo();
					break;
				}
			}
			lstReceteHasta.add(receteHasta);
		}

		// id
		TableColumn<ReceteHasta, Integer> idCol = new TableColumn<>("Id");
		idCol.setCellValueFactory(new PropertyValueFactory("Id"));
		// Adi
		TableColumn<ReceteHasta, String> hastaAdiCol = new TableColumn<>("Hasta Adý");
		hastaAdiCol.setMinWidth(200);
		hastaAdiCol.setCellValueFactory(new PropertyValueFactory("Adi"));
		// TC No
		TableColumn<ReceteHasta, String> tcnoCol = new TableColumn<>("TC No");
		tcnoCol.setCellValueFactory(new PropertyValueFactory("TCNo"));
		// Tarihi
		TableColumn<ReceteHasta, Date> tarihiCol = new TableColumn<>("Tarihi");
		tarihiCol.setCellValueFactory(new PropertyValueFactory("Tarihi"));
		// DTarihi
		TableColumn<ReceteHasta, Boolean> verildiCol = new TableColumn<>("Verildi");
		verildiCol.setCellValueFactory(cellValue -> {
			return new SimpleBooleanProperty(cellValue.getValue().isVerildi());
		});
		verildiCol.setCellFactory(CheckBoxTableCell.forTableColumn(verildiCol));

		tvHekimEkrani.getColumns().clear();
		tvHekimEkrani.getColumns().addAll(idCol, hastaAdiCol, tcnoCol, tarihiCol, verildiCol);
		tvHekimEkrani.setItems(lstReceteHasta);
	}

	@FXML
	public void btnTanilarOnAction(ActionEvent event) throws SQLException {
		// id
		TableColumn<Hasta, Integer> idCol = new TableColumn<>("Id");
		idCol.setMinWidth(100);
		idCol.setCellValueFactory(new PropertyValueFactory("id"));

		// Adi
		TableColumn<Hasta, Integer> adiCol = new TableColumn<>("Adý");
		adiCol.setMinWidth(100);
		adiCol.setCellValueFactory(new PropertyValueFactory("Adi"));

		// Soyadi
		TableColumn<Hasta, Integer> koduCol = new TableColumn<>("Kodu");
		koduCol.setMinWidth(100);
		koduCol.setCellValueFactory(new PropertyValueFactory("Kodu"));

		tvHekimEkrani.getColumns().clear();
		tvHekimEkrani.getColumns().addAll(idCol, adiCol, koduCol);
		ObservableList<ICD10> lstICD10Codes = daoICD10.ICD10KodlariniCek();
		System.out.println(lstICD10Codes.size());
		tvHekimEkrani.setItems(lstICD10Codes);
	}

	@FXML
	public void btnIlaclarOnAction(ActionEvent event) throws SQLException, IlacBulunamadi {
		// Id
		TableColumn<Hasta, Integer> idCol = new TableColumn<>("Id");
		idCol.setMinWidth(100);
		idCol.setCellValueFactory(new PropertyValueFactory("Id"));

		// Adi
		TableColumn<Hasta, Integer> adiCol = new TableColumn<>("Adý");
		adiCol.setMinWidth(100);
		adiCol.setCellValueFactory(new PropertyValueFactory("Adi"));

		// FirmaAdi
		TableColumn<Hasta, Integer> firmaAdiCol = new TableColumn<>("Firma Adý");
		firmaAdiCol.setMinWidth(100);
		firmaAdiCol.setCellValueFactory(new PropertyValueFactory("FirmaAdi"));

		// Barkodu
		TableColumn<Hasta, Integer> koduCol = new TableColumn<>("Barkodu");
		koduCol.setMinWidth(100);
		koduCol.setCellValueFactory(new PropertyValueFactory("Barkodu"));

		tvHekimEkrani.getColumns().clear();
		tvHekimEkrani.getColumns().addAll(idCol, adiCol, firmaAdiCol, koduCol);
		tvHekimEkrani.setItems(daoIlac.IlaclariGetir());
	}
}
