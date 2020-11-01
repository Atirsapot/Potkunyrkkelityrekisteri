package Potkukanta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author topias & joona
 * @versio 7.0 5.5.2020
 *
 */

public class Ottelut implements Iterable<Ottelu> {

	private boolean muutettu = false;
	private String tiedostonPerusNimi = "";

	/** Taulukko Otteluista */
	private final List<Ottelu> ottelut = new ArrayList<Ottelu>();

	private String tiedostonNimi = "ottelut.dat";

	/*
	 * Otteluiden alustaminen
	 */
	public Ottelut() {
		
	}

	/**
	 * Lisää uuden Ottelun tietorakenteeseen.
	 * 
	 * @param ottelu lisätäävän ottelun viite.
	 * @example
	 * 
	 *          <pre name="test">
	 * #THROWS SailoException 
	 * Ottelut ottelijat = new Ottelut();
	 * Ottelut graniitinKovimmat = new Ottelu(), lutakonLoysat = new Ottelu();
	 * ottelut.getLkm() === 0;
	 * ottelut.lisaa(graniitinKovimmat); ottlut.getLkm() === 1;
	 * ottelu.lisaa(lutakonLoysat); ottelut.getLkm() === 2;
	 * ottelut.lisaa(graniitinKovimmat); ottelut.getLkm() === 3;
	 * ottelut.anna(0) === graniitinKovimmat;
	 * ottelut.anna(1) === lutakonLoysat;
	 * ottelut.anna(2) === graniitinKovimmat;
	 * ottelut.anna(1) == graniitinKovimmat === false;
	 * ottelut.anna(1) == lutakonLoysat === true;
	 * ottelut.anna(3) === graniitinKovimmat; #THROWS IndexOutOfBoundsException 
	 * ottelut.lisaa(aku1); ottelut.getLkm() === 4;
	 * ottelut.lisaa(aku1); ottelut.getLkm() === 5;
	 * ottelut.lisaa(aku1);  #THROWS SailoException
	 *          </pre>
	 */
	public void lisaa(Ottelu ottelu) {
		ottelut.add(ottelu);
		muutettu = true;
	}

	/**
	 * Lukee ottelut tiedostosta
	 * 
	 * @param tied tiedoston perusnimi
	 * @throws SailoException jos lukeminen ei onnistu
	 * 
	 * @example
	 * 
	 *          <pre name="test">
	 *          #THROWS SailoException #import java.io.File;
	 * 
	 *          Ottelut ottelut = new Ottelut(); Ottelut testi = new Ottelut(),
	 *          pesti = new Ottelu(); String hakemisto = "testiottelut"; String
	 *          tiedNimi = hakemisto+"/nimet"; File ftied = new
	 *          File(tiedNimi+".dat"); File dir = new File(hakemisto); dir.kdir();
	 *          ottelut.lueTiedosta(tiedNimi); #THROWS SailoException
	 *          ottelut.lisaa(testi); ottelut.lisaa(pesti); ottelut.tallenna();
	 *          ottelut = new Ottelut(); ottelut.lueTiedostosta(tiedNimi);
	 *          Iterator<Ottelut> i = ottelut.iterator(); i.next() === testi;
	 *          i.nexT() === pesti; i.hasNext === false; ottelut.lisaa(pesti);
	 *          ottelut.tallenna(); ftied.delete() === true; File fbak = new
	 *          File(tiedNimi+".bak"); fbak.delete() === true; dir.delete() ===
	 *          true;
	 * 
	 *          <pre>
	 * 
	 */
	public void lueTiedostosta(String tied) throws SailoException {
		setTiedostonPerusNimi(tied);
		try (BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi()))) {

			String rivi;
			while ((rivi = fi.readLine()) != null) {
				rivi = rivi.trim();
				if ("".equals(rivi) || rivi.charAt(0) == ';')
					continue;
				Ottelu ottelu = new Ottelu();
				ottelu.parse(rivi);
				lisaa(ottelu);
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
		lueTiedostosta(getTiedostonPerusNimi());
	}

	/**
	 * Tallentaa ottelut tiedostoon.
	 * 
	 * @throws SailoException jos talletus epäonnistuu
	 */
	public void tallenna() throws SailoException {
		if (!muutettu)
			return;

		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());
		fbak.delete(); // if ... System.err.println("Ei voi tuhota");
		ftied.renameTo(fbak); // if ... System.err.println("Ei voi nimetä");

		try (PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath()))) {
			for (Ottelu ottelu : this) {
				fo.println(ottelu.toString());
			}
		} catch (FileNotFoundException ex) {
			throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
		} catch (IOException ex) {
			throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
		}

		muutettu = false;
	}

	/**
	 * Palauttaa viitteen i:teen otteluun.
	 * 
	 * @param i monennenko ottelun viite halutaan
	 * @return viite otteluun, jonka indeksi on i
	 * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
	 */
	public Ottelu anna(int i) throws IndexOutOfBoundsException {
		if (i < 0 || ottelut.size() <= i)
			throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
		return ((ArrayList<Ottelu>) ottelut).get(i);
	}

	/**
	 * Asettaa tiedoston perusnimen ilan tarkenninta
	 * 
	 * @param tied tallennustiedoston perusnimi
	 */
	public void setTiedostonPerusNimi(String tied) {
		tiedostonPerusNimi = tied;
	}

	/**
	 * Palauttaa tiedoston nimen, jota käytetään tallennukseen
	 * 
	 * @return tallennustiedoston nimi
	 */
	public String getTiedostonPerusNimi() {
		return tiedostonPerusNimi;
	}

	/**
	 * Palauttaa tiedoston nimen, jota käytetään tallennukseen
	 * 
	 * @return tallennustiedoston nimi
	 */
	public String getTiedostonNimi() {
		return tiedostonPerusNimi + ".dat";
	}

	/**
	 * Palauttaa varakopiotiedoston nimen
	 * 
	 * @return varakopiotiedoston nimi
	 */
	public String getBakNimi() {
		return tiedostonPerusNimi + ".bak";
	}

	/**
	 * Palauttaa Rekisterin otteluiden lukumäärän
	 * 
	 * @return otteluiden lukumäärä
	 */
	public int getLkm() {
		return ottelut.size();
	}

	/**
	 * Iteraattori kaikkien Otteluiden läpikäymiseen
	 * 
	 * @return otteluiteraattori
	 */
	@Override
	public Iterator<Ottelu> iterator() {
		return ottelut.iterator();
	}

	/*
	 * Haetaan kaikki ottelut
	 */
	public List<Ottelu> annaOttelut(int jid) {
		List<Ottelu> loydetyt = new ArrayList<Ottelu>();
		for (Ottelu ottelu : ottelut)
			if (ottelu.getKJID() == jid || ottelu.getVJID() == jid)
				loydetyt.add(ottelu);
		return loydetyt;
	}

	
	/** 
	 * tarkastaa onko ottelun ID:llä ottelua rekisterissä,
	 * jos on, niin rekisterissä oleva korvataan. Jos ei, niin
	 * lisätään loppuun uutena
	 * @param ottelu
	 */
	public void korvaaTaiLisaa(Ottelu ottelu) {
		int id = ottelu.getOID();
        for (int i = 0; i < getLkm(); i++) {
            if (ottelut.get(i).getOID() == id) {
               ottelut.set(i, ottelu);
                muutettu = true;
                return;
            }
        }
        lisaa(ottelu);
	}
}