package uygulama;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Uyarý static metotlar içerek bir sýnýftýr. Bu sayede her uyarý için tekrar pencere oluþturma kodunu yazmak gerekmez.
 * Bu sýnýfýn ilgili metotlarý çaðýrýlarak hýzlýca ekranda uyarý mesajlarý gösterilebilir.
 * @author 22556282
 *
 */
public class Uyari {
	/**
	 * Ýstisna mesajlarýný ekran gösterir. Sadece istisna nesnesini vermek yeterlidir.
	 * @param e
	 */
	static public void Istisna(Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ÝSTÝSNA OLUÞTU");
		alert.setHeaderText("Ýþleminiz yerine getirilirken istisna oluþtu!");
		alert.setContentText(e.getMessage());
		alert.showAndWait();
	}

	/**
	 * Baþarýlý sonuçlarý ALERT içinde gösteremek için kullanýlýr.
	 * 
	 * @param header
	 *            Ýþlemin baþlýk bilgisi
	 * @param content
	 *            Ýþlem detaylarý
	 */
	public static void Basarili(String header, String content) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("ÝÞLEMÝNÝZ TAMAMLANDI");
		alert.setHeaderText(header.isEmpty() ? "Ýþlem tamamlandý" : header);
		alert.setContentText(content.isEmpty() ? "Ýþleminiz baþarýyla yerine getirildi" : content);
		alert.showAndWait();

	}

	/**
	 * Hatalý bildirimler için kullanýlýr.
	 * 
	 * @param title
	 *            Mesaj penceresini baþlýðý
	 * @param header
	 *            Hataya dair konu
	 * @param content
	 *            Hatanýn içeriði
	 */
	public static void Hatali(String title, String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
