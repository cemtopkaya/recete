package dao;

import java.time.LocalDate;

public class Recete
{
	private int Id;
	private LocalDate Tarihi;
	private int HastaId;
	private boolean Verildi;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public LocalDate getTarihi() {
		return Tarihi;
	}
	public void setTarihi(LocalDate tarihi) {
		Tarihi = tarihi;
	}
	public int getHastaId() {
		return HastaId;
	}
	public void setHastaId(int hastaId) {
		HastaId = hastaId;
	}
	public boolean isVerildi() {
		return Verildi;
	}
	public void setVerildi(boolean verildi) {
		Verildi = verildi;
	}

}
