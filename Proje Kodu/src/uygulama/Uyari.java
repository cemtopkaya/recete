package uygulama;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Uyar� static metotlar i�erek bir s�n�ft�r. Bu sayede her uyar� i�in tekrar pencere olu�turma kodunu yazmak gerekmez.
 * Bu s�n�f�n ilgili metotlar� �a��r�larak h�zl�ca ekranda uyar� mesajlar� g�sterilebilir.
 * @author 22556282
 *
 */
public class Uyari {
	/**
	 * �stisna mesajlar�n� ekran g�sterir. Sadece istisna nesnesini vermek yeterlidir.
	 * @param e
	 */
	static public void Istisna(Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("�ST�SNA OLU�TU");
		alert.setHeaderText("��leminiz yerine getirilirken istisna olu�tu!");
		alert.setContentText(e.getMessage());
		alert.showAndWait();
	}

	/**
	 * Ba�ar�l� sonu�lar� ALERT i�inde g�steremek i�in kullan�l�r.
	 * 
	 * @param header
	 *            ��lemin ba�l�k bilgisi
	 * @param content
	 *            ��lem detaylar�
	 */
	public static void Basarili(String header, String content) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("��LEM�N�Z TAMAMLANDI");
		alert.setHeaderText(header.isEmpty() ? "��lem tamamland�" : header);
		alert.setContentText(content.isEmpty() ? "��leminiz ba�ar�yla yerine getirildi" : content);
		alert.showAndWait();

	}

	/**
	 * Hatal� bildirimler i�in kullan�l�r.
	 * 
	 * @param title
	 *            Mesaj penceresini ba�l���
	 * @param header
	 *            Hataya dair konu
	 * @param content
	 *            Hatan�n i�eri�i
	 */
	public static void Hatali(String title, String header, String content) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
}
