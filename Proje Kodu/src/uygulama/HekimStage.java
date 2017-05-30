package uygulama;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HekimStage
{
	static final String FXML = "/resources/fxml/Hekim.fxml";
	static final String FXML_CSS = "/resources/styles/application.css";
	static final String WINDOW_TITLE = "HEKÝM EKRANI...";

	public static void ShowStage(Stage _stage, boolean bTekKalacak) throws Exception {
		Stage stage = new Stage();
		if (_stage != null) {
			stage = _stage;
		}
		
		Parent root = FXMLLoader.load(HekimStage.class.getResource(FXML));

		// Stage üstünde görünecek sahneyi oluþturuyoruz
		Scene scene = new Scene(root);
		// CSS bilgisini sahneye ekliyoruz
		String css = HekimStage.class.getResource(FXML_CSS).toExternalForm();
		scene.getStylesheets().add(css);
		stage.setScene(scene);
		stage.setTitle(WINDOW_TITLE);
		// stage.setResizable(false);
		// Maximize edilemesin
		//stage.initStyle(StageStyle.UTILITY);
		stage.show();
	}

}
