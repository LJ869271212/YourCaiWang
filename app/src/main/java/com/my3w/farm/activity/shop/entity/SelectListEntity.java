package com.my3w.farm.activity.shop.entity;

import java.util.Arrays;

public class SelectListEntity {
	private SelectValueEntity[] varieties;
	private SelectValueEntity[] address;
	private SelectValueEntity[] growing;

	public SelectValueEntity[] getVarieties() {
		return varieties;
	}

	public void setVarieties(SelectValueEntity[] varieties) {
		this.varieties = varieties;
	}

	public SelectValueEntity[] getAddress() {
		return address;
	}

	public void setAddress(SelectValueEntity[] address) {
		this.address = address;
	}

	public SelectValueEntity[] getGrowing() {
		return growing;
	}

	public void setGrowing(SelectValueEntity[] growing) {
		this.growing = growing;
	}

	@Override
	public String toString() {
		return "SelectListEntity [varieties=" + Arrays.toString(varieties) + ", address=" + Arrays.toString(address) + ", growing="
				+ Arrays.toString(growing) + "]";
	}

}
