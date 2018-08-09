/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ANDY ESCOBAR
 */
public class Database {
    private String DRIVER;
    private String URL;
    private String USER;
    private String PASSWORD;
    private String Database;
    private Connection conexion;
    
    
    public Database(){
       DRIVER= "com.ibm.db2.jcc.DB2Driver";
       Database="";
       URL="jdbc:db2://localhost:50000/";
       USER="";
       PASSWORD="";
       conexion=null;
    }
    
    public void setData(String database,String user,String password){
        Database=database;
        URL+=database;
        USER=user;
        PASSWORD=password;
        
    }
    
    public void connect() {
        
        try {
            Class.forName(DRIVER);
            conexion=DriverManager.getConnection(URL, USER, PASSWORD);
            conexion.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException ex) {
            JOptionPane.showMessageDialog(null,"Se produjo un error al conectar la base de datos","Base de Datos",JOptionPane.ERROR_MESSAGE);
        }finally{
            if(conexion!=null){
                System.out.println("Connected successfully.");
            }
        }
           
        
        
    }
    public void close(){
        try {
            conexion.commit();
            conexion.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"No puedo cerrar la coneccion","Base de Datos",JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    
    public ResultSet getConsulta(String sql) throws SQLException{
        PreparedStatement pstmt = conexion.prepareStatement(sql); 
        ResultSet rs = pstmt.executeQuery();        
        return rs;
    } 
    
    public void ejecutarOperacion(String sql) throws SQLException{
        try (PreparedStatement pstmt = conexion.prepareStatement(sql)) {
            pstmt.executeUpdate();
            pstmt.close();
            //close();
            //connect();
            
        }   
    }

    

    @Override
    public String toString() {
        return "Database{" + "DRIVER=" + DRIVER + ", URL=" + URL + ", USER=" + USER + ", PASSWORD=" + PASSWORD + ", Database=" + Database + ", conexion=" + conexion + '}';
    }
 
    public static void main(String[] args) {
        //try {
         
            Database dat=new Database();
            dat.setData("Sample","ANDY ESCOBAR","andyescobar007");
            System.out.println(dat.toString());
           // StringTokenizer st = new StringTokenizer("SELECT  * FROM EMPLOYEE");
           Scanner st=new Scanner("SELECT  * FROM EMPLOYEE SELECT");
           while (st.hasNext()) {
                //while (st.hasMoreTokens()) {
                    
                    String tempP=st.next();
                   
                    if(dat.isPalabraReservada(tempP)){
                       
                        System.out.println("\u001B[34m"+tempP);
                    }else{
                    System.out.println('"'+tempP+'"');
                        System.out.println("SELECT * FROM \"ANDY ESCOBAR\".EMPLOYEE");
                }
            }
           String pal="CREATE VIEW SELECT * FROM USERS";
           String newPal;
          
           System.out.println(dat.eliminarPalabras(new String[]{"CREATE","VIEW"},pal));
           
                    
                
      //  }
            
            //dat.connect();
            //ResultSet rs=dat.getConsulta("SELECT TABLE_NAME FROM SYSIBM.TABLES WHERE TABLE_TYPE='BASE TABLE' AND TABLE_SCHEMA='ANDY ESCOBAR' ORDER BY TABLE_NAME");
//            ResultSet rs=dat.getConsulta("SELECT * FROM EMPLOYEE");
//           while (rs.next()) {
//                System.out.println(rs.getString(2));
//                
//            }
//           String USERS=System.getProperty("user.name");
//            System.out.println(USERS);
//            dat.close();
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
        
        
    }
    
     public String eliminarPalabras(String[] palabras, String sentencia) {
        for(String p:palabras){
            if(sentencia.contains(p)){
                sentencia=sentencia.replace(p,"");
            }
        }
        return sentencia;
       
    }
    
    public boolean isPalabraReservada(String pal ){
            String palabrasReservadas[]={"FROM","WHERE","MAX","CREATE","ALTER","SELECT"};
            for(String sg:palabrasReservadas){
                if(sg.equalsIgnoreCase(pal)){
                return true;
             }
                
            }
            return false;
    }
    
    
   
    
}
