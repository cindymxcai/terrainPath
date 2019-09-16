/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2;

import static assignment2.Database.conn;
import static assignment2.Database.statement;
import java.awt.Color;
import java.util.Random;
import assignment2.terrainGUI;
import java.awt.Point;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author Cindy
 */
public class Model  extends Observable {
    
    boolean firstMove = true;
    boolean greedyFirstMove = true; 
    public int[][] matrix;
    ArrayList<Point> greedyNext = new ArrayList();
    public int currentX, currentY, xSize, ySize, opCurrentX, opCurrentY;
    private final Random rand = new Random();
    public Database db;
    public int score, opScore;
    
    
    public Model(int y, int x)
    {
        matrix = new int[y][x];
        xSize = x;
        ySize = y;
        currentX = -1; 
        currentY = -1;
        score = 0;
        
        for(int i = 0; i < y; i ++)
        {
            for(int j = 0; j < x; j ++)
            {
               matrix[i][j] = rand.nextInt(21) -5;
            }
        }
        
        
    }
        
    public Model(String terrains) throws SQLException
    {
        currentX = -1; 
        currentY = -1;
        score = 0;
        this.db = new Database();
        int dbX = 0, dbY = 0;
        
        

        ResultSet rs = db.dbQuery("select * from " + terrains);

        while(rs.next())
        {
            dbX = Math.max(dbX, Integer.valueOf(rs.getString(1)));
            dbY = Math.max(dbY, Integer.valueOf(rs.getString(2)));
            System.out.println(dbY);
            System.out.println(dbY);  
            System.out.println("numbers" + rs.getInt(3));

        }
        dbX += 1; 
        dbY += 1;
        xSize = dbX;
        ySize =dbY;
        matrix = new int[ySize][xSize];
       
        rs.first();
        rs.previous();
        while(rs.next())
        {
            matrix[rs.getInt(2)][rs.getInt(1)] = rs.getInt(3);
        }  
     } 
    
    
        
    public void notifyView()
    {
        setChanged();
        notifyObservers();
    }
    
    
}
    
    
    

