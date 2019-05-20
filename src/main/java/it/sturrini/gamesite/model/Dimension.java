package it.sturrini.gamesite.model;

import java.io.Serializable;

public class Dimension implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 6102096345037190895L;

	private Long x;
	private Long y;

	public Dimension() {
		super();

	}

	public Dimension(Long x, Long y) {
		super();
		this.x = x;
		this.y = y;
	}

	public Long getX() {
		return x;
	}

	public Long getY() {
		return y;
	}

	@Override
	public String toString() {
		return "Dimension [x=" + x + ", y=" + y + "]";
	}

	public void setX(Long x) {
		this.x = x;
	}

	public void setY(Long y) {
		this.y = y;
	}

}
