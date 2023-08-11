package gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Diese Klasser bildet einen custom <code>JFileChooser</code>, der zum Laden
 * und zum speichern benutzt werden kann, sowie auf Deutsch übersetzt ist.
 * 
 * @author Moritz Rühm
 */
public class FileChooser extends JFileChooser {
	private static final long serialVersionUID = 1L;

	/**
	 * Hier wird der FileChooser entweder Speichernd, oder Ladend gebildet.
	 * 
	 * @param speichernd Ein boolean-wert, der determiniert, ob ein Speicher- oder
	 *                   LadenFileChooser angezeigt wird
	 */
	public FileChooser(boolean speichernd) {
		this.setCurrentDirectory(
				new File(System.getProperty("user.home") + System.getProperty("file.separator") + "Desktop"));
		UIManager.put("FileChooser.cancelButtonText", "Abbrechen");
		UIManager.put("FileChooser.filesOfTypeLabelText", "Dateityp");
		if (speichernd) { // funktioniert nicht
			this.setDialogTitle("Speichern");
			UIManager.put("FileChooser.saveButtonText", "Speichern");
			UIManager.put("FileChooser.saveInLabelText", "Speichern als");
			this.setAcceptAllFileFilterUsed(false);
			FileNameExtensionFilter extFilterSer = new FileNameExtensionFilter("Serialisiert", "ser");
			this.addChoosableFileFilter(extFilterSer);
			FileNameExtensionFilter extFilterCsv = new FileNameExtensionFilter("CSV", "csv");
			this.addChoosableFileFilter(extFilterCsv);
		} else {
			this.setDialogTitle("Laden");
			UIManager.put("FileChooser.openButtonText", "Laden");
			SwingUtilities.updateComponentTreeUI(this);
			FileNameExtensionFilter extFilterBeide = new FileNameExtensionFilter("Serialisiert oder CSV", "ser", "csv");
			this.setAcceptAllFileFilterUsed(false);
			this.addChoosableFileFilter(extFilterBeide);
		}
	}
}