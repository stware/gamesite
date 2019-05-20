package it.sturrini.gamesite.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import it.sturrini.gamesite.dao.mongo.LongDeserializer;

public class Player extends BaseEntity implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 325353952872393264L;

	protected String nickname;

	protected String password;

	protected String email;

	@JsonDeserialize(using = LongDeserializer.class)
	protected Long score;

	protected String cityId;

	public Player() throws InstantiationException, IllegalAccessException {
		super();
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@JsonDeserialize(using = LongDeserializer.class)
	public Long getScore() {
		return score;
	}

	public void setScore(Long points) {
		this.score = points;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Player) {
			return ((Player) obj).getNickname().equals(this.getNickname()) || ((Player) obj).getEmail().equals(this.getEmail());
		} else {
			return super.equals(obj);
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	@JsonIgnore
	public void addPoints(Long points) {
		if (this.score == null) {
			this.score = 0L;
		}
		this.score += points;
	}

	@JsonIgnore
	public void removePoints(Long points) {
		if (this.score == null) {
			this.score = 0L;
		}
		this.score -= points;
		if (this.score < 0) {
			this.score = 0L;
		}
	}

}
