/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import com.sun.prism.paint.Color;

/**
 *
 * @author ANDY ESCOBAR
 */
public class Columna {
  boolean primaryKey;
  String nombreCampo;
  String dataType;
  String PK;
  String size;
  boolean notNull;
  String comment;
  String metaData;


  public Columna(boolean pK, String nC, String dT, String size, boolean nN, String comment) {
    this.primaryKey = pK;
    this.nombreCampo = nC.toUpperCase();
    this.dataType = dT.toUpperCase();
    this.size = size;
    this.notNull = nN||pK;
    this.comment = comment;
//    this.metaData=nombreCampo +" "+dataType+((!size.equals("0"))||size.length()==0?"("+size+")":"")+(notNull?" NOT NULL":"")+(comment.length()>0?" DEFAULT "+comment:"");
  }

 
  
  public String getPrimaryKey(String table){
    if(primaryKey){
        return nombreCampo;
    }else{
        return null;
    }
  }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
        this.notNull=primaryKey;
    }
  

  @Override
  public String toString() {
    return  getMetaData();
  }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public String getNombreCampo() {
        return nombreCampo;
    }

    public String getDataType() {
        return dataType;
    }

    public String getSize() {
        return size;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public String getComment() {
        return comment;
    }   
    
    public String getMetaData(){
        return nombreCampo +" "+dataType+(size.equals(" ")||size.equals("0")||size.length()==0?"":"("+size+")")+(notNull?" NOT NULL":"")+(comment.length()>0?" DEFAULT "+comment:"");
    }
  

}