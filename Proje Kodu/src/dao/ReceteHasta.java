package dao;

import java.time.LocalDate;

public class ReceteHasta {
	public int Id;
	private String Adi;
	public String TCNo;
	public LocalDate Tarihi;
	public Boolean Verildi;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getTCNo() {
		return TCNo;
	}

	public void setTCNo(String tCNo) {
		TCNo = tCNo;
	}

	public LocalDate getTarihi() {
		return Tarihi;
	}

	public void setTarihi(LocalDate tarihi) {
		Tarihi = tarihi;
	}

	public Boolean isVerildi() {
		return Verildi;
	}

	public void setVerildi(Boolean verildi) {
		Verildi = verildi;
	}

	public String getAdi() {
		return Adi;
	}

	public void setAdi(String adi) {
		Adi = adi;
	}
}
