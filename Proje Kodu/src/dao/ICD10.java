package dao;

public class ICD10
{
	int Id;
	String Adi;
	String Kodu;

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

	public String getKodu() {
		return Kodu;
	}

	public void setKodu(String kodu) {
		Kodu = kodu;
	}
	
	public ICD10(int id, String adi, String kodu) {
		super();
		Id = id;
		Adi = adi;
		Kodu = kodu;
	}

}