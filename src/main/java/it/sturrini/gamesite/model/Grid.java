package it.sturrini.gamesite.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import it.sturrini.common.exception.GamesiteException;

public class Grid<T extends Serializable> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -640346031448488197L;

	private List<GridRow<T>> rows;

	private int blockSize;

	private Class clazz;

	public Grid() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Grid(Class clazz, int blockSize, int initRows, int initColumns) throws InstantiationException, IllegalAccessException {
		super();
		this.blockSize = blockSize;
		this.clazz = clazz;
		rows = new ArrayList<GridRow<T>>();
		for (int i = 0; i < initRows * blockSize; i++) {
			List<T> itms = new ArrayList<>(initColumns * blockSize);
			for (int j = 0; j < initColumns * blockSize; j++) {
				T newInstance = (T) clazz.newInstance();
				itms.add(newInstance);

			}
			GridRow<T> gr = new GridRow(itms, i);
			rows.add(gr);
		}

	}

	public void addItem(T item, int row, int column) throws GamesiteException {
		if (row >= rows.size()) {
			throw new GamesiteException();
		}
		GridRow<T> grow = rows.get(row);
		if (column >= grow.getItems().size()) {
			throw new GamesiteException();
		}
		grow.getItems().set(column, item);

	}

	public T getItem(int row, int column) throws GamesiteException {
		if (row >= rows.size()) {
			throw new GamesiteException();
		}
		GridRow<T> grow = rows.get(row);
		if (column >= grow.getItems().size()) {
			throw new GamesiteException();
		}
		T element = grow.getItems().get(column);
		return element;
	}

	public List<GridRow<T>> getRows() {
		return rows;
	}

	public void setRows(List<GridRow<T>> rows) {
		this.rows = rows;
	}

	@JsonIgnore
	public int getSizeX() {
		int maxX = 0;
		for (GridRow<T> gridRow : rows) {
			int size = gridRow.size();
			if (size > maxX) {
				maxX = size;
			}
		}
		return maxX;
	}

	@JsonIgnore
	public int getSizeY() {
		return rows.size();
	}

}
