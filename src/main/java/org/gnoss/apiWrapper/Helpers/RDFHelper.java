package org.gnoss.apiWrapper.Helpers;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Element;


public class RDFHelper {

	/**
	 * Returns a string with the value of the label localName in xelm
	 * @param xelt RDF elements
	 * @param localName Label to get the value
	 * @param nameSpaceName Optional The namespace of the label, it it is necessary 
	 * @param filterAttribute Optional Xname of the attribute to filter by 
	 * @param filterAttributeValue Optional Value of the filterAttribute
	 * @return The value of the label localName
	 */
	public static String getElementValue(Iterable<Element> xelt, String localName, String nameSpaceName, String filterAttribute, String filterAttributeValue ) {
		List<Element> eltos= new ArrayList<>();
		String value="";
			if(nameSpaceName!=null) {
				for (Element elto: xelt) {
					if(elto.getName().equals(localName) && elto.getNamespace().toString().equals(nameSpaceName)) {
						if(filterAttribute==null) {
							eltos.add(elto);
							value=eltos.get(0).getName();
							
						}else {
							if(elto.getName().equals(localName) && elto.getNamespace().toString().equals(nameSpaceName) &&(elto.getAttributeValue(filterAttribute).equals(filterAttributeValue))){
								eltos.add(elto);
								value=eltos.get(0).getName();
							}
						}						
					}else {
						return null;
					}
					
				}
			}else {
				for(Element elto: xelt) {
					if(elto.getName().equals(localName)!= false) {
						if(filterAttribute== null && filterAttributeValue.isEmpty()) {
							if(elto.getName().equals(localName)) {
								eltos.add(elto);
								value=eltos.get(0).getName();
							}
						}else if(filterAttribute!=null && !filterAttributeValue.isEmpty()){
							if(elto.getName().equals(localName) && elto.getAttributeValue(filterAttribute).equals(filterAttributeValue)) {
								eltos.add(elto);
								value=eltos.get(0).getName();
							}
						}
					}
				}
			}
			
		
		return value;
		
	}
	
	/**
	 * Returns a string with the value of the label localName in xelm
	 * @param elto RDF element
	 * @param localName Label to get a value
	 * @param nameSpaceName Optional the namespace of the label, if it is  necessary
	 * @return String valor
	 */
	
	public static String getAttributeValue( Element elto, String localName, String nameSpaceName) {
		String valor="";
		if(elto.hasAttributes()) {
			try {
				List <Attribute> lista=elto.getAttributes();
				for(Attribute attr : lista) {
					if(attr.getName().equals(localName) && attr.getNamespace().toString().equals(nameSpaceName)) {
						valor= lista.get(0).getName();
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
				valor= "";
				return valor;
			}
		}else {
			return valor;
		}
		return valor;
		
	}
	
}
