/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package assignment2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 *
 * @author Cindy
 */
public class terrainGUI extends JPanel implements ActionListener, Observer
{
    String[] dbTerrains = { "tinyA", "tinyB", "small", "medium", "large", "illustrated" };
    int size = 7;
    JButton[][] b;
    public int[][] opPath = new int[size][size];
    ArrayList<Point> path = new ArrayList<Point>();
    Model model = new Model(size, size);
    String chosenDb;

    JPanel northArea = new JPanel(new GridLayout(2, 2));
    JPanel northFin = new JPanel(new GridLayout(1,1));
    JPanel southArea = new JPanel(new GridLayout(2,2));
    JButton chooseDb = new JButton("Select terrain from database"); 
    JComboBox db = new JComboBox(dbTerrains);
    JButton newTerrain = new JButton("Or create your own");
    JLabel blank = new JLabel("              ");
    JLabel fin = new JLabel("FINISH!", SwingConstants.CENTER);   
    JLabel start = new JLabel("START HERE", SwingConstants.CENTER);   
    JLabel score = new JLabel ("Difficulty of this path: ");
    JPanel terrain = new JPanel(new GridLayout(size, size));
    
    JButton greedy = new JButton("Find Greedy Optimal Path");
    JButton optimal = new JButton("Find Optimal Path");
    JPanel eastArea = new JPanel(new GridLayout(15, 1));

    public terrainGUI(){
        super();
        super.setPreferredSize(new Dimension(800,800)); 
        this.setLayout(new BorderLayout(size, size));
        

        northArea.add(chooseDb);
        northArea.add(newTerrain);
        
        northArea.add(northFin);
        northFin.add(fin);
        southArea.add(start);
        southArea.add(score);
        
        eastArea.add(greedy);
        eastArea.add(optimal);
        
        optimal.addActionListener(this);
        newTerrain.addActionListener(this);
        chooseDb.addActionListener(this);
        greedy.addActionListener(this);
        
        this.add(northArea, BorderLayout.NORTH);
        this.add(terrain, BorderLayout.CENTER);
        this.add(southArea, BorderLayout.SOUTH);
        this.add(eastArea, BorderLayout.EAST);
        
        
        drawButtons();
        nextMoves();  
    }
    
    
    //actions
   
    @Override
    public void actionPerformed(ActionEvent e) 
    {
        Object button = e.getSource();
        
        if(button == newTerrain)
        {
            // reset model
            model = new Model(7, 7);
            this.remove(terrain);
            terrain = new JPanel(new GridLayout(size, size));
            this.add(terrain, BorderLayout.CENTER);
            System.out.println("buton2 pres");
            model.currentX = -1;
            model.currentY = -1;
            model.score = 0;
            path.removeAll(path);
            this.score.setText("Difficulty of this path: " + model.score);
            model.firstMove = true;
            terrain.removeAll();
            drawButtons();
            this.revalidate();
            this.repaint();
            nextMoves();
        }
        
        if (button == chooseDb){
            
            try {
                System.out.println("buton pres");
                chosenDb = (String) JOptionPane.showInputDialog(null, " ", "Choose a DB", JOptionPane.QUESTION_MESSAGE, null, dbTerrains, dbTerrains[0]);
                
                System.out.println("button select" + chosenDb);
                
                model = new Model(chosenDb);
                this.remove(terrain);
                terrain = new JPanel(new GridLayout(model.ySize, model.xSize));
                this.add(terrain, BorderLayout.CENTER);
                
                model.currentX = -1;
                model.currentY = -1;
                model.score = 0;
                this.score.setText("Difficulty of this path: " + model.score);
                model.firstMove = true;
                path.removeAll(path);
                drawButtons();
                nextMoves();
                this.revalidate();
                this.repaint();
                terrain.revalidate();;
                terrain.repaint();
                
            } catch (SQLException ex) {
                Logger.getLogger(terrainGUI.class.getName()).log(Level.SEVERE, null, ex);
            }


        }
        
        if (button == greedy){ 
            System.out.println("greedy pres");
        }
        
        if (button == optimal){
            System.out.println("op pres");
            
            findOpPath();
            for(int i = 0; i < model.ySize; i ++)
            {
                for(int j = 0; j < model.xSize; j ++)
                { 
                    System.out.print(opPath[i][j] + " | ");
                }
                
                System.out.println("");
            }
            findLowest();

            
        }
        
        else 
        {
             for(int i = 0; i < model.ySize; i ++)
            {
                for(int j = 0; j < model.xSize; j ++)
                {
                    // valid move - the button was enabled and was clicked
                    if(button == b[i][j])
                    {
                        System.out.println(b[i][j].getText());
                        model.currentX = j;
                        model.currentY = i;
                        model.score += Integer.valueOf(b[i][j].getText());
                        System.out.println("x: " + model.currentX);
                        System.out.println(" y: " + model.currentY);
                        Point point = new Point(model.currentX, model.currentY);
                        path.add(point);
                        this.score.setText("Difficulty of this path: " + model.score);
                        nextMoves();
                        path();

                    }
                }
            }
        }

    }

    public void nextMoves()
    {
        int x = model.currentX;
        int y = model.currentY;
                     
         for(int i = 0; i < model.ySize; i ++)
         {
            for(int j = 0; j < model.xSize; j ++)
            {
               b[i][j].setEnabled(false);
                
                 if(!model.firstMove)
                 { 
                     if(y - 1 >= 0)
                     {
                         if(x - 1 >= 0)
                         {
                            b[y - 1][x - 1].setEnabled(true);
                            b[y - 1][x - 1].setForeground(Color.GREEN); 
                         }
                         
                         if(x + 1 < model.xSize)
                         {
                            b[y - 1][x + 1].setEnabled(true);
                            b[y - 1][x + 1].setForeground(Color.GREEN); 
                         }
                         
                         b[y - 1][x].setEnabled(true);
                         b[y - 1][x].setForeground(Color.GREEN); 
                     }
                 }
            }
        }
         
        if (model.firstMove)
        { 
            for(int i = 0; i < model.xSize ; i++)
            {
                b[model.ySize -1][i].setEnabled(true);
                b[model.ySize -1][i].setForeground(Color.GREEN);
                
            }
            
            model.firstMove = false;
            
        }
        
        if(x != -1 && y != -1)
        {
            b[model.currentY][model.currentX].setForeground(Color.ORANGE);
         
        }
   }    
    
    private void path()
    {
        for(Point p:path)
        {
            int x = p.x;
            int y = p.y;
            System.out.println("path x: " + x);
            System.out.println("path y: " + y);
            
            b[y][x].setEnabled(true);
            b[y][x].removeActionListener(this);
            
            
            b[y][x].setForeground(Color.ORANGE);


        }
    }
    
    private void drawButtons() 
    {
        
        b = new JButton[model.ySize][model.xSize];
        
        for(int i = 0; i < model.ySize; i++)
        {
            for(int j = 0; j < model.xSize; j++)
            {
                int n = model.matrix[i][j];
                b[i][j] = new JButton(String.valueOf(n));
                b[i][j].addActionListener(this);
                b[i][j].setEnabled(false);
                
               // UIManager.getDefaults().put("Button.disabledText", Color.WHITE);
                

                if(n < 0){
                    b[i][j].setBackground(new Color (215, 192, 242)); 
                }
                else if (n >= 0 && n < 5) {
                    b[i][j].setBackground(new Color (188, 149, 233));
                }
                else if ( n >= 5 && n < 10){
                    b[i][j].setBackground(new Color (162, 107, 225));
                }
                else { 
                    b[i][j].setBackground(new Color (135, 65, 216));
                }

                b[i][j].setBorderPainted(false);
                terrain.add(b[i][j]);
            }
        }
    }
    
    
    public void findOpPath(){
        
        for (int i = 0; i < model.xSize; i++){
            
            opPath[model.xSize-1][i] = Integer.parseInt(b[model.xSize-1][i].getText());
        }
        
        
        
        for (int i = model.ySize-2; i >= 0; i--){
            
            for (int j = 0; j < model.xSize; j++){
                
                int minPrev = opPath [i + 1][j]; //one row down, same column
                
                if (j != 0){
                    
                    if (minPrev > opPath[i+1][j-1]){
                        
                        minPrev = opPath[i+1][j-1];
                    }
                }
                
                if( j != model.ySize -1){
                    
                    if (minPrev > opPath[i+1][j+1]){
                        
                        minPrev = opPath[i+1][j+1];
                    }
                }
                opPath[i][j] = Integer.parseInt(b[i][j].getText()) + minPrev; //dynamic programming!!!

            }
        }  
        
    }
    
    public void findLowest(){
        int lowest = opPath[0][0];
        int lowestIndex = 0;

          for (int i = 1; i < model.xSize; i++) {
              if (lowest > opPath[0][i]) {
                  lowest = opPath[0][i];
                  lowestIndex = i;
              }
          }
          
        System.out.println(lowestIndex);
        displayOpPath(lowestIndex); 
    }
    
    public void displayOpPath(int lowest){
        
        b[0][lowest].setBackground(Color.YELLOW); 
        int currentRow = 0;
        int currentCol = lowest;
        int shortest = 0;
        
        for (int i = 0; i < model.xSize ; i++){
            b[currentRow][currentCol].setBackground(Color.YELLOW);
           
            shortest = opPath[currentRow + 1][currentCol];
            
            if (currentRow != model.ySize ){
                currentRow++;
            }
            
            if (currentCol != 0 && currentRow != model.ySize-1){
                
                 if (shortest > opPath[currentRow + 1][currentCol - 1]) {
                     
                    shortest = opPath[currentRow + 1][currentCol - 1];
                    currentCol--;
                }
            }
            
            if (currentCol != model.xSize-1 && currentRow != model.ySize-1) {
                
                if (shortest > this.opPath[currentRow + 1][currentCol + 1]) {
                    
                    shortest = this.opPath[currentRow + 1][currentCol + 1];
                    currentCol++;
                    
                }
            }
            
      }
        
        b[currentRow][currentCol].setBackground(Color.YELLOW);
        System.out.println(this.opPath[model.ySize][0]);
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Updated!");
        
    }
    
    
}