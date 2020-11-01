package Potkukanta;

import fi.jyu.mit.ohj2.Mjonot;
import java.io.*;
import java.sql.*;

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
	
	 /**
     * Antaa tietokannan luontilausekkeen ottelutaululle
     * @return harrastustaulun luontilauseke
     */
    public String annaLuontilauseke()  {
        return "CREATE TABLE Ottelut (" +
                "oid INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "nimi VARCHAR(50), " +
                "kotiottelija VARCHAR(3) NOT NULL, " +
                "vierasottelija VARCHAR(3) NOT NULL, " +
                "kotiseura VARCHAR(30), " +
                "vierasseura VARCHAR(30), " +
                "kotipaino VARCHAR(4), " +
                "vieraspaino VARCHAR(4), " +
                "kotitulos BOOLEAN NOT NULL CHECK (kotitulos IN (0,1))," +
                "vierastulos BOOLEAN NOT NULL CHECK (vierastulos IN (0,1))," +
                "kjid INTEGER(3), " +
                "vjid INTEGER(3), " +
                "ttid VARCHAR(10), " +
                "pvm VARCHAR(10), " +
                "FOREIGN KEY (kjid) REFERENCES Ottelijat(jid)" +
                ")";
    }
	
    /**
     * Antaa ottelut lisäyslausekkeen
     * @param con tietokantayhteys
     * @return jäsenen lisäyslauseke
     * @throws SQLException Jos lausekkeen luonnissa on ongelmia
     */
    public PreparedStatement annaLisayslauseke(Connection con)
            throws SQLException {
        PreparedStatement sql = con.prepareStatement(
                "INSERT INTO Ottelut (oid, nimi, kotiottelija, " +
                "vierasottelija, kotiseura, vierasseura, kotipaino, vieraspaino, kotitulos, vierastulos, kjid, vjid, ttid, pvm)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        // Syötetään kentät näin välttääksemme SQL injektiot.
        // Käyttäjän syötteitä ei ikinä vain kirjoiteta kysely
        // merkkijonoon tarkistamatta niitä SQL injektioiden varalta!
        if ( oid != 0 ) sql.setInt(1, oid); else sql.setString(1, null);
        sql.setString(2, nimi);
        sql.setString(3, kotiottelija);
        sql.setString(4, vierasottelija);
        sql.setString(5, kotiseura);
        sql.setString(6, vierasseura);
        sql.setString(7, kotipaino);
        sql.setString(8, vieraspaino);
        int kt;
        int vt;
        if (kotitulos == true) {
            kt = 1;
        } else kt = 0;

        if(vierastulos == true) {
            vt = 1;
        } else vt = 0;

        sql.setInt(9,kt);
        sql.setInt(10,vt);
        sql.setInt(11, kjid);
        sql.setInt(12, vjid);
        sql.setString(13, ttid);
        sql.setString(14, pvm);

        return sql;
    }
    /**
     * Tarkistetaan onko id muuttunut lisäyksessä
     * @param rs lisäyslauseen ResultSet
     * @throws SQLException jos tulee jotakin vikaa
     */
    public void tarkistaId(ResultSet rs) throws SQLException {
        if ( !rs.next() ) return;
        int id = rs.getInt(1);
        if ( id == oid ) return;
        setOtteluID(id);
    }
    
    /**
     * Otetaan tiedot ResultSetistä
     * @param tulokset mistä tiedot otetaan
     * @throws SQLException Jos jokin menee vikaan
     */
    public void parse(ResultSet tulokset) throws SQLException {
          setOtteluID(tulokset.getInt("oid"));
          nimi =  tulokset.getString("nimi");
          kotiottelija = tulokset.getString("kotiottelija");
          vierasottelija =tulokset.getString("vierasottelija");
          kotiseura = tulokset.getString("kotiseura");
          vierasseura = tulokset.getString("vierasseura");
          kotipaino = tulokset.getString("kotipaino");
          vieraspaino = tulokset.getString("vieraspaino");
          if (tulokset.getInt("kotitulos") == 1) {
              kotitulos = true;
          } else kotitulos = false;

          if (tulokset.getInt("vierastulos") == 1) {
              vierastulos = true;
          } else vierastulos = false;

          kjid =  tulokset.getInt("kjid");
           vjid = tulokset.getInt("vjid");
          ttid =  tulokset.getString("ttid");
          pvm =  tulokset.getString("pvm");

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
