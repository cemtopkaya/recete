package controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import uygulama.*;
import dao.*;

public class GirisController implements Initializable {

	Stage stage;

	public void setStage(Stage s) {
		this.stage = s;
	}

	@FXML Label lblMesaj;
	@FXML TextField txtKullaniciAdi;
	@FXML TextField txtSifre;
	@FXML Button btnGiris;

	KullaniciDAO daoKullanici;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			daoKullanici = new KullaniciDAO();
		} catch (SQLException e) {
			e.printStackTrace();
			Uyari.Istisna(e);
		}
	}

	@FXML
	private void btnGirisAction(ActionEvent event) throws Exception {
		String kullaniciAdi = txtKullaniciAdi.getText().trim();
		String sifre = txtSifre.getText().trim();
		
		// Oluþturalacak Kullanici DB den çekilecek
		Kullanici kullanici = daoKullanici.KullaniciGetir(kullaniciAdi, sifre);

		if (kullanici != null) {
			
			Window window = ((Node) event.getSource()).getScene().getWindow();
			if (kullanici.isDoktor()) {
				HekimStage.ShowStage(null, true);			
				window.hide();
			}
			else if(kullanici.isEczaci()){
				EczaciStage.ShowStage(null, true);
				window.hide();
			}else{
				String title = "HATALI GÝRÝÞ";
				String header = "KULLANICI HATALI";
				String content = "Kullanýcý için hekim ya da eczaci bilgisi ayarlanmamýþ!";
				Uyari.Hatali(title, header,content);
			}
			
		} else {
			lblMesaj.setText("Hatalý bilgilerle girmeye çalýþtýnýz!");
		}


	}

}
