/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

/**
 *
 * @author ANDY ESCOBAR
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Tabla {
  String name;
  String schema;
  ArrayList<Columna> campos;

  public Tabla(String nameT,String SHMA){
    this.name=nameT.toUpperCase();
    this.schema=SHMA;
    this.campos=new ArrayList<>();
  }

    public Tabla(String name, String schema, ArrayList<Columna> campos) {
        this.name = name.toUpperCase();
        this.schema = schema;
        this.campos = campos;
    }

    public Tabla() {
    }
    
    
  
  
  

  public void addColumna(Columna c){
        if(!existeColumna(c)){
          campos.add(c);
        }
  }

  private boolean existeColumna(Columna c){
    for(Columna cp:campos){
      if(cp.nombreCampo.equalsIgnoreCase(c.nombreCampo)){
        JOptionPane.showMessageDialog(null,"Nombre "+c.nombreCampo+" ya existe en la tabla "
          +name);
        return true;
      }
    }
    return false;
  }
  
  
  
  

  public String getColumnas(){
    String scriptColumna='"'+schema+'"'+"."+name+" ( \n";
    for (int i=0;i<campos.size();i++){
      scriptColumna+=campos.get(i);
      if(i!=(campos.size()-1)){
        scriptColumna+=",\n";
      }
    }
    String container=", CONSTRAINT "+name+"_PK PRIMARY KEY ( ";
    boolean existPK=false;
    int lastPK=getCantidadPK();
    int countPK=0;
    
    for (int i = 0; i <campos.size(); i++) {
        if(campos.get(i).primaryKey){
            container+=campos.get(i).getPrimaryKey(name);
            existPK=true;
            countPK++;
            if(countPK!=lastPK){
                container+=", ";
            }
            
            
        }
        
      }
      container+=" )";
      if(existPK){
            scriptColumna+=container;
        }
    scriptColumna+="\n)";
    return scriptColumna;
  }
  
  
    public String getScriptCreateTable(){
      return "CREATE TABLE "+getColumnas();
    }
    
    public String getEliminarCheck(String nameCk,String nombreT,String schem){
        return "ALTER TABLE \""+schem+"\"."+nombreT+" DROP CHECK "+nameCk;
    
    } 
     private int getCantidadPK() {
        int cont=0;
      for (Columna campo : campos) {
        if(campo.isPrimaryKey()){
            cont++;
        }
      }
        return cont;
    }
     
     public String getAlterTableCheck(Database data,String nomcheck,String Schema){
         String script="ALTER TABLE ";
         String nombreTabla="";
         String nombreSchema="";
         ResultSet rs;
      try {
            rs = data.getConsulta("SELECT TBNAME,TBCREATOR FROM SYSIBM.SYSCHECKS WHERE TBCREATOR= '"+Schema+"' AND NAME='"+nomcheck+"'");
           while(rs.next()){
                nombreTabla=rs.getString(1);
                nombreSchema=rs.getString(2);
            }
           script+=nombreTabla+"\n ADD CONSTRAINT "+nomcheck+" CHECK ";
        } catch (SQLException ex) {
          Logger.getLogger(Tabla.class.getName()).log(Level.SEVERE, null, ex);
        }
        return script;
     }
  
    public String getScriptUpdateTable(){
      return "ALTER TABLE "+getColumnas();
    }
    
    public  String getScriptEliminarTabla(){
        return "DROP TABLE "+'"'+schema+'"'+"."+name;
    }
    
    public String getCreateProcedure(String schem,String name,String parametros[][]){
        return "CREATE OR REPLACE PROCEDURE \""+schem+"\"."+name+" ( "+getParametros(parametros)+" )\nBEGIN\n\nEND";
    }
    
    public String getScriptCreateAlterTable(){
        return "CREATE OR REPLACE TABLE "+getColumnas();
    }
    
    public void addPrimaryKeyToTable(String PK){
        for (int i = 0; i < campos.size(); i++) {
            if(campos.get(i).nombreCampo.equals(PK)){
                campos.get(i).setPrimaryKey(true);
            }
        }
    }
    
    public String generateTrigger(String schema,String tabla,String nombre,String event[],String time){
        String sql="CREATE OR REPLACE TRIGGER "+nombre+"\n"+(time.equalsIgnoreCase("BEFORE")?"NO CASCADE BEFORE":time)+
                " "+getTriggerEvent(event)+" ON "+tabla+"\nFOR EACH ROW MODE DB2SQL\nBEGIN ATOMIC\n" +"\nEND " ;
            
        return sql;
    }
    
    public String getTriggerEvent(String even[]){
        String ev="";
        for (int i = 0; i < even.length; i++) {
            if(!even[i].equalsIgnoreCase("")){
                ev+= even[i];
                if(i!=even.length-1){
                    ev+=" OR ";
                }
            }
        }
        return ev;
    }

    public String getName() {
        return name;
    }
    
    public String getScriptADDColumn(){
        return "ALTER TABLE "+'"'+schema+"\"."+name+"\nADD "+campos.get(0);
    }
    
    public String getScriptDROPColumn(){
        return "ALTER TABLE "+'"'+schema+"\"."+name+"\nDROP COLUMN "+campos.get(0).nombreCampo;
    }
  

  public static void main(String[] args) {
    Tabla tabla=new Tabla("EMPLEADOSTEMP2","ANDY ESCOBAR");
    Columna cod=new Columna(true,"Codigo","Varchar","20",true,"");
    Columna nom=new Columna(false,"Nombre","Varchar","20",false,"hola");
    Columna dir=new Columna(false,"Direccion","Varchar","20",false,"");
    Columna ed=new Columna(false,"Edad","Integer","0",false,"");
    tabla.addColumna(cod);
    tabla.addColumna(nom);
    tabla.addColumna(dir);
    tabla.addColumna(ed);
    ArrayList<String>events=new ArrayList<>();
    String event[]={"INSERT","DELETE","UPDATE"};
    String para[][]={{"num","OUT","VARCHAR"},{"num3","IN","VARCHAR"},{"num4","INOUT","VARCHAR"}};
      
    
   // tabla.addPrimaryKeyToTable("NOMBRE");
    System.out.println(tabla.getScriptCreateTable());
      System.out.println(tabla.getScriptEliminarTabla());
      System.out.println(tabla.getTriggerEvent(event));
      
      
      
      System.out.println(tabla.generateTrigger("ANDY ESCOBAR","NERY", "TRG_NERY",event,"AFTER"));
      
      System.out.println(tabla.getCreateProcedure("ANDY ESCOBAR","PROCEXAMPLE", para));
  }

    private String getParametros(String[][] parametros) {
        String script="";
        for (int i = 0; i < parametros.length; i++) {
            script+=parametros[i][1]+" "+parametros[i][0]+" "+parametros[i][2];
            if(i!=parametros.length-1){
                script+=",\n ";
            }
        }
            
        return script;
    }

    public String getColumnaIndexes(String columns) {
        String cadenanueva=columns.replace("+",",");
        if(cadenanueva.charAt(0)==','){
             return cadenanueva.substring(1,cadenanueva.length());
        }
        return cadenanueva;
        
    }

   
}