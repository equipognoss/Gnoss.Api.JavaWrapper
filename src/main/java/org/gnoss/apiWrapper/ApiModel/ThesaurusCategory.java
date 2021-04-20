/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.ApiModel;

import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author salopez
 */
public class ThesaurusCategory {
    private UUID category_id;
    private String category_name;
    private ArrayList<ThesaurusCategory> Children;
    private UUID parent_category_id;
    
    public ThesaurusCategory(){
        Children = new ArrayList<ThesaurusCategory>();
    }

	public UUID getCategory_id() {
		return category_id;
	}

	public void setCategory_id(UUID category_id) {
		this.category_id = category_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public ArrayList<ThesaurusCategory> getChildren() {
		return Children;
	}

	public void setChildren(ArrayList<ThesaurusCategory> children) {
		Children = children;
	}

	public UUID getParent_category_id() {
		return parent_category_id;
	}

	public void setParent_category_id(UUID parent_category_id) {
		this.parent_category_id = parent_category_id;
	}
    
    
}
