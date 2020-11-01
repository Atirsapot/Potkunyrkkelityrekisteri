package fxPotkukanta;

import Potkukanta.Ottelija;
import Potkukanta.Ottelu;
import Potkukanta.SailoException;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UusiOtteluGUIController
        implements ModalControllerInterface<Ottelu> {
    /**
     * 
     * @author topias & joona
     * @versio 7.0 5.5.2020
     *
     */

    @FXML
    private TextField textOttelunNimi;

    @FXML
    private TextField textKotiSeura;

    @FXML
    private TextField textKotiPaino;

    @FXML
    private TextField textVierasSeura;

    @FXML
    private TextField textVierasPaino;

    @FXML
    private CheckBox boxVoittoKoti;

    @FXML
    private CheckBox boxVoittoVieras;

    @FXML
    private TextField textTulostyyppi;

    @FXML
    private TextField textKotiOttelija;

    @FXML
    private TextField textVierasOttelija;

    @FXML
    private TextField textOttelunPvm;

    @FXML
    private Button buttonTallenna;

    public static Ottelu ottelu;
    public static int otteluID;
    public static int koti;
    public static int vieras;

    @FXML
    // Tallentaa ottelun tiedot
    void handleTallennaOttelu() throws SailoException {

        ottelu = new Ottelu(otteluID, textOttelunNimi.getText(),
                textKotiOttelija.getText(), textVierasOttelija.getText(),
                textKotiSeura.getText(), textVierasSeura.getText(),
                textKotiPaino.getText(), textVierasPaino.getText(), koti,
                vieras, boxVoittoKoti.isSelected(),
                boxVoittoVieras.isSelected(), textTulostyyppi.getText(),
                textOttelunPvm.getText());

        System.out.println("===========ottelutestit=========");
        System.out.println(ottelu.getKJID());
        System.out.println(ottelu.getVJID());

        if (boxVoittoKoti.isSelected() == true
                && boxVoittoVieras.isSelected() == true) {
            System.out.println("molemmat ei voi voittaa!!");
            // lopetusehto??
        }
        PaaikkunaGUIController.lisaaOttelu(ottelu);
        PaaikkunaGUIController.paivitaRekordi(ottelu.getKJID(),
                ottelu.getVJID(), boxVoittoKoti.isSelected(),
                boxVoittoVieras.isSelected());
        Stage ikkuna = (Stage) buttonTallenna.getScene().getWindow();
        ikkuna.close();
    }


    /**
     * Käsittelee uuden ottelun luomisen
     * @param uusi = uusi ottelu
     * @param kotiOttelija 
     * @param vierasOttelija
     * @return otteluCTRL
     */
    public static UusiOtteluGUIController uusiOttelu(Ottelu uusi,
            Ottelija kotiOttelija, Ottelija vierasOttelija) {
        UusiOtteluGUIController otteluCTRL = ModalController
                .showModeless(UusiOtteluGUIController.class
                        .getResource("UusiOtteluGUIView.fxml"), "Ottelu", uusi);

        otteluCTRL.textKotiOttelija.setText(kotiOttelija.getNimi());
        otteluCTRL.textKotiSeura.setText(kotiOttelija.getSeura());
        otteluCTRL.textKotiPaino.setText(kotiOttelija.getPaino());

        otteluCTRL.textVierasOttelija.setText(vierasOttelija.getNimi());
        otteluCTRL.textVierasSeura.setText(vierasOttelija.getSeura());
        otteluCTRL.textVierasPaino.setText(vierasOttelija.getPaino());

        koti = kotiOttelija.getID();
        vieras = vierasOttelija.getID();

        System.out.println(kotiOttelija.getID());
        System.out.println(vierasOttelija.getID());

        System.out.println(koti + " " + vieras);

        otteluID = uusi.getOID();
        ottelu = uusi;

        return otteluCTRL;
    }

    /*
     * ModalCTRL metodit
     */


    @Override
    public Ottelu getResult() {
        // tulos
        return null;
    }


    @Override
    public void handleShown() {
        // esitys

    }


    @Override
    public void setDefault(Ottelu oletus) {
        // oletus

    }

}
