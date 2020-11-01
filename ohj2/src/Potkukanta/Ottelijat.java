package Potkukanta;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

import static Potkukanta.Kanta.alustaKanta;
import java.sql.*;
import java.io.File;

/**
 * Rekisterin ottelijat. Osaa mm. lisätä ottelijoita ja antaa tiettyjä
 * ottelijoita.
 * 
 * @author topias & joona
 * @versio 7.0 5.5.2020
 */

public class Ottelijat {
    
    private Ottelijat ottelijat;
    private String tiedNimi;
    private File ftied;
    
    public void alusta() throws SailoException { 
            
             tiedNimi = "testiOttelijat";
             ftied = new File(tiedNimi+".db");
             ftied.delete();
             ottelijat = new Ottelijat(tiedNimi);
         }



    private boolean muutettu = false;
//    private String kokoNimi = "";
//    private String tiedostonOletusNimi = "ottelijat";
    private static Ottelija apuottelija = new Ottelija();
    private Kanta kanta;

    /**
	     * Tarkistetaan että kannassa jäsenten tarvitsema taulu
	     * @param nimi tietokannan nimi
	     * @throws SailoException jos jokin menee pieleen
	     */
	    public Ottelijat(String nimi) throws SailoException {
	        kanta = alustaKanta(nimi);
	        try ( Connection con = kanta.annaKantayhteys() ) {
	            // Hankitaan tietokannan metadata ja tarkistetaan siitä onko
	            // Ottelijat nimistä taulua olemassa.
	            // Jos ei ole, luodaan se.
	            DatabaseMetaData meta = con.getMetaData();
	            
	            try ( ResultSet taulu = meta.getTables(null, null, "Ottelijat", null) ) {
	                if ( !taulu.next() ) {
	                    // Luodaan Ottelijat taulu
	                    try ( PreparedStatement sql = con.prepareStatement(apuottelija.annaLuontilauseke()) ) {
	                        sql.execute();
	                    }
	                }
	            }
	            
	        } catch ( SQLException e ) {
	            throw new SailoException("Ongelmia tietokannan kanssa:" + e.getMessage());
	        }
	    }



	    /**
         * Lisää uuden ottelijan tietorakenteeseen.  Ottaa ottelijan omistukseensa.
         * @param ottelija lisättävän ottelija viite.  Huom tietorakenne muuttuu omistajaksi
         * @throws SailoException jos tietorakenne on jo täynnä
         * @example
         * <pre name="test">
         * #THROWS SailoException 
         * 
         * Collection<Ottelija> loytyneet = ottelijat.etsi("", 1);
         * loytyneet.size() === 0;
         * 
         * Ottelija aku1 = new Ottelija(), aku2 = new Ottelija();
         * ottelijat.lisaa(aku1); 
         * ottelijat.lisaa(aku2);
         *
         * loytyneet = ottelijat.etsi("", 1);
         * loytyneet.size() === 2;
         * 
         * // Unique constraint ei hyväksy
         * ottelijat.lisaa(aku1); #THROWS SailoException
         * Ottelija aku3 = new Ottelija(); Ottelija aku4 = new Ottelija(); Ottelija aku5 = new Ottelija();
         * ottelijat.lisaa(aku3); 
         * ottelijat.lisaa(aku4); 
         * ottelijat.lisaa(aku5); 

         * loytyneet = ottelijat.etsi("", 1);
         * loytyneet.size() === 5;
         * Iterator<Ottelija> i = loytyneet.iterator();
         * i.next() === aku1;
         * i.next() === aku2;
         * i.next() === aku3;
         * </pre>
         */
	    public void lisaa(Ottelija ottelija) throws SailoException {
	        try ( Connection con = kanta.annaKantayhteys(); PreparedStatement sql = ottelija.annaLisayslauseke(con) ) {
	            sql.executeUpdate();
	            try ( ResultSet rs = sql.getGeneratedKeys() ) {
	               ottelija.tarkistaId(rs);
	            }   
	            
	        } catch (SQLException e) {
	            throw new SailoException("Ongelmia tietokannan kanssa:" + e.getMessage());
	        }
	    }
    
    


//    /**
//     * tarkastaa onko ottelijan ID:llä ottelijaa rekisterissä, jos on, niin
//     * rekisterissä oleva korvataan. Jos ei, niin lisätään loppuun uutena.
//     * 
//     * @param ottelija ottelija
//     * @throws SailoException virhe
//     */
//    public void korvaaTaiLisaa(Ottelija ottelija) throws SailoException {
//        int id = ottelija.getID();
//        for (int i = 0; i < lkm; i++) {
//            if (ottelijat[i].getID() == id) {
//                ottelijat[i] = ottelija;
//                muutettu = true;
//                return;
//            }
//        }
//        lisaa(ottelija);
//    }


//    /**
//     * Palauttaa viitteen i:teen ottelijaan.
//     * 
//     * @param i monennenko ottelijan viite halutaan
//     * @return viite ottelijaan, jonka indeksi on i
//     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
//     */
//    public Ottelija anna(int i) throws IndexOutOfBoundsException {
//        if (i < 0 || lkm <= i)
//            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
//        return ottelijat[i];
//    }

//
//    /**
//     * Lukee ottelijat tiedostosta
//     * 
//     * @param tied tiedoston perusnimi
//     * @throws SailoException jos lukeminen ei onnistu
//     * 
//     * @example
//     * 
//     *          <pre name="test">
//     *          #THROWS SailoException #import java.io.File;
//     * 
//     *          Ottelijat ottelijat = new Ottelijat(); Ottelija testi = new
//     *          Ottelija(), pesti = new Ottelija(); String hakemisto =
//     *          "testiottelijat"; String tiedNimi = hakemisto+"/nimet"; File ftied =
//     *          new File(tiedNimi+".dat"); File dir = new File(hakemisto);
//     *          dir.kdir(); ottelijat.lueTiedosta(tiedNimi); #THROWS SailoException
//     *          ottelijat.lisaa(testi); ottelijat.lisaa(pesti);
//     *          ottelijat.tallenna(); ottelijat = new Ottelijat();
//     *          jasenet.lueTiedostosta(tiedNimi); Iterator<Ottelija> i =
//     *          ottelijat.iterator(); i.next() === testi; i.nexT() === pesti;
//     *          i.hasNext === false; ottelijat.lisaa(pesti); ottelijat.tallenna();
//     *          ftied.delete() === true; File fbak = new File(tiedNimi+".bak");
//     *          fbak.delete() === true; dir.delete() === true;
//     * 
//     *          <pre>
//     * 
//     */
//    public void lueTiedostosta(String tied) throws SailoException {
//        setTiedostonOletusNimi(tied);
//        try (BufferedReader fi = new BufferedReader(
//                new FileReader(getTiedostonNimi()))) {
//            kokoNimi = fi.readLine();
//            if (kokoNimi == null)
//                throw new SailoException("Tietokannan nimi puuttuu");
//            String rivi = fi.readLine();
//            if (rivi == null)
//                throw new SailoException("Maksimikoko puuttuu");
//
//            while ((rivi = fi.readLine()) != null) {
//                rivi = rivi.trim();
//                if ("".equals(rivi) || rivi.charAt(0) == ';')
//                    continue;
//                Ottelija ottelija = new Ottelija();
//                ottelija.parse(rivi);
//                lisaa(ottelija);
//            }
//            muutettu = false;
//
//        } catch (FileNotFoundException e) {
//            throw new SailoException(
//                    "Tiedosto " + getTiedostonNimi() + " ei aukea");
//        } catch (IOException e) {
//            throw new SailoException(
//                    "Ongelmia tiedoston kanssa: " + e.getMessage());
//        }
//    }
//
//
//    /**
//     * Luetaan aikaisemmin annetun nimisestä tiedostosta
//     * 
//     * @throws SailoException jos tulee poikkeus
//     */
//    public void lueTiedostosta() throws SailoException {
//        lueTiedostosta(getTiedostonOletusNimi());
//    }
//

//    /**
//     * Tallentaa jäsenistön tiedostoon. Tiedoston muoto:
//     * 
//     * <pre>
//     * Ottelijat
//     * 5
//     * ;jid|sukunimi etunimi|sukupuoli|painoluokka|seura|ikä|  Aktiivisuus  |doping|
//     * 1 |Huttunen Joona  |  mies   |    75     | JPN | M |  Aktiivinen   |  -   | 
//     * 2 |Seppänen Tommi  |  mies   |    85     | JPN | M |  Aktiivinen   |  -   |
//     * 3 |Walaström Eeva  |  nainen |    65     |TNPN | N |  Aktiivinen   |  -   |
//     * 4 |Viineri Veeti   |  mies   |    45     | KPN | MA|  Aktiivinen   |  -   |
//     * </pre>
//     * 
//     * @throws SailoException jos talletus epäonnistuu
//     */
//    public void tallenna() throws SailoException {
//
//        if (!muutettu)
//            return;
//
//        File fbak = new File(getBakNimi());
//        File ftied = new File(getTiedostonNimi());
//
//        fbak.delete(); // if .. System.err.println("Ei voi tuhota");
//        ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimetä");
//
//        try (PrintWriter fo = new PrintWriter(
//                new FileWriter(ftied.getCanonicalPath()))) {
//            fo.println(getKokoNimi());
//            fo.println(ottelijat.length);
//            for (Ottelija ottelija : this) {
//                fo.println(ottelija.toString());
//            }
//            // throw new SailoException("Tallettamisessa ongelmia: " +
//            // e.getMessage());
//        } catch (FileNotFoundException ex) {
//            throw new SailoException(
//                    "Tiedosto " + ftied.getName() + " ei aukea");
//        } catch (IOException ex) {
//            throw new SailoException("Tiedoston " + ftied.getName()
//                    + " kirjoittamisessa ongelmia");
//        }
//
//        muutettu = false;
//        System.out.print("Tallenettu!");
//    }


//    /**
//     * Palauttaa Rekisterin ottelijoiden lukumäärän
//     * 
//     * @return ottelijoiden lukumäärä
//     */
//    public int getLkm() {
//        return lkm;
//    }


//    /**
//     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
//     * 
//     * @return tallennustiedoston nimi
//     */
//    public String getTiedostonOletusNimi() {
//        return tiedostonOletusNimi;
//    }
//
//
//    /**
//     * Asettaa tiedoston perusnimen ilan tarkenninta
//     * 
//     * @param nimi tallennustiedoston perusnimi
//     */
//    public void setTiedostonOletusNimi(String nimi) {
//        tiedostonOletusNimi = nimi;
//    }
//
//
//    /**
//     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
//     * 
//     * @return tallennustiedoston nimi
//     */
//    public String getTiedostonNimi() {
//        return getTiedostonOletusNimi() + ".dat";
//    }
//
//
//    /**
//     * Palauttaa tietokannan koko otsakkeen
//     * 
//     * @return Kerhon koko nimi merkkijononna
//     */
//    public String getKokoNimi() {
//        return kokoNimi;
//    }
//
//
//    /**
//     * Palauttaa varakopiotiedoston nimen
//     * 
//     * @return varakopiotiedoston nimi
//     */
//    public String getBakNimi() {
//        return tiedostonOletusNimi + ".bak";
//    }

//    /**
//     * Luokka jäsenten iteroimiseksi.
//     * 
//     * @example
//     * 
//     *          <pre name="test">
//     * #THROWS SailoException 
//     * #PACKAGEIMPORT
//     * #import java.util.*;
//     * 
//     * Jasenet jasenet = new Jasenet();
//     * Jasen aku1 = new Jasen(), aku2 = new Jasen();
//     * aku1.rekisteroi(); aku2.rekisteroi();
//     *
//     * jasenet.lisaa(aku1); 
//     * jasenet.lisaa(aku2); 
//     * jasenet.lisaa(aku1); 
//     * 
//     * StringBuffer ids = new StringBuffer(30);
//     * for (Jasen jasen:jasenet)   // Kokeillaan for-silmukan toimintaa
//     *   ids.append(" "+jasen.getTunnusNro());           
//     * 
//     * String tulos = " " + aku1.getTunnusNro() + " " + aku2.getTunnusNro() + " " + aku1.getTunnusNro();
//     * 
//     * ids.toString() === tulos; 
//     * 
//     * ids = new StringBuffer(30);
//     * for (Iterator<Jasen>  i=jasenet.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
//     *   Jasen jasen = i.next();
//     *   ids.append(" "+jasen.getTunnusNro());           
//     * }
//     * 
//     * ids.toString() === tulos;
//     * 
//     * Iterator<Jasen>  i=jasenet.iterator();
//     * i.next() == aku1  === true;
//     * i.next() == aku2  === true;
//     * i.next() == aku1  === true;
//     * 
//     * i.next();  #THROWS NoSuchElementException
//     * 
//     *          </pre>
//     *
//    public class OttelijatIterator implements Iterator<Ottelija> {
//        private int kohdalla = 0;
//
//        /**
//         * Onko olemassa vielä seuraavaa jäsentä
//         * 
//         * @see java.util.Iterator#hasNext()
//         * @return true jos on vielä jäseniä
//         *
//        @Override
//        public boolean hasNext() {
//            return kohdalla < getLkm();
//        }
//
//
//        /**
//         * Annetaan seuraava jäsen
//         * 
//         * @return seuraava jäsen
//         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
//         * @see java.util.Iterator#next()
//         *
//        @Override
//        public Ottelija next() throws NoSuchElementException {
//            if (!hasNext())
//                throw new NoSuchElementException("Ei oo");
//            return anna(kohdalla++);
//        }
//
//
//        /**
//         * Tuhoamista ei ole toteutettu
//         * 
//         * @throws UnsupportedOperationException aina
//         * @see java.util.Iterator#remove()
//         *
//        @Override
//        public void remove() throws UnsupportedOperationException {
//            throw new UnsupportedOperationException("Me ei poisteta");
//        }
//    }
//
//    /**
//     * Palautetaan iteraattori jäsenistään.
//     * 
//     * @return jäsen iteraattori
//     *
//     */
//    @Override
//    public Iterator<Ottelija> iterator() {
//        return new OttelijatIterator();
//    }


//    /**
//     * Palauttaa "taulukossa" hakuehtoon vastaavien ottelijoiden viitteet
//     * 
//     * @param hakuehto hakuehto
//     * @return tietorakenteen löytyneistä ottelijoista
//     * @example
//     * 
//     *          <pre name="test">
//     *  
//     * #THROWS SailoException  
//     *   Ottelijat ottelijat = new Ottelijat(); 
//     *   Ottelija ottelija1 = new Ottelija(); ottelija1.parse(" 1 |Huttunen Joona  |  mies   |    75     | JPN | M |  Aktiivinen   |  false   |"); 
//     *   Ottelija ottelija2 = new Ottelija(); ottelija2.parse(" 2 |Seppänen Tommi  |  mies   |    85     | JPN | M |  Aktiivinen   |  false   |"); 
//     *   Ottelija ottelija3 = new Ottelija(); ottelija3.parse(" 3 |Walaström Eeva  |  nainen |    65     |TNPN | N |  Aktiivinen   |  true    |");  
//     *   ottelijat.lisaa(ottelija1); ottelijat.lisaa(ottelija2); otelijat.lisaa(ottelija3);
//     *   // TODO: toistaiseksi palauttaa kaikki ottelijat
//     *          </pre>
//     */
//    @SuppressWarnings("unused")
//    public Collection<Ottelija> etsi(String hakuehto) {
//        String ehto = "*";
//        if (hakuehto != null && hakuehto.length() > 0)
//            ehto = hakuehto;
//        List<Ottelija> loytyneet = new ArrayList<Ottelija>();
//        for (Ottelija ottelija : this) {
//            if (WildChars.onkoSamat(ottelija.getNimi(), ehto + '*'))
//                loytyneet.add(ottelija);
//        }
//        Collections.sort(loytyneet, new Ottelija.Vertailija());
//        return loytyneet;
//    }



/**
 * Palauttaa jäsenet listassa
 * @param hakuehto hakuehto  

 * @return jäsenet listassa
 * @throws SailoException jos tietokannan kanssa ongelmia
 * @example
 * <pre name="test">
 * #THROWS SailoException
 * Jasen aku1 = new Jasen(); aku1.vastaaAkuAnkka(); 
 * Jasen aku2 = new Jasen(); aku2.vastaaAkuAnkka(); 
 * jasenet.lisaa(aku1);
 * jasenet.lisaa(aku2);
 * jasenet.lisaa(aku2);  #THROWS SailoException  // ei saa lisätä sama id:tä uudelleen
 * Collection<Jasen> loytyneet = jasenet.etsi(aku1.getNimi(), 1);
 * loytyneet.size() === 1;
 * loytyneet.iterator().next() === aku1;
 * loytyneet = jasenet.etsi(aku2.getNimi(), 1);
 * loytyneet.size() === 1;
 * loytyneet.iterator().next() === aku2;
 * loytyneet = jasenet.etsi("", 15); #THROWS SailoException
 *
 * ftied.delete();
 * </pre>
 */
public Collection<Ottelija> etsi(String hakuehto) throws SailoException {
    String ehto = hakuehto;
    // Avataan yhteys tietokantaan try .. with lohkossa.
    try ( Connection con = kanta.annaKantayhteys();
          PreparedStatement sql = con.prepareStatement("SELECT * FROM Ottelijat WHERE nimi = ?") ) {
        ArrayList<Ottelija> loytyneet = new ArrayList<Ottelija>();
        
        sql.setString(1, "%" + ehto + "%");
        try ( ResultSet tulokset = sql.executeQuery() ) {
            while ( tulokset.next() ) {
                Ottelija j = new Ottelija ();
                j.parse(tulokset);
                loytyneet.add(j);
            }
        }
        return loytyneet;
    } catch ( SQLException e ) {
        throw new SailoException("Ongelmia tietokannan kanssa:" + e.getMessage());
    }
    }
/**
 * Testiohjelma jäsenistölle
 * @param args ei käytössä
 */
public static void main(String args[])  {
    try {
        new File("kokeilu.db").delete();
        Ottelijat ottelijat = new Ottelijat("kokeilu");

        Ottelija aku = new Ottelija(), aku2 = new Ottelija();
  
        
        ottelijat.lisaa(aku);
        ottelijat.lisaa(aku2);
     
        
        System.out.println("============= Ottelijat testi =================");

        int i = 0;
        for (Ottelija ottelija :ottelijat.etsi("")) {
            System.out.println("Ottelija nro : " + i++);
            ottelija.tulosta(System.out);
        }
        
        new File("kokeilu.db").delete();
    } catch ( SailoException ex ) {
        System.out.println(ex.getMessage());
    }
}
}
