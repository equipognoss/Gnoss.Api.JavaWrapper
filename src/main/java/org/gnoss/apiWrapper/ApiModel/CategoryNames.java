package org.gnoss.apiWrapper.ApiModel;

import java.util.List;
import java.util.UUID;

public class CategoryNames {

	private UUID category_id;
	private List<CategoryName> category_names_list;
	public UUID getCategory_id() {
		return category_id;
	}
	public void setCategory_id(UUID category_id) {
		this.category_id = category_id;
	}
	public List<CategoryName> getCategory_names_list() {
		return category_names_list;
	}
	public void setCategory_names_list(List<CategoryName> category_names_list) {
		this.category_names_list = category_names_list;
	}
	
}
