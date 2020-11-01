package Potkukanta;

import java.util.ArrayList;
import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * @author topias & joona
 * @version 7.0 5.5.2020
 * 
 *          Potkukantarekisteri-luokka, joka huolehtii ottelijoiden, ja
 *          otteluiden lisäämisestä. Metodit välittäjämetoideita ottelut- ja
 *          ottelijat luokkiin.
 *
 *
 */
public class Potkukantarekisteri {

	private Ottelijat ottelijat = new Ottelijat();
	private Ottelut ottelut = new Ottelut();

	public Ottelijat getOttelijat() {
		return ottelijat;
	}

	/**
	 * Palautaa Ottelijoiden määrän
	 * 
	 * @return ottelijamäärä
	 */
	public int getOttelijoita() {
		return ottelijat.getLkm();
	}

	/**
	 * Palautaa Otteluiden määrän
	 * 
	 * @return ottelumäärä
	 */
	public int getOtteluita() {
		return ottelut.getLkm();
	}

	/**
	 * Lisää rekisteriin uuden ottelijan
	 * 
	 * @param lisattava lisättävä ottelija
	 * @throws SailoException jos lisäystä ei voida tehdä
	 * @example
	 * 
	 */
	public void lisaa(Ottelija lisattava) throws SailoException {

		ottelijat.lisaa(lisattava);
	}

	/**
	 * Lisää rekisteriin uuden ottelun
	 * 
	 * @param ottelu lisättävä ottelu
	 * @throws SailoException jos lisäystä ei voida tehdä
	 * @example
	 * 
	 */
	public void lisaa(Ottelu ottelu) throws SailoException {
		ottelut.lisaa(ottelu);
	}

	/**
	 * Palauttaa i:n ottelijan
	 * 
	 * @param i monesko ottelija palautetaan
	 * @return viite i:teen ottelijaan
	 * @throws IndexOutOfBoundsException jos i väärin
	 */
	public Ottelija annaOttelija(int i) throws IndexOutOfBoundsException {
		return ottelijat.anna(i);
	}

	/**
	 * Palauttaa i:n ottelun
	 * 
	 * @param i monesko ottelu palautetaan
	 * @return viite i:teen otteluun
	 * @throws IndexOutOfBoundsException jos i väärin
	 */
	public Ottelu annaOttelu(int i) throws IndexOutOfBoundsException {
		return ottelut.anna(i);
	}

	/**
	 * Asettaa tiedostojen perusnimet
	 * 
	 * @param nimi uusi nimi
	 */
	public void setTiedosto(String nimi) {
		File dir = new File(nimi);
		dir.mkdirs();
		String hakemistonNimi = "";
		if (!nimi.isEmpty())
			hakemistonNimi = nimi + "/";
		ottelijat.setTiedostonOletusNimi(hakemistonNimi + "Ottelijat");
		ottelut.setTiedostonPerusNimi(hakemistonNimi + "Ottelut");
	}

	/**
	 * Lukee kerhon tiedot tiedostosta
	 * 
	 * @throws SailoException jos lukeminen epäonnistuu
	 * 
	 */
	public void lueTiedostosta() throws SailoException {
		setTiedosto("POTKUKANTAREKISTERI");
		ottelijat.lueTiedostosta();
		ottelut.lueTiedostosta();
	}

	/**
	 * 
	 * Tallentaa otteluiden tiedot tiedostoon. Vaikka ottelijoiden tallettamien
	 * epäonistuisi, niin yritetään silti tallettaa ottelijoita ennen poikkeuksen
	 * heittämistä.
	 * 
	 * @throws SailoException jos tallettamisessa ongelmia
	 */
	public void tallenna() throws SailoException {
		String virhe = "";
		try {
			ottelijat.tallenna();
		} catch (SailoException ex) {
			virhe = ex.getMessage();
		}

		try {
			ottelut.tallenna();
		} catch (SailoException ex) {
			virhe += ex.getMessage();
		}
		if (!"".equals(virhe))
			throw new SailoException(virhe);
	}

	/**
	 * Palauttaa "taulukossa" hakuehtoon vastaavien jäsenten viitteet
	 * 
	 * @param hakuehto hakuehto
	 * @return tietorakenteen löytyneistä jäsenistä
	 * @throws SailoException Jos jotakin menee väärin
	 */
	public Collection<Ottelija> etsi(String hakuehto) throws SailoException {
		return ottelijat.etsi(hakuehto);
	}

	/**
	 * Palauttaa "taulukossa" tietyn ottelijan ottelut jossa hän on ollut mukana.
	 * 
	 * @param ottelija ottelija
	 * @return ottelijan otteluiden tiedot
	 * @throws SailoException jos jotakin menee väärin
	 */
	public List<Ottelu> annaOttelut(Ottelija ottelija) throws SailoException {
		return ottelut.annaOttelut(ottelija.getID());
	}

	/**
	 * Mikäli ottelija on jo rekisterissä, niin korvaa sen Mikäli ottelija ei ole
	 * vielä rekisterissä niin lisää sen rekisteriin uutena otteijana.
	 * 
	 * @param ottelija
	 * @throws SailoException
	 */
	public void lisaaTaiKorvaa(Ottelija ottelija) throws SailoException {
		ottelijat.korvaaTaiLisaa(ottelija);
	}

	/**
	 * Mikäli ottelu on jo rekisterissä niin korvaaa ottelun. Mikäli ottelu ei ole
	 * vielä rekisterissä, niin lisää ottelun uutena otteluna
	 * 
	 * @param ottelu
	 * @throws SailoException
	 */
	public void lisaaTaiKorvaa(Ottelu ottelu) throws SailoException {
		ottelut.korvaaTaiLisaa(ottelu);

	}

	/**
	 * Testiohjelma rekisteristä
	 * 
	 * @param args ei käytössä
	 */
//     public static void main(String args[]) {
//     Potkukantarekisteri rekisteri = new Potkukantarekisteri();
//    
//     try {
//    
//     Ottelija testi = new Ottelija();
//     Ottelija topi = new Ottelija("Topi Topivich", "JKB", "M", "3-0-0", 85,
//     2019, true, false);
//     Ottelija onni = new Ottelija("Onni Onkija", "BASE", "M", "0-3-0", 75,
//     2019,true,false);
//     testi.rekisteroi();
//     topi.rekisteroi();
//     onni.rekisteroi();
//    
//     rekisteri.lisaa(testi);
//     rekisteri.lisaa(topi);
//     rekisteri.lisaa(onni);
//    
//     System.out.println("============= REKISTERIN testi====================="+ '\n');
//    
//     System.out.println("============= Ottelijat testi======================");
//    
//     for (int i = 0; i <rekisteri.getOttelijoita(); i++) {
//     Ottelija ottelija = rekisteri.annaOttelija(i);
//     System.out.println("Ottelija paikassa: " + i);
//     ottelija.tulosta(System.out);
//     }
//    
//     } catch (SailoException ex) {
//     System.out.println(ex.getMessage());
//     }
//     try {
//    
//     Ottelut ottelut = new Ottelut();
//    
//     Ottelu testi = new Ottelu();
//     Ottelu GraniitinKovimmat = new Ottelu("Graniitin Kovimmat", "JKB", 0, 1, 1, 3, "2020-01-18");
//     Ottelu LutakonLoysat = new Ottelu("Lutakon Loysät","BASE",1,0,2,2,"2020-03-03");
//     testi.rekisteroi();
//     GraniitinKovimmat.rekisteroi();
//     LutakonLoysat.rekisteroi();
//    
//     ottelut.lisaa(testi);
//     ottelut.lisaa(GraniitinKovimmat);
//     ottelut.lisaa(LutakonLoysat);
//    
//     System.out.println("============= Ottelut testi========================");
//    
//     for (int i = 0; i < ottelut.getLkm(); i++) {
//     Ottelu ottelu = ottelut.anna(i);
//    
//     ottelu.tulosta(System.out);
//     }
//    
//     } catch (SailoException ex) {
//     System.out.println(ex.getMessage());
//     }
//    
//     }

}
