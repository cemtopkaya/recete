package uygulama;

import java.sql.Connection;
import java.sql.DriverManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		 HastaStage.ShowStage(stage);
		// ReceteStage.ShowStage(stage);
		// HekimStage.ShowStage(stage);
		// EczaciStage.ShowStage(null);
		//KullaniciGirisStage.ShowStage(stage);
	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}
}