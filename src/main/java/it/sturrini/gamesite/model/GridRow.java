package it.sturrini.gamesite.model;

import java.io.Serializable;
import java.util.List;

public class GridRow<T extends Serializable> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -640346031448488197L;

	private List<T> items;

	private int rowNum;

	public GridRow() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GridRow(List<T> items, int rowNum) {
		super();
		this.items = items;
		this.rowNum = rowNum;
	}

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}

	public void addItem(T item) {
		this.items.add(item);
	}

	public void removeItem(T item) {
		if (items.contains(item)) {
			items.remove(item);
		}
	}

	public int size() {
		return items.size();
	}

}
