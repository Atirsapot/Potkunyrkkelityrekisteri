package Kanta;

/**
 * Tekee oikeellisuustarkistuksen
 * @author topias & joona
 * @versio 7.0
 */
public class Tarkistaja {
    // Rekisterin oma Gestapo

    // erikoismerkkien tarkistus?
    public static final String TARKISTUSMERKIT = "0123456789ABCDEFHJKLMNPRSTUVWXYÄÖabcdefghijiklmnopqrstuvxyzäö";
    public char[] ikaluokka = { 'A', 'B', 'C', 'M', 'N' };
    public String[] painoluokat = { "56", "58", "63.5", "67", "71", "75", "81", "86", "91" };

    /**
     * Tarkastaa nimen oikeellisuuden
     * @param nimi joka tarkastetaan
     * @return nimen tai virheilmoituksen
     */
    public boolean tarkistaNimi(String nimi) {
        for (int i = 0; i < nimi.length(); i++) {
            for (int j = 0; j < TARKISTUSMERKIT.length(); j++) {
                if (nimi.charAt(i) == TARKISTUSMERKIT.charAt(j)) {
                    return true;
                }

            }

        }
        System.out.println("Nimessä vääriä merkkejä");
        return false;
    }

    
    /**
     * Tarkistaa ikäluokan oikeellisuuden
     * @param ika tarkistettava ikaluokka
     * @return ikaluokka tai virheilmoitus
     */
    public boolean tarkistaIka(String ika) {
        if(ika.length()!= 1) {
        	System.out.println("Liikaa merkkejä tai väärä merkki");
            return false;
        }
        for(int i=0; i<ikaluokka.length; i++) {
            if(ika.charAt(0) == ikaluokka[i]) {
                return true;
            }
        }
        System.out.println("Ikä oli: " + ika);
        System.out.println("Sallitut iät: " + "A, B, C, M, N ");
        System.out.println("Liikaa merkkejä tai väärä merkki");
        return false;
    }


    /**
     * Tarkistaa painoluokan oikeellisuuden
     * @param paino tarkistettava paino
     * @return paino tai vihreilmoitus
     */
    public boolean tarkistaPaino(String paino) {
        for(int i = 0; i < painoluokat.length; i++) {
            if(paino.equals(painoluokat[i])) {
               return true;
            }
           
        } 
        System.out.println("virheellinen painoluokka");
        return false;
       
    }
    
    
}
