package uygulama;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ReceteStage
{

	static final String RECETE_FXML = "/resources/fxml/Recete.fxml";
	static final String RECETE_FXML_CSS = "/resources/styles/application.css";
	static final String WINDOW_TITLE = "REÇETE...";
	
	public static void ShowStage(Stage _stage) throws Exception {
		Stage stage = new Stage();
		if (_stage != null) {
			stage = _stage;
		}
		
		Parent root = FXMLLoader.load(HastaStage.class.getResource(RECETE_FXML));

		// Stage üstünde görünecek sahneyi oluþturuyoruz
		Scene scene = new Scene(root);
		// CSS bilgisini sahneye ekliyoruz
		String css = HastaStage.class.getResource(RECETE_FXML_CSS).toExternalForm();
		scene.getStylesheets().add(css);
		stage.setScene(scene);
		stage.setTitle(WINDOW_TITLE);
		//stage.setResizable(false);
		// Maximize edilemesin
		stage.initStyle(StageStyle.UTILITY);
		stage.show();
	}
}
