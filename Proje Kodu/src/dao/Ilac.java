package dao;

public class Ilac
{
	private int Id;
	private String Adi;
	private String FirmaAdi;
	private String Barkodu;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getAdi() {
		return Adi;
	}
	public void setAdi(String adi) {
		Adi = adi;
	}
	public String getFirmaAdi() {
		return FirmaAdi;
	}
	public void setFirmaAdi(String firmaAdi) {
		FirmaAdi = firmaAdi;
	}
	public String getBarkodu() {
		return Barkodu;
	}
	public void setBarkodu(String barkodu) {
		Barkodu = barkodu;
	}
	public Ilac(int id, String adi, String firmaAdi, String barkodu) {
		super();
		Id = id;
		Adi = adi;
		FirmaAdi = firmaAdi;
		Barkodu = barkodu;
	}

}