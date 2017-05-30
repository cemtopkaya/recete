package uygulama;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HastaStage
{
	static final String HASTA_FXML = "/resources/fxml/Hasta.fxml";
	static final String HASTA_FXML_CSS = "/resources/styles/application.css";
	static final String WINDOW_TITLE = "HASTALAR...";
	
	public static void ShowStage(Stage _stage) throws Exception {
		Stage stage = new Stage();
		if (_stage != null) {
			stage = _stage;
		}
		
		Parent root = FXMLLoader.load(HastaStage.class.getResource(HASTA_FXML));

		// Stage �st�nde g�r�necek sahneyi olu�turuyoruz
		Scene scene = new Scene(root);
		// CSS bilgisini sahneye ekliyoruz
		String css = HastaStage.class.getResource(HASTA_FXML_CSS).toExternalForm();
		scene.getStylesheets().add(css);
		stage.setScene(scene);
		stage.setTitle(WINDOW_TITLE);
		//stage.setResizable(false);
		// Maximize edilemesin
		stage.initStyle(StageStyle.UTILITY);
		stage.show();
	}

}
