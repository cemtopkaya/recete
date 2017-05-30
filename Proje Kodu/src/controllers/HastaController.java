
package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import dao.Hasta;
import dao.HastaDAO;
import istisna.HastaBulunamadi;
import istisna.HastaKayitli;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uygulama.Uyari;
import uygulama.Validasyon;

public class HastaController implements Initializable {
	Stage stage;

	public void setStage(Stage s) {
		this.stage = s;
	}

	@FXML
	Button toolbarBtnHastaEkle;
	@FXML
	Button toolbarBtnHastaDuzenle;
	@FXML
	Button toolbarBtnHastaSil;
	@FXML
	TextField txtAdi;
	@FXML
	TextField txtSoyadi;
	@FXML
	TextField txtTCNo;
	@FXML
	Button btnKaydet;
	@FXML
	DatePicker dpDogumTarihi;
	@FXML
	Button btnKaydetGuncelle;
	@FXML
	VBox vboxHastaBilgileri;
	@FXML
	GridPane gridHastaEkle;
	@FXML
	GridPane gridHastaDuzenle;
	@FXML
	Button btnHastaKaydet;
	@FXML
	Button btnHastaGuncelle;
	@FXML
	TextField txtTCNoGuncelle;
	@FXML
	TextField txtSoyadiGuncelle;
	@FXML
	TextField txtAdiGuncelle;
	@FXML
	TextArea txtNotlar;
	@FXML
	TextArea txtNotlarGuncelle;
	@FXML
	DatePicker dpDogumTarihiGuncelle;
	@FXML
	Button btnHastaKaydetIptal;
	@FXML
	Button toolbarBtnTazele;
	@FXML
	TableView<Hasta> tvHastalar;
	@FXML
	HBox hboxHastaBilgileri;
	@FXML
	Label lblNotlarGoruntule;

	HastaDAO dao;
	Hasta secilenHasta = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			dao = new HastaDAO();
			NodeGizleGoster(vboxHastaBilgileri, false);
			NodeGizleGoster(gridHastaDuzenle, false);
			NodeGizleGoster(gridHastaEkle, false);
			NodeGizleGoster(lblNotlarGoruntule, false);
			toolbarBtnTazeleOnAction(null);
		} catch (SQLException e) {
			e.printStackTrace();
			Uyari.Istisna(e);
		}
	}

	/**
	 * Node e�er k�kten gizlenmek isteniyorsa, ya da g�sterilmek isteniyorsa
	 * kullan�l�r. Parent layout'�n g�r�n�rl��� false oldu�unda kaplad��� yerin
	 * hesaplama d���nda tutulmas�n� istiyorsak, manage edilmeyecek diye
	 * i�aretlememiz gerekiyor. Bunun i�in node tipindeki bile�enin managed
	 * �zelli�ini false yapmal�y�z.
	 * {@code nodeTipindekiBilesen.setManaged(false) }
	 */
	void NodeGizleGoster(Node node, boolean goster) {
		node.setVisible(goster);
		if (goster) {
			if (node instanceof GridPane || node instanceof VBox)
				node.managedProperty().bindBidirectional(node.visibleProperty());
			else
				node.managedProperty().bind(node.visibleProperty());
		} else {
			if (!(node instanceof Label)) {
				node.setManaged(false);
			}
		}
	}

	/**
	 * Hasta eklemek istedi�imizde, ekleme gridinin g�r�nt�lenmesini sa�lar!.
	 * 
	 * Parent layout'�n g�r�n�rl��� false oldu�unda kaplad��� yerin hesaplama
	 * d���nda tutulmas�n� istiyorsak, manage edilmeyecek diye i�aretlememiz
	 * gerekiyor. Bunun i�in node tipindeki bile�enin managed �zelli�ini false
	 * yapmal�y�z. {@code nodeTipindekiBilesen.setManaged(false) }
	 */
	@FXML
	public void toolbarBtnHastaEkleOnAction(ActionEvent event) {
		if (!vboxHastaBilgileri.isVisible()) {
			NodeGizleGoster(vboxHastaBilgileri, true);
		}
		NodeGizleGoster(gridHastaDuzenle, false);
		NodeGizleGoster(gridHastaEkle, true);
	}

	@FXML
	public void toolbarBtnHastaDuzenleOnAction(ActionEvent event) {
		if (secilenHasta != null) {
			// Hasta se�ilmi�se Hasta Bilgilerini d�zenle modunda g�sterece�iz
			if (!vboxHastaBilgileri.isVisible()) {
				NodeGizleGoster(vboxHastaBilgileri, true);
			}
			NodeGizleGoster(gridHastaEkle, false);
			NodeGizleGoster(lblNotlarGoruntule, false);
			NodeGizleGoster(gridHastaDuzenle, true);

			// Hasta bilgilerini controllere ba�l�yoruz
			txtAdiGuncelle.setText(secilenHasta.getAdi());
			txtSoyadiGuncelle.setText(secilenHasta.getSoyadi());
			txtTCNoGuncelle.setText(secilenHasta.getTCNo());
			dpDogumTarihiGuncelle.setValue(secilenHasta.getDTarihi());
			txtNotlarGuncelle.setText(secilenHasta.getNotlar());
		}
	}

	@FXML
	public void toolbarBtnSilOnAction(ActionEvent event) {

		try {
			boolean bSilindi = dao.HastaSil(secilenHasta.getId());
			if (bSilindi)
				HastalariCekBagla();
		} catch (HastaBulunamadi e) {
			String title = "HASTA BULUNAMADI";
			String header = "Sistemde Kay�tl� Olmayan Hasta";
			String content = "Hasta sistemde kay�tl� de�il! Silme i�lemine devam edilemez.";
			Uyari.Hatali(title, header, content);
		} catch (SQLException e) {
			Uyari.Istisna(e);
		} catch (Exception e) {
			Uyari.Istisna(e);
		}
	}

	@FXML
	public void toolbarBtnTazeleOnAction(ActionEvent event) {
		try {
			HastalariCekBagla();
		} catch (SQLException e) {
			e.printStackTrace();
			Uyari.Istisna(e);
		}
	}

	@FXML
	public void btnHastaKaydetOnAction() {
		// �nce gui'den bilgieri de�i�kenelere TRIM leyerek al�yoruz.
		String adi = txtAdi.getText().trim();
		String soyadi = txtSoyadi.getText().trim();
		String tcno = txtTCNo.getText().trim();
		String notlar = txtNotlar.getText().trim();
		LocalDate dtarihi = dpDogumTarihi.getValue();

		// Validasyon �ncesinde t�m bile�enleri normale �ekiyoruz
		Validasyon.ValCssBorderNormal(txtAdi);
		Validasyon.ValCssBorderNormal(txtSoyadi);
		Validasyon.ValCssBorderNormal(txtTCNo);
		dpDogumTarihiGuncelle.setStyle("-fx-border-style: solid inside;-fx-border-width: 0;-fx-border-color: red;");

		// E�er TC NO i�inde rakamdan ba�ka karakter varsa o karakterleri
		// kald�r�yoruz.
		tcno = tcno.replaceAll("[^\\d]", "").trim();
		// 11 karakterden fazlaysa TCNoyu 11 karaktere �ek
		tcno = tcno.substring(0, tcno.length() > 11 ? 11 : tcno.length());
		// sadece rakam kalm�� haliyle TCNo yu bile�ene tekrar set ediyoruz.
		txtTCNo.setText(tcno);

		if (adi.length() < 3) {
			Validasyon.ValAlertRequired(txtAdi, "Hasta ad� 3 karakterden az olamaz!");
		} else if (soyadi.length() < 3) {
			Validasyon.ValAlertRequired(txtSoyadi, "Hasta soyad� 3 karakterden az olamaz!");
		} else if (tcno.length() < 11) {
			Validasyon.ValAlertRequired(txtTCNo, "TC No 11 Rakamdan az olamaz!");
		} else if (dpDogumTarihi.getValue() == null) {
			dpDogumTarihiGuncelle.setStyle("-fx-border-style: solid inside;-fx-border-width: 1;-fx-border-color: red;");
			dpDogumTarihiGuncelle.requestFocus();
			String title = "VAL�DASYON HATASI";
			String header = "DO�UM TAR�H� GEREKL�";
			String content = "Do�um tarihini bilgisini dolu g�ndermelisiniz!";
			Uyari.Hatali(title, header, content);
		} else {

			try {
				// Sistemde bu hasta kay�tl� m� kontrol� yap�yoruz!
				Hasta eskiHasta = dao.HastaGetir(tcno);
				// Hasta d�nd�yse sistemde kay�tl� mesaj� g�ster
				// null ise hastay� sisteme kaydet
				if (eskiHasta != null) {
					String title = "HASTA MEVCUT";
					String header = "Hasta kay�tl�";
					String content = "Ayn� TC numaral� hasta sistemde kay�tl�";
					Uyari.Hatali(title, header, content);
				} else {
					// Hasta bulunamad��� i�in sisteme kay�t edebiliriz.
					Hasta hasta = new Hasta(adi, soyadi, tcno, dtarihi, notlar);
					hasta = dao.HastaKaydet(hasta);
					if (hasta.getId() > 0) {
						String header = "HASTA KAYIT ED�LD�";
						String content = "Hasta kay�d� ba�ar�yla tamamland�.";
						Uyari.Basarili(header, content);
						HastalariCekBagla();
						HastaBilgileriniGizle();
					}
				}
			} catch (SQLException e) {
				Uyari.Istisna(e);
			} catch (Exception e) {
				Uyari.Istisna(e);
			}
		}
	}

	@FXML
	public void btnHastaKaydetIptalOnAction() {
		HastaBilgileriniGizle();
	}

	@FXML
	public void btnHastaGuncelleOnAction(ActionEvent event) {
		String adi = txtAdiGuncelle.getText().trim();
		String soyadi = txtSoyadiGuncelle.getText().trim();
		String tCNo = txtTCNoGuncelle.getText().trim();
		LocalDate dTarihi = dpDogumTarihiGuncelle.getValue();
		String notlar = txtNotlarGuncelle.getText().trim();

		Validasyon.ValCssBorderNormal(txtAdiGuncelle);
		Validasyon.ValCssBorderNormal(txtSoyadiGuncelle);
		Validasyon.ValCssBorderNormal(txtTCNoGuncelle);
		dpDogumTarihiGuncelle.setStyle("-fx-border-style: solid inside;-fx-border-width: 0;-fx-border-color: red;");

		tCNo = tCNo.substring(0, tCNo.length() > 11 ? 11 : tCNo.length()).replaceAll("[^\\d]", "");
		txtTCNoGuncelle.setText(tCNo);

		if (adi.trim().length() < 3) {
			Validasyon.ValAlertRequired(txtAdiGuncelle, "Ad� 3 Karakterden az olamaz!");
		} else if (soyadi.trim().length() < 3) {
			Validasyon.ValAlertRequired(txtSoyadiGuncelle, "Soyad� 2 Karakterden az olamaz!");
		} else if (tCNo.length() != 11) {
			Validasyon.ValAlertRequired(txtTCNoGuncelle, "TC Numaras� 11 rakamdan az ya da �ok olamaz!");
		} else if (dTarihi == null) {
			dpDogumTarihiGuncelle.setStyle("-fx-border-style: solid inside;-fx-border-width: 1;-fx-border-color: red;");
			dpDogumTarihiGuncelle.requestFocus();
		} else {
			try {
				Hasta hasta = new Hasta(secilenHasta.getId(), adi, soyadi, tCNo, dTarihi, notlar);
				dao.HastaGuncelle(hasta);
				HastaBilgileriniGizle();
				HastalariCekBagla();
				String header = "Hasta G�ncelleme";
				String content = "Hasta bilgileri ba�ar�yla g�ncellendi.";
				Uyari.Basarili(header, content);
			} catch (Exception e) {
				Uyari.Istisna(e);
			}
		}
	}

	@FXML
	public void tvHastalarOnMousePressed(MouseEvent event) {
		if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
			Node targetNode = (Node) event.getTarget();
			Node parentNode = targetNode.getParent();
			TableRow row = null;
			if (parentNode instanceof TableRow) {
				row = (TableRow) parentNode;
			} else {
				// clicking on text part
				if (null != parentNode.getParent() && parentNode.getParent() instanceof TableRow)
					row = (TableRow) parentNode.getParent();
			}
			if (row != null && row.getItem() instanceof Hasta) {
				secilenHasta = (Hasta) row.getItem();
				lblNotlarGoruntule.setText(secilenHasta.getNotlar());
				AciklamaGoster();
			}
		}
	}

	void HastaBilgileriniGizle() {
		NodeGizleGoster(vboxHastaBilgileri, false);
		NodeGizleGoster(gridHastaDuzenle, false);
		NodeGizleGoster(gridHastaEkle, false);
		NodeGizleGoster(lblNotlarGoruntule, false);
	}

	void HastalariCekBagla() throws SQLException {
		// id
		TableColumn<Hasta, Integer> idCol = new TableColumn<>("Id");
		idCol.setMinWidth(50);
		idCol.setCellValueFactory(new PropertyValueFactory("id"));

		// Adi
		TableColumn<Hasta, Integer> adiCol = new TableColumn<>("Ad�");
		adiCol.setMinWidth(100);
		adiCol.setCellValueFactory(new PropertyValueFactory("Adi"));

		// Soyadi
		TableColumn<Hasta, Integer> soyadiCol = new TableColumn<>("Soyad�");
		soyadiCol.setMinWidth(100);
		soyadiCol.setCellValueFactory(new PropertyValueFactory("Soyadi"));

		// TCNo
		TableColumn<Hasta, Integer> tcnoCol = new TableColumn<>("TCNo");
		tcnoCol.setMinWidth(100);
		tcnoCol.setCellValueFactory(new PropertyValueFactory("TCNo"));

		// DTarihi
		TableColumn<Hasta, Integer> dtarihiCol = new TableColumn<>("Do�um Tarihi");
		dtarihiCol.setMinWidth(80);
		dtarihiCol.setCellValueFactory(new PropertyValueFactory("DTarihi"));

		// �nce tablo i�indeki kolonlar�n hepsini siliyoruz
		tvHastalar.getColumns().clear();
		// Yukar�da olu�turdu�umuz kolonlar� TableView'a ba�l�yoruz
		tvHastalar.getColumns().addAll(idCol, adiCol, soyadiCol, tcnoCol, dtarihiCol);
		// DB'den �ekti�imiz hasta listesini TableView'a sat�r sat�r ba�l�yoruz
		tvHastalar.setItems(dao.HastalariCek());
	}

	void AciklamaGoster() {
		if (!vboxHastaBilgileri.isVisible()) {
			NodeGizleGoster(vboxHastaBilgileri, true);
			vboxHastaBilgileri.setMaxHeight(150);
			txtNotlar.setMinWidth(vboxHastaBilgileri.getWidth());
		}
		NodeGizleGoster(gridHastaDuzenle, false);
		NodeGizleGoster(gridHastaEkle, false);
		NodeGizleGoster(lblNotlarGoruntule, true);
	}
}
