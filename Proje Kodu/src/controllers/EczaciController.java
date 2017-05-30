package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import dao.*;
import istisna.HastaBulunamadi;
import javafx.fxml.Initializable;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import uygulama.Uyari;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.event.ActionEvent;

public class EczaciController implements Initializable {
	IlacDAO daoIlac;
	HastaDAO daoHasta;
	ICD10DAO daoICD10;
	ReceteDAO daoRecete;
	ReceteIlacDAO daoReceteIlac;

	@FXML
	TextField txtTCNo;
	@FXML
	Button btnHastaReceteleriniAra;
	@FXML
	TableView tvHastaReceteleri;
	@FXML
	TableView tvReceteIlaclari;
	@FXML
	ContextMenu ctxMenu;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			daoIlac = new IlacDAO();
			daoHasta = new HastaDAO();
			daoICD10 = new ICD10DAO();
			daoRecete = new ReceteDAO();
			daoReceteIlac = new ReceteIlacDAO();

		} catch (Exception e) {
			e.printStackTrace();
			Uyari.Istisna(e);
		}

	}

	@FXML
	public void tvHastaReceteleriOnMousePressed(MouseEvent event) {
		if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
			Node targetNode = (Node) event.getTarget();
			Node parentNode = targetNode.getParent();
			// Se�ilen sat�r TableRow ise
			TableRow row = (parentNode instanceof TableRow ? (TableRow) parentNode : null);
			// De�il ama parent'� TableRow ise
			if (row == null && parentNode.getParent() instanceof TableRow) {
				row = (TableRow) parentNode.getParent();
			}

			try {
				if (row != null) {
					// Se�ilen sat�r ReceteHasta olarak cast edilebiliyorsa
					if (row.getItem() instanceof ReceteHasta) {
						ReceteHasta secilenRecete = (ReceteHasta) row.getItem();
						ReceteIlaclariniBagla(secilenRecete.getId());
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				Uyari.Istisna(e);
			}
		}
	}

	private void tvReceteIlaclariTablosunuHazirla() {
		TableColumn<ReceteIlac, Integer> ilacAdiCol = new TableColumn<>("�la� Ad�");
		ilacAdiCol.setMinWidth(200);
		ilacAdiCol.setCellValueFactory(new PropertyValueFactory("IlacAdi"));

		TableColumn<ReceteIlac, Integer> dozCol = new TableColumn<>("Doz");
		dozCol.setMinWidth(60);
		dozCol.setCellValueFactory(new PropertyValueFactory("Doz"));

		TableColumn<ReceteIlac, Integer> periyotCol = new TableColumn<>("Periyot");
		periyotCol.setMinWidth(60);
		periyotCol.setCellValueFactory(new PropertyValueFactory("Periyot"));

		TableColumn<ReceteIlac, Integer> birimCol = new TableColumn<>("Birim");
		birimCol.setMinWidth(100);
		birimCol.setCellValueFactory(new PropertyValueFactory("Birim"));

		TableColumn<ReceteIlac, Integer> taniAdiCol = new TableColumn<>("Tan� Ad�");
		taniAdiCol.setMinWidth(200);
		taniAdiCol.setCellValueFactory(new PropertyValueFactory("TaniAdi"));

		tvReceteIlaclari.getColumns().clear();
		tvReceteIlaclari.getColumns().addAll(ilacAdiCol, dozCol, periyotCol, birimCol, taniAdiCol);
	}

	void ReceteIlaclariniBagla(int iReceteId) throws SQLException {
		tvReceteIlaclariTablosunuHazirla();
		ObservableList<ReceteIlac> lstReceteIlac = daoReceteIlac.ReceteIlaclariniGetir(iReceteId);
		tvReceteIlaclari.getItems().setAll(lstReceteIlac);
	}

	@FXML
	public void btnHastaReceteleriniAraOnAction() {
		String tcno = txtTCNo.getText().replaceAll("[^\\d]", "").trim();

		if (tcno.length() < 11) {
			String title = "TC No Eksik!";
			String header = "TC Numaras� Bilgisi Hatal�";
			String content = "TC No bilgisinin 11 rakamdan az olmamas� gerekiyor!";
			Uyari.Hatali(title, header, content);
		} else {

			try {
				// Hastay� bul
				Hasta arananHasta = daoHasta.HastaGetir(tcno);
				int iHastaId = arananHasta.getId();

				// Re�etelerini �ek
				ObservableList<Recete> lstHastaReceteleri = daoRecete.HastaReceteleriniGetir(iHastaId);

				// Re�etedeki HastaId yi hastaya ba�la
				ObservableList<ReceteHasta> lstReceteHasta = FXCollections.observableArrayList();

				for (Recete recete : lstHastaReceteleri) {

					ReceteHasta receteHasta = new ReceteHasta();
					receteHasta.Id = recete.getId();
					receteHasta.Tarihi = recete.getTarihi();
					receteHasta.Verildi = recete.isVerildi();
					receteHasta.setAdi(arananHasta.getAdi() + " " + arananHasta.getSoyadi());
					receteHasta.TCNo = arananHasta.getTCNo();

					lstReceteHasta.add(receteHasta);
				}

				// id
				TableColumn<ReceteHasta, Integer> idCol = new TableColumn<>("Id");
				idCol.setCellValueFactory(new PropertyValueFactory("Id"));
				// Adi
				TableColumn<ReceteHasta, String> hastaAdiCol = new TableColumn<>("Hasta Ad�");
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

				tvHastaReceteleri.getColumns().clear();
				tvHastaReceteleri.getColumns().addAll(idCol, hastaAdiCol, tcnoCol, tarihiCol, verildiCol);
				tvHastaReceteleri.setItems(lstReceteHasta);

			} catch (SQLException e) {
				Uyari.Istisna(e);
			}
		}
	}

	@FXML
	public void ctxReceteVerildi(ActionEvent event) {
		ReceteHasta receteHasta = (ReceteHasta) tvHastaReceteleri.getSelectionModel().getSelectedItem();
		if (receteHasta.isVerildi()) {
			String title = "RE�ETE UYGUN DE��L";
			String header = "Re�ete Daha �nce Verilmi�!";
			String content = "Re�ete ila�lar� daha �nce hastaya verilmi� bu y�zden tekrar verildi olarak i�aretleyemezsiniz!";
			Uyari.Hatali(title, header, content);
		} else {
			try {

				boolean basarili = daoRecete.VerildiOlarakIsaretle(receteHasta.Id);
				String header = "G�NCELLEME BA�ARILI";
				String content = "Re�ete ila�lar� hasta teslim edildi olarak g�ncellendi.";
				Uyari.Basarili(header, content);
				btnHastaReceteleriniAraOnAction();
			} catch (SQLException e) {
				e.printStackTrace();
				Uyari.Istisna(e);
			}
		}
		System.out.println("Verildi diye i�aretlenecek");
	}
}
