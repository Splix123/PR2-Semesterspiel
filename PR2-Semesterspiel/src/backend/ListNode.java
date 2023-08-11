package backend;

import java.io.Serializable;

/**
 * Diese Klasse erstellt einen Listenknoten für eine List.
 * 
 * @author Nicole Shiner
 * @param <T> Der Generische Typ der Liste
 */
public class ListNode<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	/** Instanz des generischen Knoten */
	private T data;
	private ListNode<T> next;

	/**
	 * Default Konstruktor für den Knoten.
	 * 
	 * @param data Daten des Knotens
	 * @param next Referenz zum nächsten Knoten
	 */
	public ListNode(T data, ListNode<T> next) {
		this.setData(data);
		this.setNext(next);
	}

	/**
	 * Gibt die Daten des Knoten zurück.
	 * 
	 * @return Daten des Knoten
	 */
	public T getData() {
		return data;
	}

	/**
	 * Weist die übergebenen Daten dem Knoten zu.
	 * 
	 * @param data die zu Daten die dem Knoten zugewiesen werden sollen
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * Ruft den nächsten Knoten auf.
	 * 
	 * @return Referenz zum nächsten Knoten
	 */
	public ListNode<T> getNext() {
		return next;
	}

	/**
	 * setzt den nächsten Knoten.
	 * 
	 * @param next speichert die Referenz zum nächsten Objekt als next
	 */
	public void setNext(ListNode<T> next) {
		this.next = next;
	}

	/**
	 * Überschreibt die toString-Methode für eine angepasste Konsolenausgabe.
	 */
	@Override
	public String toString() {
		if (this.getData() == null) {
			return null;
		}
		return this.getData().toString();
	}
}
