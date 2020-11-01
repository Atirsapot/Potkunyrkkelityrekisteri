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

/**
 * Rekisterin ottelijat. Osaa mm. lisätä ottelijoita ja antaa tiettyjä
 * ottelijoita.
 * 
 * @author topias & joona
 * @versio 7.0 5.5.2020
 */

public class Ottelijat implements Iterable<Ottelija> {

	private static final int MAX_OTTELIJOITA = 10;
	private int lkm = 0;
	private boolean muutettu = false;
	private String kokoNimi = "";
	private String tiedostonOletusNimi = "ottelijat";
	private Ottelija[] ottelijat = new Ottelija[MAX_OTTELIJOITA];

	public Ottelijat() {

	}

	/**
	 * Lisää uuden Ottelijan tietorakenteeseen.
	 * 
	 * @param ottelija ottelija
	 * @throws SailoException jos tietorakenne on jo täynnä
	 * @example
	 * 
	 *          <pre name="test">
	 * #THROWS SailoException 
	 * Ottelijat ottelijat = new Ottelijat();
	 * Ottelija topi = new Ottelija(), onni = new Ottelija();
	 * ottelijat.getLkm() === 0;
	 * ottelijat.lisaa(topi); jasenet.getLkm() === 1;
	 * ottelijat.lisaa(onni); jasenet.getLkm() === 2;
	 * ottelijat.lisaa(topi); jasenet.getLkm() === 3;
	 * ottelijat.anna(0) === topi;
	 * ottelijat.anna(1) === onni;
	 * ottelijat.anna(2) === topi;
	 * ottelijat.anna(1) == topi === false;
	 * ottelijat.anna(1) == onni === true;
	 * ottelijat.anna(3) === topi; #THROWS IndexOutOfBoundsException 
	 * ottelijat.lisaa(aku1); ottelijat.getLkm() === 4;
	 * ottelijat.lisaa(aku1); ottelijat.getLkm() === 5;
	 * ottelijat.lisaa(aku1);  #THROWS SailoException
	 *          </pre>
	 */
	public void lisaa(Ottelija ottelija) throws SailoException {
		if (lkm >= MAX_OTTELIJOITA) {
			ottelijat = Arrays.copyOf(ottelijat, lkm +10);
		}
		ottelijat[lkm] = ottelija;
		lkm++;
		muutettu = true;

	}

	/**
	 * tarkastaa onko ottelijan ID:llä ottelijaa rekisterissä, jos on, niin
	 * rekisterissä oleva korvataan. Jos ei, niin lisätään loppuun uutena.
	 * 
	 * @param ottelija
	 */
	public void korvaaTaiLisaa(Ottelija ottelija) throws SailoException {
		int id = ottelija.getID();
		for (int i = 0; i < lkm; i++) {
			if (ottelijat[i].getID() == id) {
				ottelijat[i] = ottelija;
				muutettu = true;
				return;
			}
		}
		lisaa(ottelija);
	}

	/**
	 * Palauttaa viitteen i:teen ottelijaan.
	 * 
	 * @param i monennenko ottelijan viite halutaan
	 * @return viite ottelijaan, jonka indeksi on i
	 * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
	 */
	public Ottelija anna(int i) throws IndexOutOfBoundsException {
		if (i < 0 || lkm <= i)
			throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
		return ottelijat[i];
	}

	/**
	 * Lukee ottelijat tiedostosta
	 * 
	 * @param tied tiedoston perusnimi
	 * @throws SailoException jos lukeminen ei onnistu
	 * 
	 * @example
	 * 
	 *          <pre name="test">
	 *          #THROWS SailoException #import java.io.File;
	 * 
	 *          Ottelijat ottelijat = new Ottelijat(); Ottelija testi = new
	 *          Ottelija(), pesti = new Ottelija(); String hakemisto =
	 *          "testiottelijat"; String tiedNimi = hakemisto+"/nimet"; File ftied =
	 *          new File(tiedNimi+".dat"); File dir = new File(hakemisto);
	 *          dir.kdir(); ottelijat.lueTiedosta(tiedNimi); #THROWS SailoException
	 *          ottelijat.lisaa(testi); ottelijat.lisaa(pesti);
	 *          ottelijat.tallenna(); ottelijat = new Ottelijat();
	 *          jasenet.lueTiedostosta(tiedNimi); Iterator<Ottelija> i =
	 *          ottelijat.iterator(); i.next() === testi; i.nexT() === pesti;
	 *          i.hasNext === false; ottelijat.lisaa(pesti); ottelijat.tallenna();
	 *          ftied.delete() === true; File fbak = new File(tiedNimi+".bak");
	 *          fbak.delete() === true; dir.delete() === true;
	 * 
	 *          <pre>
	 * 
	 */
	public void lueTiedostosta(String tied) throws SailoException {
		setTiedostonOletusNimi(tied);
		try (BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()))) {
			kokoNimi = fi.readLine();
			if (kokoNimi == null)
				throw new SailoException("Tietokannan nimi puuttuu");
			String rivi = fi.readLine();
			if (rivi == null)
				throw new SailoException("Maksimikoko puuttuu");

			while ((rivi = fi.readLine()) != null) {
				rivi = rivi.trim();
				if ("".equals(rivi) || rivi.charAt(0) == ';')
					continue;
				Ottelija ottelija = new Ottelija();
				ottelija.parse(rivi);
				lisaa(ottelija);
			}
			muutettu = false;

		} catch (FileNotFoundException e) {
			throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
		} catch (IOException e) {
			throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
		}
	}

	/**
	 * Luetaan aikaisemmin annetun nimisestä tiedostosta
	 * 
	 * @throws SailoException jos tulee poikkeus
	 */
	public void lueTiedostosta() throws SailoException {
		lueTiedostosta(getTiedostonOletusNimi());
	}

	/**
	 * Tallentaa jäsenistön tiedostoon. Tiedoston muoto:
	 * 
	 * <pre>
	 * Ottelijat
	 * 5
	 * ;jid|sukunimi etunimi|sukupuoli|painoluokka|seura|ikä|  Aktiivisuus  |doping|
	 * 1 |Huttunen Joona  |  mies   |    75     | JPN | M |  Aktiivinen   |  -   | 
	 * 2 |Seppänen Tommi  |  mies   |    85     | JPN | M |  Aktiivinen   |  -   |
	 * 3 |Walaström Eeva  |  nainen |    65     |TNPN | N |  Aktiivinen   |  -   |
	 * 4 |Viineri Veeti   |  mies   |    45     | KPN | MA|  Aktiivinen   |  -   |
	 * </pre>
	 * 
	 * @throws SailoException jos talletus epäonnistuu
	 */
	public void tallenna() throws SailoException {

		if (!muutettu)
			return;

		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());

		fbak.delete(); // if .. System.err.println("Ei voi tuhota");
		ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimetä");

		try (PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))) {
			fo.println(getKokoNimi());
			fo.println(ottelijat.length);
			for (Ottelija ottelija : this) {
				fo.println(ottelija.toString());
			}
			// throw new SailoException("Tallettamisessa ongelmia: " + e.getMessage());
		} catch (FileNotFoundException ex) {
			throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
		} catch (IOException ex) {
			throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
		}

		muutettu = false;
		System.out.print("Tallenettu!");
	}

	/**
	 * Palauttaa Rekisterin ottelijoiden lukumäärän
	 * 
	 * @return ottelijoiden lukumäärä
	 */
	public int getLkm() {
		return lkm;
	}

	/**
	 * Palauttaa tiedoston nimen, jota käytetään tallennukseen
	 * 
	 * @return tallennustiedoston nimi
	 */
	public String getTiedostonOletusNimi() {
		return tiedostonOletusNimi;
	}

	/**
	 * Asettaa tiedoston perusnimen ilan tarkenninta
	 * 
	 * @param nimi tallennustiedoston perusnimi
	 */
	public void setTiedostonOletusNimi(String nimi) {
		tiedostonOletusNimi = nimi;
	}

	/**
	 * Palauttaa tiedoston nimen, jota käytetään tallennukseen
	 * 
	 * @return tallennustiedoston nimi
	 */
	public String getTiedostonNimi() {
		return getTiedostonOletusNimi() + ".dat";
	}

	/**
	 * Palauttaa tietokannan koko otsakkeen
	 * 
	 * @return Kerhon koko nimi merkkijononna
	 */
	public String getKokoNimi() {
		return kokoNimi;
	}

	/**
	 * Palauttaa varakopiotiedoston nimen
	 * 
	 * @return varakopiotiedoston nimi
	 */
	public String getBakNimi() {
		return tiedostonOletusNimi + ".bak";
	}

	/**
	 * Luokka jäsenten iteroimiseksi.
	 * 
	 * @example
	 * 
	 *          <pre name="test">
	 * #THROWS SailoException 
	 * #PACKAGEIMPORT
	 * #import java.util.*;
	 * 
	 * Jasenet jasenet = new Jasenet();
	 * Jasen aku1 = new Jasen(), aku2 = new Jasen();
	 * aku1.rekisteroi(); aku2.rekisteroi();
	 *
	 * jasenet.lisaa(aku1); 
	 * jasenet.lisaa(aku2); 
	 * jasenet.lisaa(aku1); 
	 * 
	 * StringBuffer ids = new StringBuffer(30);
	 * for (Jasen jasen:jasenet)   // Kokeillaan for-silmukan toimintaa
	 *   ids.append(" "+jasen.getTunnusNro());           
	 * 
	 * String tulos = " " + aku1.getTunnusNro() + " " + aku2.getTunnusNro() + " " + aku1.getTunnusNro();
	 * 
	 * ids.toString() === tulos; 
	 * 
	 * ids = new StringBuffer(30);
	 * for (Iterator<Jasen>  i=jasenet.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
	 *   Jasen jasen = i.next();
	 *   ids.append(" "+jasen.getTunnusNro());           
	 * }
	 * 
	 * ids.toString() === tulos;
	 * 
	 * Iterator<Jasen>  i=jasenet.iterator();
	 * i.next() == aku1  === true;
	 * i.next() == aku2  === true;
	 * i.next() == aku1  === true;
	 * 
	 * i.next();  #THROWS NoSuchElementException
	 * 
	 *          </pre>
	 */
	public class OttelijatIterator implements Iterator<Ottelija> {
		private int kohdalla = 0;

		/**
		 * Onko olemassa vielä seuraavaa jäsentä
		 * 
		 * @see java.util.Iterator#hasNext()
		 * @return true jos on vielä jäseniä
		 */
		@Override
		public boolean hasNext() {
			return kohdalla < getLkm();
		}

		/**
		 * Annetaan seuraava jäsen
		 * 
		 * @return seuraava jäsen
		 * @throws NoSuchElementException jos seuraava alkiota ei enää ole
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Ottelija next() throws NoSuchElementException {
			if (!hasNext())
				throw new NoSuchElementException("Ei oo");
			return anna(kohdalla++);
		}

		/**
		 * Tuhoamista ei ole toteutettu
		 * 
		 * @throws UnsupportedOperationException aina
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException("Me ei poisteta");
		}
	}

	/**
	 * Palautetaan iteraattori jäsenistään.
	 * 
	 * @return jäsen iteraattori
	 */
	@Override
	public Iterator<Ottelija> iterator() {
		return new OttelijatIterator();
	}

	/**
	 * Palauttaa "taulukossa" hakuehtoon vastaavien ottelijoiden viitteet
	 * 
	 * @param hakuehto hakuehto
	 * @param k        etsittävän kentän indeksi
	 * @return tietorakenteen löytyneistä ottelijoista
	 * @example
	 * 
	 *          <pre name="test">
	 *  
	 * #THROWS SailoException  
	 *   Ottelijat ottelijat = new Ottelijat(); 
	 *   Ottelija ottelija1 = new Ottelija(); ottelija1.parse(" 1 |Huttunen Joona  |  mies   |    75     | JPN | M |  Aktiivinen   |  false   |"); 
	 *   Ottelija ottelija2 = new Ottelija(); ottelija2.parse(" 2 |Seppänen Tommi  |  mies   |    85     | JPN | M |  Aktiivinen   |  false   |"); 
	 *   Ottelija ottelija3 = new Ottelija(); ottelija3.parse(" 3 |Walaström Eeva  |  nainen |    65     |TNPN | N |  Aktiivinen   |  true    |");  
	 *   ottelijat.lisaa(ottelija1); ottelijat.lisaa(ottelija2); otelijat.lisaa(ottelija3);
	 *   // TODO: toistaiseksi palauttaa kaikki ottelijat
	 *          </pre>
	 */
	@SuppressWarnings("unused")
	public Collection<Ottelija> etsi(String hakuehto) {
		String ehto = "*";
		if (hakuehto != null && hakuehto.length() > 0)
			ehto = hakuehto;
		List<Ottelija> loytyneet = new ArrayList<Ottelija>();
		for (Ottelija ottelija : this) {
			if (WildChars.onkoSamat(ottelija.getNimi(), ehto + '*'))
				loytyneet.add(ottelija);
		}
		Collections.sort(loytyneet, new Ottelija.Vertailija());
		return loytyneet;
	}
}
