package dao;

import java.time.LocalDate;
import java.sql.Date;


public class Hasta
{
	private int Id;
	private String Adi;
	private String Soyadi;
	private String TCNo;
	private LocalDate DTarihi;
	private String Notlar;

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		this.Id = id;
	}

	public String getAdi() {
		return Adi;
	}

	public void setAdi(String adi) {
		Adi = adi;
	}

	public String getSoyadi() {
		return Soyadi;
	}

	public void setSoyadi(String soyadi) {
		Soyadi = soyadi;
	}

	public String getTCNo() {
		return TCNo;
	}

	public void setTCNo(String tCNo) {
		TCNo = tCNo;
	}

	public String getNotlar() {
		return Notlar;
	}

	public void setNotlar(String notlar) {
		Notlar = notlar;
	}

public LocalDate getDTarihi() {
		//public Date getDTarihi() {
		return DTarihi;
	}

	public void setDTarihi(LocalDate dTarihi) {
		//public void setDTarihi(Date dTarihi) {
		DTarihi = dTarihi;
	}

	public Hasta() {
	}

public Hasta(int id, String adi, String soyadi, String tCNo, LocalDate dTarihi, String notlar) {
		//public Hasta(int id, String adi, String soyadi, String tCNo, Date dTarihi, String notlar) {
		Id = id;
		Adi = adi;
		Soyadi = soyadi;
		TCNo = tCNo;
		DTarihi = dTarihi;
		Notlar = notlar;
	}

	public Hasta(String adi, String soyadi, String tCNo, LocalDate dTarihi, String notlar) {
		//public Hasta(String adi, String soyadi, String tCNo, Date dTarihi, String notlar) {
		Adi = adi;
		Soyadi = soyadi;
		TCNo = tCNo;
		DTarihi = dTarihi;
		Notlar = notlar;
	}

}