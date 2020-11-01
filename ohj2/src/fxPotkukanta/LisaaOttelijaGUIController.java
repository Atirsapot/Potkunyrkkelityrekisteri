package fxPotkukanta;

import fxPotkukanta.PaaikkunaGUIController;
import Kanta.Tarkistaja;
import Potkukanta.Ottelija;
import Potkukanta.Potkukantarekisteri;
import Potkukanta.SailoException;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LisaaOttelijaGUIController implements ModalControllerInterface<Ottelija> {
	/**
	 * 
	 * @author topias & joona
	 * @versio 7.0 5.5.2020
	 *
	 */
	@FXML
	private TextField textOttelijaNimi;
	@FXML
	private TextField textOttelijaSeura;
	@FXML
	private TextField textOttelijaPaino;
	@FXML
	private TextField textOttelijaRekordi;
	@FXML
	private CheckBox choiceAktiivisuus;
	@FXML
	private CheckBox choiceDoping;
	@FXML
	private TextField textOttelijaIka;
	@FXML
	private Button buttonTallenna;

	private Potkukantarekisteri potkukantarekisteri;

	public static Ottelija ottelija;
	public static int ottelijaID;

	// tallentaa ottelijan tiedot
	@FXML
	void handleTallennaOttelija() throws SailoException {

		ottelija = new Ottelija(ottelijaID, textOttelijaNimi.getText(), textOttelijaSeura.getText(),
				textOttelijaIka.getText(), textOttelijaRekordi.getText(), textOttelijaPaino.getText(),
				choiceAktiivisuus.isSelected(), choiceDoping.isSelected());

		ottelija.tulosta(System.out);
		System.out.println("============= Ottelija testi ======================");

		Tarkistaja tarkistaja = new Tarkistaja();
		if (tarkistaja.tarkistaNimi(ottelija.getNimi()) == true && tarkistaja.tarkistaIka(ottelija.getIka()) == true
				&& tarkistaja.tarkistaPaino(ottelija.getPaino()) == true) {
			PaaikkunaGUIController.lisaaOttelija(ottelija);
			// ikkunan sulkeminen tallennuksen jälkeen
			Stage ikkuna = (Stage) buttonTallenna.getScene().getWindow();
			ikkuna.close();

		}

		else {
			Dialogs.showMessageDialog("Tarkista vielä oikeinkirjoitus");
		}

	}

	/**
	 * Käsittelee uuden ottelijan lisäyksen
	 * 
	 * @param lisattava = lisattava ottelija
	 * @return ottelijaCTRL
	 */
	public static LisaaOttelijaGUIController uusiOttelija(Ottelija lisattava) {
		LisaaOttelijaGUIController ottelijaCTRL = ModalController.showModeless(
				LisaaOttelijaGUIController.class.getResource("LisaaOttelijaGUIView.fxml"), "Ottelija", lisattava);

		ottelijaID = lisattava.getID();
		ottelija = lisattava;
		return ottelijaCTRL;
	}

	/**
	 * @param ottelijaKohdalla valitsee käsiteltäväksi kohteeksi valitun ottelijan
	 * @return ottelijaCTRL
	 */
	public static LisaaOttelijaGUIController muokkaaOttelijaa(Ottelija ottelijaKohdalla) {
		LisaaOttelijaGUIController ottelijaCTRL = ModalController.showModeless(
				LisaaOttelijaGUIController.class.getResource("LisaaOttelijaGUIView.fxml"), "Ottelija",
				ottelijaKohdalla);
		ottelijaID = ottelijaKohdalla.getID();
		ottelija = ottelijaKohdalla;

		ottelijaCTRL.textOttelijaNimi.setText(ottelijaKohdalla.getNimi());
		ottelijaCTRL.textOttelijaSeura.setText(ottelijaKohdalla.getSeura());
		ottelijaCTRL.textOttelijaIka.setText(ottelijaKohdalla.getIka());
		ottelijaCTRL.textOttelijaRekordi.setText(ottelijaKohdalla.getRekordi());
		ottelijaCTRL.textOttelijaPaino.setText(ottelijaKohdalla.getPaino());
		ottelijaCTRL.choiceAktiivisuus.setSelected(Boolean.parseBoolean(ottelijaKohdalla.getAktiivisuus()));
		ottelijaCTRL.choiceDoping.setSelected(Boolean.parseBoolean(ottelijaKohdalla.getDoping()));

		return ottelijaCTRL;

	}

	// ModalCTRL metodeja
	@Override
	public Ottelija getResult() {

		return null;
	}

	@Override
	public void handleShown() {
		//

	}

	@Override
	public void setDefault(Ottelija oletus) {
		if (oletus == null)
			return;
	}

}
