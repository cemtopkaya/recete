package uygulama;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class IlacStage
{
	static final String ILAC_FXML = "/resources/fxml/Hasta.fxml";
	static final String ILAC_FXML_CSS = "/resources/styles/application.css";
	static final String WINDOW_TITLE = "ILAÇLAR...";
	
	public static void ShowStage(Stage _stage) throws Exception {
		Stage stage = new Stage();
		if (_stage != null) {
			stage = _stage;
		}
		Parent root = FXMLLoader.load(HastaStage.class.getResource(ILAC_FXML));

		// Stage üstünde görünecek sahneyi oluþturuyoruz
		Scene scene = new Scene(root);
		// CSS bilgisini sahneye ekliyoruz
		String css = HastaStage.class.getResource(ILAC_FXML_CSS).toExternalForm();
		scene.getStylesheets().add(css);
		stage.setScene(scene);
		stage.setTitle(WINDOW_TITLE);
		//stage.setResizable(false);
		// Maximize edilemesin
		stage.initStyle(StageStyle.UTILITY);
		stage.show();
	}

}
