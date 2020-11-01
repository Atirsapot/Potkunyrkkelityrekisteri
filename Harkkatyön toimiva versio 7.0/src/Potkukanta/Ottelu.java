package Potkukanta;

import fi.jyu.mit.ohj2.Mjonot;
import java.io.*;

/**
 * Potkunyrkkeilyrekisterin jäsen joka luo Ottelu olioita
 * 
 * @author topias ja joona
 * @version 7.0
 */
public class Ottelu {

	private int oid; // otteluID
	private String nimi = "oletus";
	private String kotiottelija = "koti";
	private String vierasottelija = "vieras";
	private String kotiseura = "kotiseura";
	private String vierasseura = "vierasseura";
	private String kotipaino = "0";
	private String vieraspaino = "0";
	private boolean kotitulos = false;
	private boolean vierastulos = false;
	private int kjid = 0;
	private int vjid = 0;

	private String ttid = ""; // tulostyyppi

	private String pvm = "2020-01-01";

	// param sNro avulla saadaan uniikki
	// OtteluID lisättäville otteluille.
	private static int sNro = 1;

	public Ottelu(int oid, String nimi, String kotiottelija, String vierasottelija, String kotiseura,
			String vierasseura, String kotipaino, String vieraspaino, int kjid, int vjid, boolean kotitulos,
			boolean vierastulos, String ttid, String pvm) {
		this.oid = oid;
		this.nimi = nimi;
		this.kotiottelija = kotiottelija;
		this.kotiseura = kotiseura;
		this.vierasottelija = vierasottelija;
		this.vierasseura = vierasseura;
		this.kotipaino = kotipaino;
		this.vieraspaino = vieraspaino;
		this.kjid = kjid;
		this.vjid = vjid;
		this.kotitulos = kotitulos;
		this.vierastulos = vierastulos;
		this.ttid = ttid;
		this.pvm = pvm;
	}

	/**
	 * Antaa kullekkin ottelulle oman ID luvun
	 * 
	 * @param nro
	 */
	private void setOtteluID(int nro) {
		oid = nro;
		if (oid >= sNro)
			sNro = oid + 1;
	}

	/**
	 * Palauttaa ottelun tiedot merkkijonona jonka voi tallentaa tiedostoon.
	 * 
	 * @return ottelu | eroteltuna merkkijonona
	 * @example
	 * 
	 *          <pre name="test">
	 *   Ottelu ottelu = new Ottelu();
	 *   ottelu.parse("   1  | testiottelu  |   Topi  | Tessu | JKB ");
	 *   ottelu.toString()    === "1|testiottelu|Topi|Tessu|JKB";
	 *          </pre>
	 */
	@Override
	public String toString() {
		return "" + getOID() + "|" + nimi + "|" + kotiottelija + "|" + vierasottelija + "|" + kotiseura + "|"
				+ vierasseura + "|" + kotipaino + "|" + vieraspaino + "|" + kjid + "|" + vjid + "|" + kotitulos + "|"
				+ vierastulos + "|" + ttid + "|" + pvm;
	}

	/**
	 * Selvitää ottelun tiedot | erotellusta merkkijonosta.
	 * 
	 * @param rivi josta ottelun tiedot otetaan
	 * @example
	 * 
	 *          <pre name="test">
	 *   Ottelu ottelu = new Ottelu();
	 *   ottelu.parse("   1   |  10  |   Kalastus  | 1949 | 22 t ");
	 *   ottelu.parse("   1  | testiottelu  |   Topi  | Tessu | JKB ");
	 *   harrastus.getOID() === 1;
	 *   harrastus.toString()    === === "1|testiottelu|Topi|Tessu|JKB";
	 * 
	 *          </pre>
	 */
	public void parse(String rivi) {
		StringBuffer sb = new StringBuffer(rivi);
		setOtteluID(Mjonot.erota(sb, '|', getOID()));
		nimi = Mjonot.erota(sb, '|', nimi);
		kotiottelija = Mjonot.erota(sb, '|', kotiottelija);
		vierasottelija = Mjonot.erota(sb, '|', vierasottelija);
		kotiseura = Mjonot.erota(sb, '|', kotiseura);
		vierasseura = Mjonot.erota(sb, '|', vierasseura);
		kotipaino = Mjonot.erota(sb, '|', kotipaino);
		vieraspaino = Mjonot.erota(sb, '|', vieraspaino);
		kjid = Mjonot.erota(sb, '|', kjid);
		vjid = Mjonot.erota(sb, '|', vjid);
		if (Mjonot.erota(sb, '|', kotitulos) == "true") {
			kotitulos = true;
		}
		if (Mjonot.erota(sb, '|', vierastulos) == "false") {
			vierastulos = false;
		}
		ttid = Mjonot.erota(sb, '|', ttid);
		pvm = Mjonot.erota(sb, '|', pvm);
	}

	public Ottelu() {

	}

	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));
	}

	public void tulosta(PrintStream out) {
		out.println(oid + " | " + nimi + " | " + kotiottelija + " | " + vierasottelija + " | " + kotiseura + " | "
				+ vierasseura + " | " + kotipaino + " | " + vieraspaino + " | " + kotitulos + " | " + vierastulos
				+ " | " + kjid + " | " + vjid + " | " + ttid + " | " + pvm + " | ");

	}

	public int rekisteroi() {
		oid = sNro;
		sNro = sNro + 1;
		return oid;
	}
//====================================================================================
	// Metodit otteluun kuuluvien ottelijoiden tietojen käsittelemiselle.

	public String getNimi() {
		return this.nimi;
	}

	public int getKJID() {
		return kjid;
	}

	public int getVJID() {
		return vjid;
	}

	public int getOID() {
		return oid;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		return this.toString().equals(obj.toString());
	}

	@Override
	public int hashCode() {
		return oid;
	}

	/**
	 * Luo testiottelun
	 * 
	 * @param koti   ottelija
	 * @param vieras ottelija
	 */
	public void testi(int koti, int vieras) {
		kjid = koti;
		vjid = vieras;
		kotitulos = true;
		vierastulos = false;
		ttid = "KO";
	}

}
