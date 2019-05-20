package it.sturrini.gamesite.model;

/**
 * Tutte le entità che si possono salvare su Mongo
 *
 * @author sturrini
 */
public enum EntitiesEnum {

	Player("Player", "players"), Map("Map", "maps"), MapElement("Map Element", "mapelements");

	private EntitiesEnum(String label, String collectionName) {
		this.label = label;
		this.collectionName = collectionName;
	}

	private String label;
	private String collectionName;

	public String label() {
		return label;
	}

	public String collectionName() {
		return collectionName;
	}

}
