package dao;

public class ReceteIlac
{
	private int Id;
	private int ReceteId;
	private int IlacId;
	private String IlacAdi;
	private int TaniId;
	private String TaniAdi;
	private int Doz;
	private int Periyot;
	private String Birim;
	
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public int getReceteId() {
		return ReceteId;
	}
	public void setReceteId(int receteId) {
		ReceteId = receteId;
	}
	public int getIlacId() {
		return IlacId;
	}
	public void setIlacId(int ilacId) {
		IlacId = ilacId;
	}
	public int getTaniId() {
		return TaniId;
	}
	public void setTaniId(int taniId) {
		TaniId = taniId;
	}
	public int getDoz() {
		return Doz;
	}
	public void setDoz(int doz) {
		Doz = doz;
	}
	public int getPeriyot() {
		return Periyot;
	}
	public void setPeriyot(int periyot) {
		Periyot = periyot;
	}
	public String getBirim() {
		return Birim;
	}
	public void setBirim(String birim) {
		Birim = birim;
	}
	public String getIlacAdi() {
		return IlacAdi;
	}
	public void setIlacAdi(String ilacAdi) {
		IlacAdi = ilacAdi;
	}
	public String getTaniAdi() {
		return TaniAdi;
	}
	public void setTaniAdi(String taniAdi) {
		TaniAdi = taniAdi;
	}

}
