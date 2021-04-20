/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gnoss.apiWrapper.ApiModel;

/**
 *
 * @author salopez
 */
public class SemanticAttachedResource {
    private String file_rdf_property;
    private short file_property_type;
    private byte[] rdf_attacherd_file;
    private boolean delete_file;

    public String getFile_rdf_property() {
        return file_rdf_property;
    }

    public void setFile_rdf_property(String file_rdf_property) {
        this.file_rdf_property = file_rdf_property;
    }

    public short getFile_property_type() {
        return file_property_type;
    }

    public void setFile_property_type(short file_property_type) {
        this.file_property_type = file_property_type;
    }

    public byte[] getRdf_attacherd_file() {
        return rdf_attacherd_file;
    }

    public void setRdf_attacherd_file(byte[] rdf_attacherd_file) {
        this.rdf_attacherd_file = rdf_attacherd_file;
    }

    public boolean isDelete_file() {
        return delete_file;
    }

    public void setDelete_file(boolean delete_file) {
        this.delete_file = delete_file;
    }
}
