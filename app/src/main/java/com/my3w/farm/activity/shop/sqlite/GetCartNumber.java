package com.my3w.farm.activity.shop.sqlite;

import android.content.Context;
import android.database.Cursor;

public class GetCartNumber {

	public static GetCartNumber getCartNumber;

	private Context context;

	public static GetCartNumber getInstance(Context context) {
		if (getCartNumber == null)
			getCartNumber = new GetCartNumber(context);
		return getCartNumber;
	}

	public GetCartNumber(Context context) {
		this.context = context;
	}

	public int get() {
		DBManager sqlite = new DBManager();
		sqlite.open(context);
		try {
			Cursor landCartcursors = sqlite.findAll("LandCart", null);
			Cursor seedCartcursors = sqlite.findAll("SeedCart", null);
			Cursor toolsCartcursors = sqlite.findAll("ToolsCart", null);
			int landNumber = landCartcursors.getCount();
			int seedNumber = seedCartcursors.getCount();
			int toolsNumber = toolsCartcursors.getCount();
			landCartcursors.close();
			seedCartcursors.close();
			toolsCartcursors.close();
			sqlite.close();
			sqlite = null;
			return landNumber + seedNumber + toolsNumber;
		} catch (Exception e) {
			e.printStackTrace();
			sqlite.close();
			sqlite = null;
			return 0;
		}
	}

}
