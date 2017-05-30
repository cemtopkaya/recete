package uygulama;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class KullaniciGirisStage
{
	// sabit deðiþkenler STATIC ve FINAL olarak set edilirse yazým hatalarý yapýlmamýþ olur.
	static final String FXML = "/resources/fxml/Giris.fxml";
	static final String FXML_CSS = "/resources/styles/application.css";
	static final String WINDOW_TITLE = "KULLANICI GÝRÝÞÝ";
	
	public static void ShowStage(Stage _stage) throws Exception
	{
		Stage stage = new Stage();
		if (_stage != null) {
			stage = _stage;
		}
		
		Parent root = FXMLLoader.load(KullaniciGirisStage.class.getResource(FXML));

		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle(WINDOW_TITLE);
		// stage.setResizable(false);
		// Maximize edilemesin
		stage.initStyle(StageStyle.UTILITY);
		stage.show();
	}
}
