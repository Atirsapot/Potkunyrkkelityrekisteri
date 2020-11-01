# Potkunyrkkelitytulosrekisteri
2020 Java fullstack harjoitustyö.
 
Työn suunnitelma, esittely ja muuta lisätietoa osoitteessa https://tim.jyu.fi/view/kurssit/tie/ohj2/2020k/ht/tovarita

Ohjelman avulla tallennetaan potkunyrkkeilyottelijoita sekä -otteluita.

## Mitä tietoja ottelijoista tallennetaan?

- nimi
- painoluokka
- seura
- ikäluokka
- doping historia

## Mitä tietoja otteluista tallennetaan?

- Ottelun nimi
- Kotiottelijan nimi
- Vierasottelijan nimi
- Kotiseura
- Vierasseura
- Kotiottelijan paino
- Vierasottelijan paino
- Koti id
- Vieras id
- Kotiottelijan tulos ( voitto / häviö)
- Vierasottelijan tulos ( voitto /häviö)
- Lopputuloksen tyyppi (tuomaripisteet, tyrmäys, jne...)
- Päivämäärä

## Mitä ominaisuuksia rekisteriltä halutaan?

- Ottelijan lisääminen / nimen poistaminen
- tietyn ottelijan tietojen hakeminen ja muuttaminen
- Ottelutulosten tarkastelu
- Ottelutuloksen lisääminen
    - ottelutulos päivittää osallistujien rekordia automaattisesti

## Tallennustiedostojen muoto
Ohjelman tiedot tallennetaan seuraavanlaisiin tekstitiedostoihin:

`ottelijat.dat` - relaatiokannan päätaulu
```
Ottelijat
;jid|sukunimi etunimi|sukupuoli|painoluokka|seura|ikä|  Aktiivisuus  |doping|
  1 |Huttunen Joona  |  mies   |    75     | JPN | M |  Aktiivinen   |  -   |
  2 |Seppänen Tommi  |  mies   |    85     | JPN | M |  Aktiivinen   |  -   |
  3 |Walaström Eeva  |  nainen |    65     |TNPN | N |  Aktiivinen   |  -   |
  4 |Viineri Veeti   |  mies   |    45     | KPN | A|  Aktiivinen   |  -   |
  5 |Roinatan Joonas |  mies   |    91     | SMC | M |  Epäaktiivinen|  +   |
  ...
```

`ottelut.dat` - ottelut relaation avulla

```
Ottelut
 ;oid|seura|kjid|vjid|tulos|ttid|pvm       |
   1 | JPN |  1 | 7  |  1  |  1 |01.01.2001|
   2 | TNPN|  3 | 8  |  2  |  3 |01.02.2001|
  ...
```

```
oid : Ottelun tunniste
kjid: Kotiottelijan jäsen ID
vjid: Vierasottelijan jäsen ID
tulos: Ottelun lopputulos; 1 - koti voittaa, X - tasapeli, 2 - Vieras voittaa
ttid: Ottelun lopputuloksen tyyppi: TKO - tekninen tyrmäys, DNF - diskaus, jne...
pvm: ottelun päivämäärä
```

`tulostyyppi.dat` - lopputuloksen tyyppi relaation avulla

```
Tulostyyppi
;ttid|teksti         |
   1 |Tuomaripisteet |
   2 |TKO            |
   3 |KO             |
   4 |DNF            |
   5 |FOR            |
   6 |INJ            |
  ...
```

## CRC-kortit

```
Potkukanta -luokka
+--------------------------------------+--------------------------------------+
| Luokan nimi: Potkukantarekisteri     | Avustajat:                           |
+--------------------------------------+--------------------------------------+
| Vastuualueet:                        | - Ottelijat                          |
|                                      | - Ottelut                            |
| - pitää yllä varsinaista rekisteriä, |                                      |
| eli osaa lisätä ja poistaa ottelijan |                                      |
| sekä ottelun   					   |									  |
|				                       |                                      |
| - lukee ja kirjoittaa potkukannan    |                                      |
| tiedostoon                           |                                      |
|                                      |                                      |
| - osaa etsiä ja lajitella            |                                      |
|                                      |                                      |
|                                      |                                      |
|                                      |                                      |
|                                      |                                      |
+--------------------------------------+--------------------------------------+

Ottelija -luokka
| Luokan nimi: Ottelija                | Avustajat:                           |
+--------------------------------------+--------------------------------------+
| Vastuualueet:                        |                                      |
|                                      |                                      |
| (- ei tiedä potkukannasta, eikä      |                                      |
| käyttöliittymästä)                   |                                      |
|                                      |                                      |
| - tietää ottelijan kentät (nimi, ikä |                                      |
| paino, jne.)                         |                                      |
|                                      |                                      |
| - osaa tarkistaa tietyn kentän       |                                      |
| oikeellisuuden (syntaksin)           |                                      |
|                                      |                                      |
| - osaa muuttaa |4|Veeti Viineri||...||                                      |
| merkkijonon ottelijan tiedoiksi      |                                      |
|                                      |                                      |
| - osaa antaa merkkijonona i:n kentän |                                      |
| tiedot                               |                                      |
|                                      |                                      |
| - osaa laittaa merkkijonon i:neksi   |                                      |
| kentäksi                             |                                      |
|                                      |                                      |
|                                      |                                      |
+--------------------------------------+--------------------------------------+

Ottelu -luokka

+--------------------------------------+--------------------------------------+
| Luokan nimi: Ottelu	               | Avustajat:                           |
+--------------------------------------+--------------------------------------+
| Vastuualueet:                        |                                      |
|                                      |                                      |
| (- ei tiedä potkukannasta, eikä      |                                      |
| käyttöliittymästä)                   |                                      |
|                                      |                                      |
| - tietää ottelun kentät (nimet, tulos|                                      |
|  jne.)                	      	   |                                      |
|                                      |                                      |
| - osaa tarkistaa tietyn kentän       |                                      |
| oikeellisuuden (syntaksin)           |                                      |
|                                      |                                      |
| - osaa muuttaa |4|Veeti Viineri||...||                                      |
| merkkijonon ottelun tiedoiksi        |                                      |
|                                      |                                      |
| - osaa antaa merkkijonona i:n kentän |                                      |
| tiedot                               |                                      |
|                                      |                                      |
| - osaa laittaa merkkijonon i:neksi   |                                      |
| kentäksi                             |                                      |
|                                      |                                      |
|                                      |                                      |
+--------------------------------------+--------------------------------------+

Näyttö -luokka
+--------------------------------------+--------------------------------------+
| Luokan nimi:Naytto                   | Avustajat:                           |
+--------------------------------------+--------------------------------------+
| Vastuualueet:                        | - Ottelijat                          |
|                                      | - Ottelut                            |
| - hoitaa kaiken näyttöön tulevan     | - Potkukantarekisteri                |
| tekstin                              |                                      |
|   								   | 									  | 
|  - hoitaa tiedon syötön              |                                      |
| - hoitaa kaiken tiedon pyytämisen    |                                      |
| käyttäjältä                          |                                      |
|                                      |                                      |
| (-  ei tiedä potkukannan,ottelijan   |                                      |
|  eikä ottelun yksityiskohtia)        |                                      |
|                                      |                                      |
|                                      |                                      |
|                                      |                                      |
|                                      |                                      |
|                                      |                                      |
|______________________________________|______________________________________|

Ottelijat -luokka

+--------------------------------------+--------------------------------------+
| Luokan nimi: Ottelijat               | Avustajat:                           |
+--------------------------------------+--------------------------------------+
| Vastuualueet:                        | - Ottelija                           |
|                                      |                                      |
| - pitää yllä varsinaista             |                                      |
| ottelijarekisteriä, eli osaa lisätä| |                                   	  |
| ja poistaa  ottelijan                |                                      |
|                                      |                                      |
| - lukee ja kirjoittaa ottelijat      |                                      |
| tiedostoon                           |                                      |
|                                      |                                      |
| - osaa etsiä ja lajitella ottelijoita|                                      |
|                                      |                                      |
|                                      |                                      |
|                                      |                                      |
|______________________________________|______________________________________|

Ottelut -luokka

+--------------------------------------+--------------------------------------+
| Luokan nimi:Ottelut                  | Avustajat:                           |
+--------------------------------------+--------------------------------------+
| Vastuualueet:                        | - Ottelu                             |
|                                      |                                      |
| - pitää yllä varsinaista             |                                      |
| ottelurekisteriä, eli osaa lisätä ja |                                      |
| poistaa ottelun                      |                                      |
|                                      |                                      |
| - lukee ja kirjoittaa ottelijat      |                                      |
| tiedostoon                           |                                      |
|                                      |                                      |
| - osaa etsiä ja lajitella otteluita  |                                      |
|                                      |                                      |
|                                      |                                      |
|                                      |                                      |
|______________________________________|______________________________________|


```

Tietorakennekuva:
![](%%raw%%kuvat/TIETORAKENNEKUVA.png)


# Ohjelman käyttö

## Ohjelman käynnistys.
Ohjelma käynnistetään klikkaamalla Ottelukanta.jar-ikonia tai antamalla komentoriviltä komento
```
java -jar ottelukanta.jar
```
## Pääikkuna
Kun ohjelma on käynnistynyt on näkyvillä ohjelman pääikkuna:

![](%%raw%%kuvat/PaaikkunaSB.png)

Ottelija lisätään ikkunasta: 

![](%%raw%%kuvat/LisaaOttelijaSB.png)

Uusi ottelu lisätään ikkunasta: 
![](%%raw%%kuvat/OtteluikkunaSB.png)

\
Pääikkunassa on seuraava menurakenne:
```
Tiedosto         Muokkaa                 Apua
========         =======                 =====
Listaa...        Lisää ottelija          Ohje
Tallenna         Uusi ottelu             Tietoja                      
Lopeta         

```

### Hakeminen
Pääikkunan vasemmassa reunassa näkyy Hakuehto.  Tästä
voi valita minkä kentän mukaan etsitään (Ottelijat tai Ottelut).  Tämän jälkeen
tekstikenttään voi syöttää hakuehdon ja listaan tulee vain
ne ottelijat joille haku toteutuu.  Hakutermi saa löytyä valitusta
kentästä mistä kohti vaan.  Esimerkiksi jos kirjoitetaan
hakuehtoon <tt>s</tt>, niin haetaan kaikki jäsenet joiden nimessä
on s jossakin kohti.

### Muokkaaminen
Valittua jäsentä voidaan muokata menemällä tietoihin oikeaan
kohtaan ja kirjoittamalla uusi arvo.  Jos tietoon syötetään jotakin mikä
ei kelpaa, tulee tästä ilmoitus:

```
Esim paino muodosta: 45 kiloa

tulee ilmoittaa

"Ottelijan paino ilmoitetaan kokonaislukuna"
```

Ottelijaa muokataan ikkunasta: 

![](%%raw%%kuvat/MuokkaaOttelijaSB.png)

## Menutoiminnot yksityiskohtaisemmin

### Listaa...
Avaa uuden ikkunan jolle listataan ottelijoita määrätyillä hakuehdoilla.
Esim. 10 eniten voittanutta ottelijaa
Lista on mahdollista tulostaa .txt tiedostona

### Tallenna
Tallentaa kaikki muutokset.  Mahdollinen ottelija / ottelu jäsen tallentuu tiedostoihin.
Ohjelma kehottaa tarkastamaan oikeinkirjoituksen ennen tallentamista.
### Lisää ottelija & Uusi ottelu
Tyhjentää tekstikentät uuden ottelijan tai uuden ottelun lsiäämistä varten
Tiedot lisätään painamalla "Tallenna"

...

### Help
Avaa TIMin harjoitustyön suunnitelman verkkoselaimessa

