package backend_UnitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import backend.Geschlecht;
import backend.Karte;
import backend.Spiel;
import backend.Spieler;
import exceptions.ExceptionDB;
import exceptions.ExceptionDB.FalscheSpielerAnzahlException;
import exceptions.ExceptionDB.KeineElementeVorhandenException;
import exceptions.ExceptionDB.LogFileException;
import exceptions.ExceptionDB.NichtVorhandenException;
import exceptions.ExceptionDB.UngueltigeEingabeException;
import exceptions.ExceptionDB.ladenSpeichernFehlgeschlagenException;

/**
 * Dies ist die Testklasse zur KLasse <code>Spiel</code> in der JUnit basierte Tests durchgeführt werden.
 * 
 * @author Nicole Shiner
 * @author Rodez Tazo
 * @author Cindy Tuku
 */
public class TestSpiel {
	private Spiel spiel = null;
	String name1 = "Nina";
	String name2 = "Tom";

	/**
	 * Initialisierung eine Spiels zum Testen.
	 * Karte.zaehler wird gestartet.
	 */
	@Before
	public void setUp() {
		spiel = new Spiel();
		Karte.zaehler = 1;
	}
	
	/**
	 * Testet, ob es möglich ist, zwei verschiedene Spieler hinzuzufügen.
	 * 
	 * @throws UngueltigeEingabeException a
	 */
	@Test
	public void addZweiSpielerTest() throws UngueltigeEingabeException{
		Spieler spieler1 = new Spieler(name1, "1", spiel);
		Assert.assertEquals("Nina", spieler1.getName());
		//Assert.assertTrue(spiel.getAlleSpieler().contains(name1));
		//Assert.assertTrue(spiel.getErstellteSpieler().containsKey(name2));
	}
	
	/**
	 * Testet, ob das Geschlecht des angelegten Spielers auf weiblich gesetzt wurde.
	 * 
	 * @throws UngueltigeEingabeException a
	 */
	@Test
	public void geschlechtKiTest() throws UngueltigeEingabeException {
		Spieler spieler1 = new Spieler(name2, "4", spiel);
		assertEquals(Geschlecht.ki, spieler1.getGeschlecht());
	}
	
	/**
	 * Testet ob eine Exception zur Wahl des falsches Geschlechts geworfen wird.
	 * 
	 * @throws UngueltigeEingabeException a
	 */
	@SuppressWarnings("unused")
	@Test(expected=ExceptionDB.UngueltigeEingabeException.class)
	public void geschlechtUngueltigeEingabeTest() throws UngueltigeEingabeException {
		Spieler spieler1 = new Spieler(name2, "5", spiel);
	}
	
	/**
	 * Testet das Erstellen eines männlichen Spielers.
	 * 
	 * @throws UngueltigeEingabeException a
	 */
	@Ignore
	public void addSpielerMaennlichTest() throws UngueltigeEingabeException {
		spiel.addSpieler(name2, "1");
	}
	
	/**
	 * Testet, ob man bestimmte Spieler auch erntfernen kann.
	 * 
	 * @throws UngueltigeEingabeException a
	 * @throws NichtVorhandenException a
	 * @throws KeineElementeVorhandenException a
	 */
	@Test
	public void removeSpielerTest() throws UngueltigeEingabeException, NichtVorhandenException, KeineElementeVorhandenException {
		spiel.addSpieler(name1,"1");
		spiel.addSpieler(name2, "2");
		spiel.removeSpieler(name1);
		Assert.assertEquals(1, spiel.getAlleSpieler().length);
	}
	
	/**
	 * Es wird eine Exception erwartet wenn ein Spieler entfernt wird, der garnicht existiert.
	 */
	@Test 
	public void removeSpielerNichtVorhandenExceptionTest() {
			try {
				spiel.removeSpieler("TT");
			} catch (NichtVorhandenException e) {
				System.out.println("Es sind keine Spieler vorhanden");
			}
		
	}
	
//	/**
//	 *  Testet ob die passende Exception geworfen wird.
//	 */
//	@Test(expected=IndexOutOfBoundsException.class)
//	public void getSpielerAmZugVorSpielbeginn() {
//		spiel.getSpielerAmZug();
//	}
	
	
	/**
	 * Testet, ob es moeglich ist ein Spiel mit zwei Spielern richtig zu starten.
	 */
	@Test
	public void starteSpielTest() {
		try {
			spiel.addSpieler(name1, "1");
			spiel.addSpieler(name2, "1");
		} catch (UngueltigeEingabeException e) {
			System.out.println(e.getMessage());
		}
		assertEquals(false, spiel.istSpielBeendet()); 
	}

	/**
	 * Testet ob die Exception zur unpassenden Spieleranzahl geworfen wird.
	 * 
	 * @throws FalscheSpielerAnzahlException a
	 * @throws ladenSpeichernFehlgeschlagenException a
	 * @throws LogFileException a
	 */
	@Test(expected = FalscheSpielerAnzahlException.class)
	public void starteSpielSpielerAnzahlZuKleinException() throws FalscheSpielerAnzahlException, ladenSpeichernFehlgeschlagenException, LogFileException {
		spiel.starteSpiel();
	}
	
	/**
	 * Testet ob alle Spieler angemessen gespeichert sind.
	 * 
	 * @throws KeineElementeVorhandenException a
	 * @throws ladenSpeichernFehlgeschlagenException a
	 * @throws LogFileException a
	 */
	@Ignore
	public void getAlleSpielerTest() throws KeineElementeVorhandenException, ladenSpeichernFehlgeschlagenException, LogFileException {
		try {
			spiel.addSpieler(name1, "1");
			spiel.addSpieler(name2, "2");
		} catch (UngueltigeEingabeException e) {
			System.out.println(e.getMessage());
		}
		spiel.starteSpiel();
		Assert.assertEquals("Nina, Leben: 66", spiel.getAlleSpieler()[0].toString());
	}
	
	/**
	 * Testet, ob die Zugreihenfolge richtig funktioniert.
	 * 
	 * @throws ladenSpeichernFehlgeschlagenException a
	 * @throws LogFileException a
	 */
	@Ignore
	public void getSpielerAmZugTest() throws ladenSpeichernFehlgeschlagenException, LogFileException {
		try {
			spiel.addSpieler(name2, "1");
			spiel.addSpieler(name1, "1");
		} catch (UngueltigeEingabeException e) {
			System.out.println(e.getMessage());
		}
		spiel.starteSpiel();
		assertEquals("Tom, Leben: 66", spiel.getSpielerAmZug()[0].toString());
	}
	/**
	 * Testet ob der SPieler noch Karten hat wenn er am Zug ist.
	 * 
	 * @throws FalscheSpielerAnzahlException a
	 * @throws UngueltigeEingabeException a
	 * @throws ladenSpeichernFehlgeschlagenException a
	 * @throws LogFileException a
	 */
	@Test
	public void getKartenVonSpielerAmZugNOtNullTest() throws FalscheSpielerAnzahlException, UngueltigeEingabeException, ladenSpeichernFehlgeschlagenException, LogFileException {
		spiel.addSpieler(name2, "1");
		spiel.addSpieler(name1, "1");
		spiel.starteSpiel();
		System.out.println("here" +spiel.getSpielerAmZug()[0]);
		assertNotNull(spiel.getKartenVonSpielerAmZug());
	}	
	
	/**
	 * Testet ob das Ausspielen der Karten regulaer ablaeuft.
	 * @throws UngueltigeEingabeException a
	 * @throws FalscheSpielerAnzahlException a
	 * @throws KeineElementeVorhandenException a
	 * @throws NichtVorhandenException a
	 * @throws NumberFormatException a
	 * @throws ladenSpeichernFehlgeschlagenException a
	 * @throws LogFileException a
	 */
	@Ignore
	public void ausspielenTest() throws UngueltigeEingabeException, FalscheSpielerAnzahlException, KeineElementeVorhandenException, NumberFormatException, NichtVorhandenException, ladenSpeichernFehlgeschlagenException, LogFileException {
		spiel.addSpieler(name2, "1");
		spiel.addSpieler(name1, "1");
		spiel.starteSpiel();
		spiel.getSpielerAmZug();
		spiel.ausspielen(Integer.parseInt(spiel.getKartenVonSpielerAmZug()[0]));
		spiel.beendeZug();
		Assert.assertNotNull(spiel.getAusgespielt());
	}
	
//	/**
//	 * Testet ob die Exceptiom zum Nehmen einer nicht vorhandenen Reihe funktioniert.
//	 * @throws KeineElementeVorhandenException a
//	 */
//	@Test(expected=NullPointerException.class)
//	public void nimmReiheNichtVorhandenTest() throws KeineElementeVorhandenException {
//		try {
//			spiel.addSpieler(name1, "1");
//		} catch (UngueltigeEingabeException e) {
//			System.out.println(e.getMessage());
//		}
//		try {
//			spiel.nimmReihe(1);
//		} catch (NichtVorhandenException e) {
//			System.out.println(e.getMessage());
//		}	
//	}
}