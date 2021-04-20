package org.gnoss.apiWrapper.ApiModel;

public enum TiposDocumentacion {
	 	hyperlink(0),
	    video(2),
	    server_file(3),
	    ontology(5),
	    image(6),
	    note(8),
	    newsletter(11),
	    audio(21),
	    secondary_ontology(23);

	    private int fileType;

	    TiposDocumentacion(int fileType) {
	        this.fileType = fileType;
	    }
	    
	    public int getID(){
	    	return fileType;
	    }
}
