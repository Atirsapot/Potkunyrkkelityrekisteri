package Potkukanta;

import fi.jyu.mit.ohj2.Mjonot;
import java.io.*;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Random;
import java.sql.*;

/**
 * Potkunyrkkeilyrekisterin Ottelija luokka jossa käsitellään ottelijan tietoja.
 * 
 * @author joona & topias
 * @version 7.0 5.5.2020
 *
 */
public class Ottelija {

    private String nimi = "";
    private String seura = "";
    private String ikaluokka = "";

    private int jid; // jäsen-id
    private String rekordi = "0-0-0";
    private String painoluokka = "0";

    private boolean aktiivisuus = false;
    private boolean doping = false;

    // param sNro avulla saadaan uniikki
    // jasenID lisättäville ottelijoille.
    private static int sNro = 1;

    /**
     * Ottelijan tiedot
     * 
     * @param nimi        ottelijan nimi
     * @param seura       mihin kuuluu
     * @param ikaluokka   M, N tai juniori luokan etuliite (esim. M-A = Mies -A
     *                    juniori)
     * @param rekordi     ottelijan rekordi
     * @param painoluokka painoluokka
     * @param aktiivisuus aktiivisuus
     * @param doping      doping historia boolean arvona.
     * @param ID          ottelijan id
     */
    public Ottelija(int ID, String nimi, String seura, String ikaluokka,
            String rekordi, String painoluokka, boolean aktiivisuus,
            boolean doping) {
        this.jid = ID;
        this.nimi = nimi;
        this.seura = seura;
        this.ikaluokka = ikaluokka;
        this.rekordi = rekordi;
        this.painoluokka = painoluokka;
        this.aktiivisuus = aktiivisuus;
        this.doping = doping;

    }


    /**
      * Antaa tietokannan luontilausekkeen ottelijataululle
      * @return ottelijataulun luontilauseke
      */

    public String annaLuontilauseke() {

        return "CREATE TABLE Ottelijat ("
                + "jid INTEGER PRIMARY KEY AUTOINCREMENT , "
                + "nimi VARCHAR(70) NOT NULL, " + "seura VARCHAR(30),  "
                + "ikaluokka VARCHAR(1), " + "rekordi VARCHAR(5), "
                + "painoluokka VARCHAR(4), "
                + "aktiivisuus BOOLEAN NOT NULL CHECK (aktiivisuus IN (0,1)), "
                + "doping BOOLEAN NOT NULL CHECK (doping IN (0,1))" +
                // "PRIMARY KEY(jid)" +
                ")";

    }


    /**
     * Antaa ottelijan lisäyslausekkeen
     * @param con tietokantayhteys
     * @return ottelijan lisäyslauseke
     * @throws SQLException Jos lausekkeen luonnissa on ongelmia
     */
    public PreparedStatement annaLisayslauseke(Connection con)
            throws SQLException {
        PreparedStatement sql = con.prepareStatement("INSERT INTO Ottelijat"
                + "(jid, nimi, seura, ikaluokka, rekordi, painoluokka, aktiivisuus, doping) "
                + "VALUES (?,?,?,?,?,?,?,?)");

        // Syötetään kentät näin välttääksemme SQL injektiot.
        // Käyttäjän syötteitä ei ikinä vain kirjoiteta kysely
        // merkkijonoon tarkistamatta niitä SQL injektioiden varalta!
        if (jid != 0)
            sql.setInt(1, jid);
        else
            sql.setString(1, null);
        sql.setString(2, nimi);
        sql.setString(3, seura);
        sql.setString(4, ikaluokka);
        sql.setString(5, rekordi);
        sql.setString(6, painoluokka);
        int akt;
        int dop;
        if (aktiivisuus == true) {
            akt = 1;
        } else
            akt = 0;

        if (doping == true) {
            dop = 1;
        } else
            dop = 0;

        sql.setInt(7, akt);
        sql.setInt(8, dop);

        return sql;
    }


    /**
     * Tarkistetaan onko id muuttunut lisäyksessä
     * @param rs lisäyslauseen ResultSet
     * @throws SQLException jos tulee jotakin vikaa
     */
    public void tarkistaId(ResultSet rs) throws SQLException {
        if (!rs.next())
            return;
        int id = rs.getInt(1);
        if (id == jid)
            return;
        setJasenID(id);
    }


    /** 
     * Ottaa ottelijan tiedot ResultSetistä
     * @param tulokset mistä tiedot otetaan
     * @throws SQLException jos jokin menee väärin
     */
    public void parse(ResultSet tulokset) throws SQLException {
        setJasenID(tulokset.getInt("jid"));
        nimi = tulokset.getString("nimi");
        seura = tulokset.getString("seura");
        ikaluokka = tulokset.getString("ikaluokka");
        rekordi = tulokset.getString("rekordi");
        painoluokka = tulokset.getString("painoluokka");

        if (tulokset.getInt("aktiivisuus") == 1) {
            aktiivisuus = true;
        } else
            aktiivisuus = false;

        if (tulokset.getInt("doping") == 1) {
            doping = true;
        } else
            doping = false;

    }


    /**
     * Aliohjelma ottelijan poistamista varten. Tiedot muutetaan tyhjiksi ja nimen
     * tilalle vaihdetaan "poistettu".
     * 
     */
    public void tyhjennaOttelija() {
        this.nimi = "Poistettu";
        this.seura = "";
        this.ikaluokka = "";
        this.painoluokka = "";
        this.aktiivisuus = false;
        this.doping = false;
    }


    /**
     * Palauttaa ottelijan tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * 
     * @return ottelija " | " eroteltuna merkkijonona
     * @example
     * 
     *          <pre name="test">
     *          Ottelija ottelija = new Ottelija(); ottelija.parse(" 1 | Topivitch
     *          Topi | BASE"); ottelija.toString().startsWith("1|Topivitch
     *          Topi|BASE|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     * 
     */
    @Override
    public String toString() {
        return "" + getID() + "|" + nimi + "|" + seura + "|" + ikaluokka + "|"
                + rekordi + "|" + painoluokka + "|" + aktiivisuus + "|" + doping
                + "|";

    }


    /**
     * Selvitää ottelijan tiedot | erotellusta merkkijonosta
     * 
     * @param rivi josta ottelijan tiedot otetaan
     * 
     * @example
     * 
     *          <pre name="test">
     *   Ottelija ottelija = new Ottelija();
     *   ottelija.parse("  1  |  Topivitch Topi   | BASE");
     *   jasen.getID() === 1;
     *   ottelija.toString().startsWith("1|Topivitch Topi|BASE|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     *
     *   ottelija.rekisteroi();
     *   int n = ottelija.getID();
     *   ottelija.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero
     *   ottelija.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
     *   ottelija.getID() === n+20+1;
     * 
     *          </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setJasenID(Mjonot.erota(sb, '|', getID()));
        nimi = Mjonot.erota(sb, '|', nimi);
        seura = Mjonot.erota(sb, '|', seura);
        ikaluokka = Mjonot.erota(sb, '|', ikaluokka);
        rekordi = Mjonot.erota(sb, '|', rekordi);
        painoluokka = Mjonot.erota(sb, '|', painoluokka);

        if (Mjonot.erota(sb, '|', aktiivisuus) == "true") {
            aktiivisuus = true;
        }
        if (Mjonot.erota(sb, '|', aktiivisuus) == "false") {
            aktiivisuus = false;
        }
        if (Mjonot.erota(sb, '|', doping) == "true") {
            doping = true;
        }
        if (Mjonot.erota(sb, '|', doping) == "false") {
            doping = false;
        }

    }


    /**
     * Asettaa jasenID:n ja samalla varmistaa että seuraava numero on aina suurempi
     * kuin tähän mennessä suurin.
     * 
     * @param nr asetettava jasenID
     */
    private void setJasenID(int nro) {
        jid = nro;
        if (jid >= sNro)
            sNro = jid + 1;
    }


    // Oletusmuodostaja
    public Ottelija() {

    }


    /**
     * Antaa ottelijalle seuraavan rekisterinumeron
     * 
     * @return jäsenID
     */
    public int rekisteroi() {
        jid = sNro;
        sNro = sNro + 1;
        return jid;
    }


    /**
     * Tulostetaan henkilön tiedot
     * 
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(jid + " | " + nimi + " | " + ikaluokka + " | " + painoluokka
                + " kg" + " | " + seura + " | " + rekordi + " | " + aktiivisuus
                + " | " + doping);
    }


    /**
     * Tulostetaan ottelijan tiedot
     * 
     * @param os virta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }

    /**
     * Ottelijoiden vertailija
     * @author topiasrita
     *
     */
    public static class Vertailija implements Comparator<Ottelija> {

        @SuppressWarnings("javadoc")
        public Vertailija() {

        }


        @Override
        public int compare(Ottelija ottelija1, Ottelija ottelija2) {
            return ottelija1.getNimi().compareToIgnoreCase(ottelija2.getNimi());
        }

    }

    /**
     * Päivittää otteluiden lopputuloksen perusteella ottelijoiden rekordit.
     * @param i = kertoo lisätäänkö rekordiin voitto, häviö vai tasapeli
     * 
     */
    public void paivitaRekordia(int i) {
        int voitot = 0;
        int tasurit = 0;
        int haviot = 0;
        StringBuilder jono = new StringBuilder(this.rekordi);

        voitot = Mjonot.erotaInt(jono, 0);
        Mjonot.erotaChar(jono, '-');
        tasurit = Mjonot.erotaInt(jono, 0);
        Mjonot.erotaChar(jono, '-');
        haviot = Mjonot.erotaInt(jono, 0);

        if (i == 0) {
            voitot++;
        }
        if (i == 1) {
            tasurit++;
        }
        if (i == 2) {
            haviot++;
        }

        StringBuilder uusiRekordi = new StringBuilder();
        uusiRekordi.append(voitot);
        uusiRekordi.append("-");
        uusiRekordi.append(tasurit);
        uusiRekordi.append("-");
        uusiRekordi.append(haviot);

        this.rekordi = uusiRekordi.toString();

    }


    // ============================================================
    // ISO KASA GET METODEJA OTTELIJAN TIETOJEN SAAMISEKSI
    /**
     * @return Ottelijan nimen
     */
    public String getNimi() {
        return nimi;
    }


    /**
     * Palauttaa Ottelijan id:n
     * 
     * @return ottelijan id:n
     */
    public int getID() {
        return jid;
    }


    /**
     * 
     * @return ottelijan ikäluokka
     */
    public String getIka() {
        return ikaluokka;
    }


    /**
     * 
     * @return Ottelijan paino
     */
    public String getPaino() {
        return painoluokka;
    }


    /**
     * 
     * @return ottelijan seura
     */
    public String getSeura() {
        return seura;
    }


    /**
     * 
     * @return rekordi
     */
    public String getRekordi() {
        return rekordi;
    }


    /**
     * 
     * @return aktiivisuus muutettuna booleanista stringiksi tulostusta varten
     */
    public String getAktiivisuus() {
        String akt = String.valueOf(aktiivisuus);
        return akt;
    }


    /**
     * 
     * @return dopingkäräytys muutettuna booleanista Stringiksi tulostusta varten.
     */
    public String getDoping() {
        String dpng = String.valueOf(doping);
        return dpng;
    }

}