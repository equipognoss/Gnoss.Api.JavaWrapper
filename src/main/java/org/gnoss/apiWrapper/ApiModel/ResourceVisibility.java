package org.gnoss.apiWrapper.ApiModel;

public enum ResourceVisibility {
	open(0),
    editors(1),
    communitymembers(2),
    specific(3);

    private int fileType;

    ResourceVisibility(int fileType) {
        this.fileType = fileType;
    }
    
    public int getID(){
    	return fileType;
    }
    
}
