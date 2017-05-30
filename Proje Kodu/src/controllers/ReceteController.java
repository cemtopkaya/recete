package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import dao.*;
import uygulama.*;
import istisna.HastaBulunamadi;
import istisna.IlacBulunamadi;
import javafx.beans.binding.StringExpression;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderStroke;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import uygulama.Uyari;
import uygulama.Validasyon;
import javafx.scene.control.TableView;

public class ReceteController implements Initializable
{

	@FXML TextField txtTaniKodu;
	@FXML Button toolbarBtnReceteEkle;
	@FXML Button toolbarBtnReceteDuzenle;
	@FXML Button toolbarBtnReceteSil;
	@FXML TextField txtHastaAdi;
	@FXML TextField txtIlacAdi;
	@FXML TextField txtDoz;
	@FXML TextField txtPeriyot;
	@FXML ComboBox<String> cbBirim;
	@FXML Button btnIlacEkle;
	@FXML Button btnReceteKaydet;
	@FXML TableView tvReceteIlaclari;
	@FXML Button btnSeciliIlaciSil;

	IlacDAO daoIlac;
	HastaDAO daoHasta;
	ICD10DAO daoICD10;
	ReceteDAO daoRecete;
	ReceteIlacDAO daoReceteIlac;

	@Override public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Re�ete controller ba�lat�ld�");
		try {
			daoIlac = new IlacDAO();
			daoHasta = new HastaDAO();
			daoICD10 = new ICD10DAO();
			daoRecete = new ReceteDAO();
			daoReceteIlac = new ReceteIlacDAO();

			IlaclariBagla();
			HastalariBagla();
			ICD10Bagla();
			Validasyon();
			BirimleriBagla();
			TableViewHazirla();
		} catch (SQLException e) {
			e.printStackTrace();
			Uyari.Istisna(e);
		} catch (IlacBulunamadi e) {
			e.printStackTrace();
			Uyari.Istisna(e);
		}
	}

	private void TableViewHazirla() {
		TableColumn<ReceteIlac, Integer> ilacIdCol = new TableColumn<>("�la� Ad�");
		ilacIdCol.setMinWidth(100);
		ilacIdCol.setCellValueFactory(new PropertyValueFactory("IlacAdi"));

		TableColumn<ReceteIlac, Integer> taniIdCol = new TableColumn<>("Tan� Ad�");
		taniIdCol.setMinWidth(100);
		taniIdCol.setCellValueFactory(new PropertyValueFactory("TaniAdi"));

		TableColumn<ReceteIlac, Integer> dozCol = new TableColumn<>("Doz");
		dozCol.setMinWidth(100);
		dozCol.setCellValueFactory(new PropertyValueFactory("Doz"));

		TableColumn<ReceteIlac, Integer> periyotCol = new TableColumn<>("Periyot");
		periyotCol.setMinWidth(100);
		periyotCol.setCellValueFactory(new PropertyValueFactory("Periyot"));

		TableColumn<ReceteIlac, Integer> birimCol = new TableColumn<>("Birim");
		birimCol.setMinWidth(100);
		birimCol.setCellValueFactory(new PropertyValueFactory("Birim"));

		tvReceteIlaclari.getColumns().clear();
		tvReceteIlaclari.getColumns().addAll(ilacIdCol, taniIdCol, dozCol, periyotCol, birimCol);

	}

	void BirimleriBagla() {
		cbBirim.getItems().addAll("G�nde", "Haftada", "Ayda", "Y�lda");
	}

	void Validasyon() {
		txtDoz.textProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal.matches("\\d*")) {
				Validasyon.ValAlertRequired(txtDoz, "Sadece say� i�ermeli!");
				txtDoz.setText(newVal.replaceAll("[^\\d]", ""));
			} else {
				Validasyon.ValCssBorderNormal(txtDoz);
			}

			if (newVal.length() < 1) {
				Validasyon.ValAlertRequired(txtDoz, "Bo� b�rak�lamaz!");
			} else {
				Validasyon.ValCssBorderNormal(txtDoz);
			}
		});

		txtPeriyot.textProperty().addListener((obs, oldVal, newVal) -> {
			if (!newVal.matches("\\d*")) {
				Validasyon.ValAlertRequired(txtPeriyot, "Sadece say� i�ermeli!");
				txtPeriyot.setText(newVal.replaceAll("[^\\d]", ""));
			} else {
				Validasyon.ValCssBorderNormal(txtPeriyot);
			}

			if (newVal.length() < 1) {
				Validasyon.ValAlertRequired(txtPeriyot, "Bo� b�rak�lamaz!");
			} else {
				Validasyon.ValCssBorderNormal(txtPeriyot);
			}
		});

	}

	void IlaclariBagla() throws SQLException, IlacBulunamadi {
		List<Ilac> lstIlaclar = daoIlac.IlaclariGetir();
		if (lstIlaclar == null) {
			Uyari.Hatali("�la� Listesi", "�la� Listesi �ekilemedi", "�la�lar bo� geldi!");
		}
		// Ila�lar�n hepsi �ekilecek ve TextField'a ba�lanacak
		ArrayList<String> alIlaclar = new ArrayList<String>();
		for (Ilac ilac : lstIlaclar) {
			Integer id = ilac.getId();
			String adi = ilac.getAdi();
			alIlaclar.add(id + " | " + adi);
		}
		TextFields.bindAutoCompletion(txtIlacAdi, alIlaclar);
	}

	void HastalariBagla() throws SQLException {
		List<Hasta> lstHastalar = daoHasta.HastalarTop(0);
		if (lstHastalar == null) {
			Uyari.Hatali("�la� Listesi", "�la� Listesi �ekilemedi", "�la�lar bo� geldi!");
		}

		// Hastalar�n hepsi �ekilecek ve TextField'a ba�lanacak
		ArrayList<String> alHastalar = new ArrayList<String>();
		for (Hasta hasta : lstHastalar) {
			Integer id = hasta.getId();
			String adi = hasta.getAdi();
			String soyadi = hasta.getSoyadi();
			alHastalar.add(id + " | " + adi + " " + soyadi);
		}
		TextFields.bindAutoCompletion(txtHastaAdi, alHastalar);
	}

	void ICD10Bagla() throws SQLException {
		List<ICD10> lstICD10 = daoICD10.ICD10KodlariniCek();
		System.out.println("ICD10 leri cektik");
		if (lstICD10 == null) {
			Uyari.Hatali("�la� Listesi", "�la� Listesi �ekilemedi", "�la�lar bo� geldi!");
		}

		// Hastalar�n hepsi �ekilecek ve TextField'a ba�lanacak
		ArrayList<String> alICD10 = new ArrayList<String>();
		for (ICD10 icd : lstICD10) {
			int id = icd.getId();
			String adi = icd.getAdi();
			String kodu = icd.getKodu();
			alICD10.add(id + " | " + kodu + " | " + adi);
		}

		System.out.println("Hastalari bagliyoruz");
		TextFields.bindAutoCompletion(txtTaniKodu, alICD10);
		System.out.println("Hastalari bagladik");
	}

	@FXML public void btnIlacEkleOnAction(ActionEvent event) {

		Validasyon.ValCssBorderNormal(txtIlacAdi);
		Validasyon.ValCssBorderNormal(txtTaniKodu);
		Validasyon.ValCssBorderNormal(txtDoz);
		Validasyon.ValCssBorderNormal(txtPeriyot);

		String taniKodu = txtTaniKodu.getText().trim();
		String ilacAdi = txtIlacAdi.getText().trim();
		String doz = txtDoz.getText().trim();
		String periyot = txtPeriyot.getText().trim();
		String secilenBirim;

		if (cbBirim.getSelectionModel().getSelectedItem() == null || cbBirim.getSelectionModel().getSelectedItem().equals("Birimi Se�iniz...")) {
			String title = "DO�RULAMA HATASI";
			String header = "�la� birim i�in girilen bilgi yeterli de�il!";
			String content = "L�tfen bir birim se�iniz. �rn. Ay, G�n, Hafta vs.";
			Uyari.Hatali(title, header, content);
		} else if (txtIlacAdi.getText().length() == 0
				|| txtTaniKodu.getText().length() == 0
				|| txtDoz.getText().length() == 0
				|| txtPeriyot.getText().length() == 0) {
			if (txtIlacAdi.getText().length() == 0) {
				Validasyon.ValAlertRequired(txtIlacAdi, "Bo� b�rak�lamaz!");
			}
			if (txtTaniKodu.getText().length() == 0) {
				Validasyon.ValAlertRequired(txtTaniKodu, "Bo� b�rak�lamaz!");
			}
			if (txtDoz.getText().length() == 0) {
				Validasyon.ValAlertRequired(txtDoz, "Bo� b�rak�lamaz!");
			}
			if (txtPeriyot.getText().length() == 0) {
				Validasyon.ValAlertRequired(txtPeriyot, "Bo� b�rak�lamaz!");
			}
		} else {

			secilenBirim = cbBirim.getSelectionModel().getSelectedItem().toString();

			try {
				String[] arrIlac = ilacAdi.split("\\s\\|\\s");
				String[] arrTani = taniKodu.split("\\s\\|\\s");
				int iIlacId = Integer.parseInt(arrIlac[0]);
				int iTaniId = Integer.parseInt(arrTani[0]);				
				int iDoz = Integer.parseInt(doz.trim());
				int iPeriyot = Integer.parseInt(periyot.trim());
				ReceteIlac receteIlac = new ReceteIlac();
				receteIlac.setIlacId(iIlacId);
				receteIlac.setIlacAdi(arrIlac[1]);
				receteIlac.setBirim(secilenBirim);
				receteIlac.setDoz(iDoz);
				receteIlac.setPeriyot(iPeriyot);
				receteIlac.setTaniId(iTaniId);
				receteIlac.setTaniAdi(arrTani[1]);

				tvReceteIlaclari.getItems().add(receteIlac);
				// tvReceteIlaclari.setItem(dao.HastalariCek());
			} catch (Exception e) {

			}
		}

	}

	@FXML public void btnReceteKaydetOnAction(ActionEvent event) throws SQLException {
		Validasyon.ValCssBorderNormal(txtHastaAdi);

		String hastaAdi = txtHastaAdi.getText();

		if (hastaAdi.length() == 0) {
			Validasyon.ValAlertRequired(txtHastaAdi, "Bo� b�rak�lamaz!");
		} else if (tvReceteIlaclari.getItems().size() == 0) {
			String title = "�LA� B�LG�S� YOK";
			String header = "�la� Bilgisi Eklemelisiniz!";
			String content = "Re�ete olu�turmak i�in en az bir ila� bilgisi eklemelisiniz!";
			Uyari.Hatali(title, header, content);
		} else {
			int iHastaId = Integer.parseInt(hastaAdi.substring(0, hastaAdi.indexOf(" |")).trim());
			// Hasta ID si ve �u an�n tarihiyle Re�ete olu�tur

			// Re�eteID sini ila�lara ekle
			Recete recete = new Recete();
			recete.setHastaId(iHastaId);
			recete = daoRecete.ReceteKaydet(recete);
			// Ba�ar�yla eklendi de ve kapat
			for (Object item : tvReceteIlaclari.getItems()) {
				ReceteIlac ri = (ReceteIlac) item;
				ri.setReceteId(recete.getId());
				daoReceteIlac.ReceteIlacKaydet(ri);
			}
			String header = "Re�ete �la� Kay�d� Ba�ar�l�";
			String content = "Re�etenin kay�d� ba�ar�yla tamamland�!";

			Uyari.Basarili(header, content);
			((Stage) btnReceteKaydet.getScene().getWindow()).close();
		}
	}

	@FXML public void btnSeciliIlaciSilOnAction(ActionEvent event) {

		int selectedIndex = tvReceteIlaclari.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			tvReceteIlaclari.getItems().remove(selectedIndex);
		} else {
			String title = "SE��L� KAYIT YOK";
			String header = "�LA� S�L�NEMED�";
			String content = "Silmek istedi�ini ilac� se�erek tekrar deneyin";
			Uyari.Hatali(title, header, content);
		}
	}

}
