package it.sturrini.gamesite.dao;

import java.util.HashMap;
import java.util.Map;

public class SearchFilter {

	public Map<String, Object> params;

	public SearchFilter() {
		super();
		params = new HashMap<>();
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public void addParam(String key, Object value) {
		if (this.params == null) {
			params = new HashMap<>();
		}
		params.put(key, value);
	}

}
