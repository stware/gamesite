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

}
