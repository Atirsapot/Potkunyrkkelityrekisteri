package Potkukanta;
/**
 * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille.
 * @author Topias Rita & Joona Huttunen
 * @version 7.0 5.5.2020
 */
public class SailoException extends Exception {
    private static final long serialVersionUID = 1L;


    /**
     * Poikkeuksen muodostaja jolle tuodaan poikkeuksessa
     * käytettävä viesti
     * @param viesti Poikkeuksen viesti
     */
    public SailoException(String viesti) {
        super(viesti);
    }
}