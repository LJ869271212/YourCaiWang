package com.my3w.farm.activity.shop.entity;

public class SelectValueClassLevelEntity {

	private String key;
	private String name;
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "SelectValueClassLevelEntity [key=" + key + ", name=" + name + ", value=" + value + "]";
	}

}
