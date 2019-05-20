package it.sturrini.gamesite.model;

import java.io.Serializable;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import it.sturrini.gamesite.dao.mongo.LongDeserializer;
import it.sturrini.gamesite.dao.mongo.ObjectIdDeserializer;

@JsonPropertyOrder({ "name" })
public abstract class BaseEntity implements Serializable {

	/**
	 *
	 */
	@JsonIgnore
	private static final long serialVersionUID = 7926349503704068892L;

	@JsonDeserialize(using = ObjectIdDeserializer.class)
	private ObjectId _id;

	protected String id;

	@JsonProperty("name")
	protected String name;

	@JsonDeserialize(using = LongDeserializer.class)
	protected Long insertTimestamp;

	@JsonDeserialize(using = LongDeserializer.class)
	protected Long lastEditTimestamp;

	/**
	 * @return the _id
	 */
	// @JsonSerialize(using = ObjectIdSerializer.class)
	public ObjectId get_id() {
		if (_id == null && id == null) {
			return _id;
		}
		if (_id == null && id != null) {
			return new ObjectId(id);
		}
		return _id;
	}

	/**
	 * @param _id
	 *        the _id to set
	 */
	public void set_id(ObjectId _id) {
		this._id = _id;
		this.setId(_id.toString());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		if (id != null) {
			return id;
		}
		if (_id != null) {
			return _id.toHexString();
		}
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [id=" + id + "]";
	}

	public Long getInsertTimestamp() {
		return insertTimestamp;
	}

	public void setInsertTimestamp(Long insertTimestamp) {
		this.insertTimestamp = insertTimestamp;
	}

	public Long getLastEditTimestamp() {
		return lastEditTimestamp;
	}

	public void setLastEditTimestamp(Long lastEditTimestamp) {
		this.lastEditTimestamp = lastEditTimestamp;
	}

}
