/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author f2419488
 */
public class Conexao {

    private Connection conn;
    private boolean conectado;
    private final String driver;
    private final String banco;
    private final String host;
    private final String url;
    private final String usuario;
    private final String senha;

    //conexão no BD
    public Conexao() {
      String usarBanco;    
    usarBanco = "local";    
    this.conn = null;
    this.conectado = false;
    this.driver = "com.mysql.jdbc.Driver"; //Classe do driver JDBC
    this.banco = "bdjudicial"; ///servidor
        if ("localxx".equals(usarBanco)) {//Para gravar local-deixar apenas "local" na condição
            this.host = "localhost"; //ip do banco de dados
            this.usuario = "root";
            this.senha = "root";
        }else{
            this.host = "172.29.15.144";
            this.usuario = "userjud";  
            this.senha = "userjud1981";
        }
   //this.banco = "cadernogestaoarrecadacao"; ///servidor
   //--this.host = "localhost"; //ip do banco de dados
   //this.host = "172.29.50.88"; //ip do banco de dados
   //this.host = "172.29.15.144"; //ip do banco de dados-novo 28/05/2013   
   this.url = "jdbc:mysql://" + host + ":3306/" + banco; //URL de conexão
   //this.usuario = username;
  //this.senha = password;  
    }        
    //Conexão local//
    /*public Conexao() {
        this.conn = null;
        this.conectado = false;
        this.driver = "com.mysql.jdbc.Driver"; //Classe do driver JDBC
      //this.banco = "arrec"; ///servidor
         this.banco = "cadernogestaoarrecadacao"; ///servidor
        this.host = "localhost"; //ip do banco de dados
     //this.host = "172.29.50.88"; //ip do banco de dados
      this.url = "jdbc:mysql://" + host + ":3306/" + banco; //URL de conexão
     //this.usuario = username;
     //this.senha = password;
     //this.usuario = "CDC";
     //this.senha = "CDC";
        this.usuario = "root";
    this.senha = "";
    }*/

   // public Conexao() {
      //throw new UnsupportedOperationException("Not yet implemented");
  //}

    public boolean autenticar() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, usuario, senha);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            conectado = false;
            return conectado;
        }
        conectado = true;
        return conectado;
    }

    public Connection conectar() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, usuario, senha);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return conn;
    }
}
