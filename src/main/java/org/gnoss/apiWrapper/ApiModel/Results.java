package org.gnoss.apiWrapper.ApiModel;

import java.util.ArrayList;
import java.util.HashMap;

import org.gnoss.apiWrapper.ApiModel.SparqlObject;

public class Results {

	
		private boolean distinct;
		private boolean ordered;
		private ArrayList<HashMap<String, Data>> bindings;
		
		public boolean isDistinct() {
			return distinct;
		}
		public void setDistinct(boolean distinct) {
			this.distinct = distinct;
		}
		public boolean isOrdered() {
			return ordered;
		}
		public void setOrdered(boolean ordered) {
			this.ordered = ordered;
		}
		public ArrayList<HashMap<String, Data>> getBindings() {
			return bindings;
		}
		public void setBindings(ArrayList<HashMap<String, Data>> bindings) {
			this.bindings = bindings;
		}
	
}
