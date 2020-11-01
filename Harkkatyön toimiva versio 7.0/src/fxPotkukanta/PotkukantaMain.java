package fxPotkukanta;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import Potkukanta.Potkukantarekisteri;

/**
 * @author topiasrita & joonahuttunen
 * @version 7.0  5.5.2020
 * 
 * Pääohjelma Potkukantarekisterin käynnistämiseksi
 */

public class PotkukantaMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {

            FXMLLoader ldr = new FXMLLoader(
                    getClass().getResource("PaaikkunaGUIView.fxml"));

            final Pane root = ldr.load();
            final PaaikkunaGUIController potkukantaCtrl = (PaaikkunaGUIController) ldr
                    .getController();

            Scene scene = new Scene(root);
            // scene.getStylesheets().add(getClass().getResource("potkukanta.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Potkukanta");

            Potkukantarekisteri potkukantarekisteri = new Potkukantarekisteri();
            potkukantaCtrl.setPotkukanta(potkukantarekisteri);

            primaryStage.show();

            Application.Parameters params = getParameters();
            potkukantaCtrl.lueTiedosto();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Käynnistetään käyttöliittymä
     */
    public static void main(String[] args) {
        launch(args);
    }
}