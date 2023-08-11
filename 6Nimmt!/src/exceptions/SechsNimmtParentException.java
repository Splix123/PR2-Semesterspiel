package exceptions;

import loadAndSave.LogFileTool;

/**
 * Dies ist die abstracte Eltern-Exception, von der alle unter-exceptions erben.
 * 
 * @author Nicole Shiner
 * @author Moritz Ruehm
 */
public abstract class SechsNimmtParentException extends Exception {
	private static final long serialVersionUID = -4452553261515810488L;
	LogFileTool log = new LogFileTool();

	/**
	 * Die Methode, welche die ID und den Fehler an <code>Exception</code> uebergibt
	 * und diese somit wirft.
	 * 
	 * @param id     Die ID die die Exception identifiziert und eine grobe Aussage
	 *               ueber ihre Funktionalitaet gibt
	 * @param fehler Die Fehlermeldung, welche beim catchen der Exception ausgegeben
	 *               werden soll
	 */
	public SechsNimmtParentException(int id, String fehler) {
		super("\nEin Fehler mit der ID " + id + " ist aufgetreten:\n" + fehler + "\n");
		
		log.stringEintragen("\nEin Fehler mit der ID " + id + " ist aufgetreten:\n" + fehler + "\\n");
	}

	/**
	 * Getter-Methode, welche die Fehlermeldung, sowie die Fehler-ID zurueck gibt.
	 * 
	 * @param id     Die ID die die Exception identifiziert und eine grobe Aussage
	 *               ueber ihre Funktionalitaet gibt
	 * @param fehler Die Fehlermeldung, welche beim catchen der Exception ausgegeben
	 *               werden soll
	 * @return Einen String, welcher den Fehler beinhaltet
	 */
	public String getMessage(int id, String fehler) {
		log.stringEintragen("\nEin Fehler mit der ID " + id + "ist aufgetreten:\n" + fehler + "\n");
		return "\nEin Fehler mit der ID " + id + "ist aufgetreten:\n" + fehler + "\n";
	}
}