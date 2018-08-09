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
import java.util.ArrayList;
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
    
     private int getCantidadPK() {
        int cont=0;
      for (Columna campo : campos) {
        if(campo.isPrimaryKey()){
            cont++;
        }
      }
        return cont;
    }
  
    public String getScriptUpdateTable(){
      return "ALTER TABLE "+getColumnas();
    }
    
    public  String getScriptEliminarTabla(){
        return "DROP TABLE "+'"'+schema+'"'+"."+name;
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
    
   // tabla.addPrimaryKeyToTable("NOMBRE");
    System.out.println(tabla.getScriptCreateTable());
      System.out.println(tabla.getScriptEliminarTabla());
      System.out.println(tabla.getTriggerEvent(event));
      
      System.out.println(tabla.generateTrigger("ANDY ESCOBAR","NERY", "TRG_NERY",event,"AFTER"));
  }

   
}