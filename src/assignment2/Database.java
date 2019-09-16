/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cindy
 */
public class Database {
    
    private static final String DRIVER="com.mysql.jdbc.Driver";
    public static Connection conn;
    public static Statement statement;
    /**
     *
     */
    public Database () throws SQLException {
        
        System.out.println("attempting to connect");
        try 
        {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection("jdbc:mysql://raptor2.aut.ac.nz:3306/terrains", "student", "fpn871");
            statement = conn.createStatement();
            System.out.println("Connection Made! ");
        }
        catch (Exception e) 
        {
                System.out.println("Connection Error :( " + e);
        }
        
    }
    
    public ResultSet dbQuery(String q)
    {
            try 
            {
                System.out.println(statement);
                return statement.executeQuery(q);
    
               
            } 
            catch (SQLException ex) 
            {
                System.out.println("Canceled");
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error!");
            }

        return null;
    }
    
}
    

  

  