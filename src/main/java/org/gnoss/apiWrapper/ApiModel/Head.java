package org.gnoss.apiWrapper.ApiModel;

import java.util.ArrayList;

public class Head {

	private ArrayList<Object> link;
	private ArrayList<String> vars;
	
	public ArrayList<Object> getLink() {
		return link;
	}
	public void setLink(ArrayList<Object> link) {
		this.link = link;
	}
	public ArrayList<String> getVars() {
		return vars;
	}
	public void setVars(ArrayList<String> vars) {
		this.vars = vars;
	}
}
