package com.my3w.farm.activity.shop.entity;

import java.util.Arrays;

public class SelectValueEntity {

	private String key;
	private String name;
	private String value;
	private SelectValueClassLevelEntity[] classs;
	private SelectValueClassLevelEntity[] level;

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

	public SelectValueClassLevelEntity[] getClasss() {
		return classs;
	}

	public void setClasss(SelectValueClassLevelEntity[] classs) {
		this.classs = classs;
	}

	public SelectValueClassLevelEntity[] getLevel() {
		return level;
	}

	public void setLevel(SelectValueClassLevelEntity[] level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "SelectValueEntity [key=" + key + ", name=" + name + ", value=" + value + ", classs=" + Arrays.toString(classs) + ", level="
				+ Arrays.toString(level) + "]";
	}

}
