package com.ssinfo.corider.app.models;

import java.util.List;

public class ConfigInfo {
	private List<Info> configList;

	public List<Info> getConfigList() {
		return configList;
	}

	public void setConfigList(List<Info> configList) {
		this.configList = configList;
	}

	public class Info {
		private String key;
		private String value;
		private String id;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

	}

}
