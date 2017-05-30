package dao;

public class Kullanici {

	private int Id;
	private String KullaniciAdi;
	private String Sifre;
	private Boolean Doktor;
	private Boolean Eczaci;
	
	
	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getKullaniciAdi() {
		return KullaniciAdi;
	}

	public void setKullaniciAdi(String kullaniciAdi) {
		KullaniciAdi = kullaniciAdi;
	}

	public String getSifre() {
		return Sifre;
	}

	public void setSifre(String sifre) {
		Sifre = sifre;
	}

	public Boolean isDoktor() {
		return Doktor;
	}

	public void setDoktor(Boolean doktor) {
		Doktor = doktor;
	}

	public Boolean isEczaci() {
		return Eczaci;
	}

	public void setEczaci(Boolean eczaci) {
		Eczaci = eczaci;
	}
	
}
