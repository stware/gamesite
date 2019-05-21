package it.sturrini.gamesite.events;

public enum Event {

	create("Create"), move("Move"), delete("Delete"), update("Update"), login("Login");

	private Event(String type) {
		this.type = type;
	}

	private String type;

	public String getType() {
		return type;
	}

}
