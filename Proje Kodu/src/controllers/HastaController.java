
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
	 * Node eðer kökten gizlenmek isteniyorsa, ya da gösterilmek isteniyorsa
	 * kullanýlýr. Parent layout'ýn görünürlüðü false olduðunda kapladýðý yerin
	 * hesaplama dýþýnda tutulmasýný istiyorsak, manage edilmeyecek diye
	 * iþaretlememiz gerekiyor. Bunun için node tipindeki bileþenin managed
	 * özelliðini false yapmalýyýz.
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
	 * Hasta eklemek istediðimizde, ekleme gridinin görüntülenmesini saðlar!.
	 * 
	 * Parent layout'ýn görünürlüðü false olduðunda kapladýðý yerin hesaplama
	 * dýþýnda tutulmasýný istiyorsak, manage edilmeyecek diye iþaretlememiz
	 * gerekiyor. Bunun için node tipindeki bileþenin managed özelliðini false
	 * yapmalýyýz. {@code nodeTipindekiBilesen.setManaged(false) }
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
			// Hasta seçilmiþse Hasta Bilgilerini düzenle modunda göstereceðiz
			if (!vboxHastaBilgileri.isVisible()) {
				NodeGizleGoster(vboxHastaBilgileri, true);
			}
			NodeGizleGoster(gridHastaEkle, false);
			NodeGizleGoster(lblNotlarGoruntule, false);
			NodeGizleGoster(gridHastaDuzenle, true);

			// Hasta bilgilerini controllere baðlýyoruz
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
			String header = "Sistemde Kayýtlý Olmayan Hasta";
			String content = "Hasta sistemde kayýtlý deðil! Silme iþlemine devam edilemez.";
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
		// Önce gui'den bilgieri deðiþkenelere TRIM leyerek alýyoruz.
		String adi = txtAdi.getText().trim();
		String soyadi = txtSoyadi.getText().trim();
		String tcno = txtTCNo.getText().trim();
		String notlar = txtNotlar.getText().trim();
		LocalDate dtarihi = dpDogumTarihi.getValue();

		// Validasyon öncesinde tüm bileþenleri normale çekiyoruz
		Validasyon.ValCssBorderNormal(txtAdi);
		Validasyon.ValCssBorderNormal(txtSoyadi);
		Validasyon.ValCssBorderNormal(txtTCNo);
		dpDogumTarihiGuncelle.setStyle("-fx-border-style: solid inside;-fx-border-width: 0;-fx-border-color: red;");

		// Eðer TC NO içinde rakamdan baþka karakter varsa o karakterleri
		// kaldýrýyoruz.
		tcno = tcno.replaceAll("[^\\d]", "").trim();
		// 11 karakterden fazlaysa TCNoyu 11 karaktere çek
		tcno = tcno.substring(0, tcno.length() > 11 ? 11 : tcno.length());
		// sadece rakam kalmýþ haliyle TCNo yu bileþene tekrar set ediyoruz.
		txtTCNo.setText(tcno);

		if (adi.length() < 3) {
			Validasyon.ValAlertRequired(txtAdi, "Hasta adý 3 karakterden az olamaz!");
		} else if (soyadi.length() < 3) {
			Validasyon.ValAlertRequired(txtSoyadi, "Hasta soyadý 3 karakterden az olamaz!");
		} else if (tcno.length() < 11) {
			Validasyon.ValAlertRequired(txtTCNo, "TC No 11 Rakamdan az olamaz!");
		} else if (dpDogumTarihi.getValue() == null) {
			dpDogumTarihiGuncelle.setStyle("-fx-border-style: solid inside;-fx-border-width: 1;-fx-border-color: red;");
			dpDogumTarihiGuncelle.requestFocus();
			String title = "VALÝDASYON HATASI";
			String header = "DOÐUM TARÝHÝ GEREKLÝ";
			String content = "Doðum tarihini bilgisini dolu göndermelisiniz!";
			Uyari.Hatali(title, header, content);
		} else {

			try {
				// Sistemde bu hasta kayýtlý mý kontrolü yapýyoruz!
				Hasta eskiHasta = dao.HastaGetir(tcno);
				// Hasta döndüyse sistemde kayýtlý mesajý göster
				// null ise hastayý sisteme kaydet
				if (eskiHasta != null) {
					String title = "HASTA MEVCUT";
					String header = "Hasta kayýtlý";
					String content = "Ayný TC numaralý hasta sistemde kayýtlý";
					Uyari.Hatali(title, header, content);
				} else {
					// Hasta bulunamadýðý için sisteme kayýt edebiliriz.
					Hasta hasta = new Hasta(adi, soyadi, tcno, dtarihi, notlar);
					hasta = dao.HastaKaydet(hasta);
					if (hasta.getId() > 0) {
						String header = "HASTA KAYIT EDÝLDÝ";
						String content = "Hasta kayýdý baþarýyla tamamlandý.";
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
			Validasyon.ValAlertRequired(txtAdiGuncelle, "Adý 3 Karakterden az olamaz!");
		} else if (soyadi.trim().length() < 3) {
			Validasyon.ValAlertRequired(txtSoyadiGuncelle, "Soyadý 2 Karakterden az olamaz!");
		} else if (tCNo.length() != 11) {
			Validasyon.ValAlertRequired(txtTCNoGuncelle, "TC Numarasý 11 rakamdan az ya da çok olamaz!");
		} else if (dTarihi == null) {
			dpDogumTarihiGuncelle.setStyle("-fx-border-style: solid inside;-fx-border-width: 1;-fx-border-color: red;");
			dpDogumTarihiGuncelle.requestFocus();
		} else {
			try {
				Hasta hasta = new Hasta(secilenHasta.getId(), adi, soyadi, tCNo, dTarihi, notlar);
				dao.HastaGuncelle(hasta);
				HastaBilgileriniGizle();
				HastalariCekBagla();
				String header = "Hasta Güncelleme";
				String content = "Hasta bilgileri baþarýyla güncellendi.";
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
		dtarihiCol.setMinWidth(80);
		dtarihiCol.setCellValueFactory(new PropertyValueFactory("DTarihi"));

		// Önce tablo içindeki kolonlarýn hepsini siliyoruz
		tvHastalar.getColumns().clear();
		// Yukarýda oluþturduðumuz kolonlarý TableView'a baðlýyoruz
		tvHastalar.getColumns().addAll(idCol, adiCol, soyadiCol, tcnoCol, dtarihiCol);
		// DB'den çektiðimiz hasta listesini TableView'a satýr satýr baðlýyoruz
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
