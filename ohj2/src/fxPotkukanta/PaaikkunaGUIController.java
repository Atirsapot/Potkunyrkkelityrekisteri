package fxPotkukanta;

import java.io.PrintStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.io.IOException;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import Potkukanta.Ottelija;
import Potkukanta.Ottelu;
import Potkukanta.Potkukantarekisteri;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;
import Potkukanta.SailoException;

/**
 * luokka rekisterin käyttöliittymän tapahtumien käsittelyyn
 * 
 * @author topias & joona
 * @versio 7.0 5.5.2020
 *
 */

public class PaaikkunaGUIController implements Initializable {

    @FXML
    private Button buttonHae;
    @FXML
    private Menu menuTiedosto;
    @FXML
    private MenuItem menuListaa;
    @FXML
    private MenuItem menuTallenna;
    @FXML
    private MenuItem menuLopeta;
    @FXML
    private Menu menuMuokkaa;
    @FXML
    private Menu menuApua;
    @FXML
    private TextField textHakuehto;
    @FXML
    private ListChooser<Ottelija> listHakutulokset;
    @FXML
    private TextField textOttelijaNimi;
    @FXML
    private TextField textOttelijaPaino;
    @FXML
    private TextField textOttelijaSeura;
    @FXML
    private TextField textOttelijaRekordi;
    @FXML
    private TextField textOttelijaUranpituus;
    @FXML
    private TextField textOttelijaAktiivisuus;
    @FXML
    private TextField textOttelijaDoping;
    @FXML
    private ListChooser<Ottelu> listOttelut;
    @FXML
    private Button buttonUusiOttelija;
    @FXML
    private Button buttonMuokkaaOttelijaa;
    @FXML
    private Button buttonUusiottelu;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();

    }


    // Sulkee ohjelman
    @FXML
    void handleExit() {

        Platform.exit();

    }


    // avaa timin suunnitelman selaimeen
    @FXML
    void avustus() {
        apua();
    }


    /*
     * Hakemista aloitettu, ei toimi viä, ehk joskus sit....
     */
    @FXML
    void handeHae() {
        hae(0);
        // System.out.print(ottelijaKohdalla.getID());
        // hae(ottelijaKohdalla.getID());
    }


    // Tekee käyttäjän haluaman listauksen
    @FXML
    void handleListaa() {
        // käsittelee listauksen
    }


    // Avaa "Lisää ottelija ikkunan, mutta valmiiksi täytettynä
    @FXML
    void handleMuokkaaOttelijaa() {
        muokkaaOttelijaa();
    }


    // avaa ikkunan Uusi Ottelija
    @FXML
    private void handleUusiottelija() {
        uusiOttelija();

    }


    // avaa ikkunan UusiOttelu
    @FXML
    private void handleUusiOttelu() {
        uusiOttelu();

    }


    @FXML
    void handlePoistaOttelija() {
        poistaOttelija();
    }

    // ===========================================================================================

    private static Potkukantarekisteri potkukantarekisteri;
    private Ottelija ottelijaKohdalla;

    /**
     * Tarvittavat alustukset. Nyt lisataan Ottelijalistan kuuntelija
     */
    protected void alusta() {
        listHakutulokset.addSelectionListener(e -> naytaJasen());

    }


//    /**
//     * Alustaa rekisterin lukemalla sen valitun nimisestä tiedostosta
//     * 
//     * @return null jos onnistuu, muuten virhe tekstinä
//     */
//    protected String lueTiedosto() {
//
//        try {
//            potkukantarekisteri.lueTiedostosta();
//            hae(0);
//            return null;
//        } catch (SailoException e) {
//            hae(0);
//            String virhe = e.getMessage();
//            if (virhe != null)
//                Dialogs.showMessageDialog(virhe);
//            return virhe;
//        }
//    }


    // Tallentaa muutetut tiedot
    @FXML
    void handleTallenna() throws SailoException {
        potkukantarekisteri.tallenna();
    }


    /**
     * Näyttää listasta valitun jäsenen tiedot, tilapäisesti yhteen isoon
     * edit-kenttäänŒŒ
     */
    protected void naytaJasen() {
        ottelijaKohdalla = listHakutulokset.getSelectedObject();
        if (ottelijaKohdalla == null) {

            return;
        }
        // if (ottelijaKohdalla == null) return;
        taytatiedot(ottelijaKohdalla);

    }


    @SuppressWarnings("javadoc")
    public void setPotkukanta(Potkukantarekisteri potkukantarekisteri) {
        PaaikkunaGUIController.potkukantarekisteri = potkukantarekisteri;
        // naytaJasen();
    }


    /**
     * Täyttää ottelijan tiedot kenttiin
     * @param ottelija 
     */
    private void taytatiedot(Ottelija ottelija) {
        textOttelijaNimi.setText(ottelija.getNimi());
        textOttelijaPaino.setText(ottelija.getPaino());
        textOttelijaSeura.setText(ottelija.getSeura());
        textOttelijaRekordi.setText(ottelija.getRekordi());
        textOttelijaAktiivisuus.setText(ottelija.getAktiivisuus());
        textOttelijaDoping.setText(ottelija.getDoping());

        listOttelut.clear();
        List<Ottelu> ottelut;

        try {
            ottelut = potkukantarekisteri.annaOttelut(ottelija);
            int index = 0;
            int i = 0;
            for (Ottelu ottelu : ottelut) {
                index = i;
                listOttelut.addExample(ottelu.toString());
                i++;

            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog(
                    "Jäsenen hakemisessa ongelmia! " + ex.getMessage());
        }

    }


    /*
     * Luo uuden ottelijan ikkunan joghon ottelijan tietoja syötetään. *
     * Rekisteröi ottelijan = antaa uniikin jasen ID:n
     */
    protected void uusiOttelija() {
        Ottelija lisattava = new Ottelija();
        lisattava.rekisteroi();
        LisaaOttelijaGUIController.uusiOttelija(lisattava);

    }


    /**
    
     * Lisaa ottelian rekisteriin 
     * @param ottelija  ottelija
     */
    protected static void lisaaOttelija(Ottelija ottelija) {
        if (ottelija == null)
            return;

        try {
            potkukantarekisteri.lisaaTaiKorvaa(ottelija);

        } catch (SailoException e) {
            Dialogs.showMessageDialog(
                    "Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }

    }


    /*
     * Vie listasta valitun ottelijan Uuden luonti kontrollerille, joka t'ytt''
     * j'senen tiedot muokkkausikkunaan
     */
    private void muokkaaOttelijaa() {
        ottelijaKohdalla = listHakutulokset.getSelectedObject();
        if (ottelijaKohdalla == null) {
            return;
        }
        ottelijaKohdalla.tulosta(System.out);
        LisaaOttelijaGUIController.muokkaaOttelijaa(ottelijaKohdalla);

    }

    /*
     * Luo uuden ottelun ja rekisteröi sen. Ottelu toistaiseksi testiottelu
     * rekisterin kahdesta ensimmaisesta ottelijasta. Lisaa ottelun rekisteriin.
     */

    public boolean kotivalittu = false;
    public Ottelija koti;
    public Ottelija vieras;

    protected void uusiOttelu() {

        if (kotivalittu != true) {
            koti = listHakutulokset.getSelectedObject();
            kotivalittu = true;
            Dialogs.showMessageDialog(
                    "1. Ottelija: " + ottelijaKohdalla.getNimi()
                            + " valittu, valitse seuraava");
            return;
        }
        Ottelu uusi = new Ottelu();
        uusi.rekisteroi();
        uusi.tulosta(System.out);
        vieras = listHakutulokset.getSelectedObject();
        kotivalittu = false;
        UusiOtteluGUIController.uusiOttelu(uusi, koti, vieras);

    }


    /** Lisää ottelun
    * @param ottelu lisättävä tai korvattava ottelu
    * @throws SailoException jos täynnä
    */
    protected static void lisaaOttelu(Ottelu ottelu) throws SailoException {
        if (ottelu == null)
            return;

        potkukantarekisteri.lisaaTaiKorvaa(ottelu);
    }


    /*
     * Muuttaa valitun ottelijan tiedot tyhjäksi.
     */
    private void poistaOttelija() {
        ottelijaKohdalla = listHakutulokset.getSelectedObject();
        if (ottelijaKohdalla == null) {
            return;
        }
        ottelijaKohdalla.tulosta(System.out);
        ottelijaKohdalla.tyhjennaOttelija();

    }


    /**
     * Hakee jäsenten tiedot listaan
     * 
     * @param jnro jäsenen numero, joka aktivoidaan haun jälkeen
     */
    protected void hae(int jnro) {

        String ehto = textHakuehto.getText();
        listHakutulokset.clear();

        int index = 0;
        Collection<Ottelija> ottelijat;

        try {
            ottelijat = potkukantarekisteri.etsi(ehto);

            int i = 0;
            for (Ottelija ottelija : ottelijat) {
                if (ottelija.getID() == jnro)
                    index = i;
                listHakutulokset.add(ottelija.getNimi(), ottelija);
                i++;

            }
        } catch (SailoException ex) {
            Dialogs.showMessageDialog(
                    "Jäsenen hakemisessa ongelmia! " + ex.getMessage());
        }
        listHakutulokset.setSelectedIndex(index); // tästä tulee muutosviesti
                                                  // joka näyttää jäsenen
    }


    /**
     * Avaa ohjelman suunnitelman erilliseen selaimeen
     */
    private void apua() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI(
                    "https://tim.jyu.fi/view/kurssit/tie/ohj2/2020k/ht/tovarita");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }


    /**
     * Päivittää ottelijoiden rekordit ottelun tuloksen perusteella.
     * @param koti = kotiottelija
     * @param vieras = vierasottelija
     * @param kotitulos = kotitulos
     * @param vierastulos = vierastulos
     */
    public static void paivitaRekordi(int koti, int vieras, boolean kotitulos,
            boolean vierastulos) {
        Ottelija kotiOttelija = potkukantarekisteri.annaOttelija(koti - 1);
        Ottelija vierasOttelija = potkukantarekisteri.annaOttelija(vieras - 1);

        if (kotitulos) {
            kotiOttelija.paivitaRekordia(0);
            vierasOttelija.paivitaRekordia(2);
        }
        if (vierastulos) {
            vierasOttelija.paivitaRekordia(0);
            kotiOttelija.paivitaRekordia(2);
        }

        if (kotitulos == false && vierastulos == false) {
            kotiOttelija.paivitaRekordia(1);
            vierasOttelija.paivitaRekordia(1);
        }

        try {
            potkukantarekisteri.lisaaTaiKorvaa(vierasOttelija);
            potkukantarekisteri.lisaaTaiKorvaa(kotiOttelija);
        } catch (SailoException e) {
            e.printStackTrace();
        }

    }

}
