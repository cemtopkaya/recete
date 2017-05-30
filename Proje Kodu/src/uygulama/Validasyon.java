package uygulama;

import java.util.Collections;

import javafx.collections.ObservableList;
import javafx.scene.control.TextField;

public class Validasyon
{

	public Validasyon() {
	}

	static public void ValCssBorderNormal(TextField tf) {
		ObservableList<String> styleClass = tf.getStyleClass();
		if (styleClass.contains("error")) {
			styleClass.removeAll(Collections.singleton("error"));
		}
	}

	static public void ValAlertRequired(TextField tf, String mesaj) {
		System.out.println("Doðrulama Mesajý: " + mesaj);
		String title = "DOÐRULAMA HATASI";
		String header = "Girilen deðer yeterli deðil!";
		String content = mesaj;
		Uyari.Hatali(title, header, content);
		ValCssBorderRed(tf);
		tf.requestFocus();
	}

	static public void ValCssBorderRed(TextField tf) {
		ObservableList<String> styleClass = tf.getStyleClass();
		if (!styleClass.contains("error")) {
			styleClass.add("error");
		}
	}
}
