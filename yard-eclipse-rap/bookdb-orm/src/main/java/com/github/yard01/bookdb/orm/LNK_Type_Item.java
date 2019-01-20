package com.github.yard01.bookdb.orm;

public class LNK_Type_Item {
	Integer ID;
	HUB_Type hub_Type;
	HUB_Item hub_Item;
	public HUB_Type getHub_Type() {
		return hub_Type;
	}
	public void setHub_Type(HUB_Type hub_Type) {
		this.hub_Type = hub_Type;
	}
	public HUB_Item getHub_Item() {
		return hub_Item;
	}
	public void setHub_Item(HUB_Item hub_Item) {
		this.hub_Item = hub_Item;
	}
}
