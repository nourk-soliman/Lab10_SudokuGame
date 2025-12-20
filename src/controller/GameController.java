/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import exceptions.InvalidGame;
import exceptions.NotFoundException;
import exceptions.SolutionInvalidException;
import interfaces.*;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Nour
 */
public  class GameController implements Controllable {
    
    @Override
    public boolean[] getCatalog(){
        boolean[] catalog=new boolean[2];
        String basePath="N:\\Term 5\\Programming 2\\SudokuGame\\";
        String[]difficultyPaths={"easy","medium","hard"};
        
        boolean current=false;
        File currentFolder=new File(basePath+"current game");
        
        if(currentFolder.isDirectory() && currentFolder.list().length >0) //checks if the folder exists && that there is at least one folder.
        {
            current=true;
        }
        
       boolean allModesExist=true;
       for(int i=0;i<3;i++)
       {File folder=new File(basePath+difficultyPaths[i]);
       if(!folder.isDirectory() || !(folder.list().length>0))
       {allModesExist=false;
       break;}
       }
       catalog[0]=current;
       catalog[1]=allModesExist;
       return catalog;
       }
   

    @Override
    public int[][] getGame(char level) throws NotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean[][] verifyGame(int[][] game) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int[][] solveGame(int[][] game) throws InvalidGame {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void logUserAction(Object userAction) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    //testing the catalog method
        public static void main(String[] args) {
        GameController controller=new GameController();
        boolean[] catalog=controller.getCatalog();
        System.out.println("Current: "+catalog[0]+" allModes: "+catalog[1]);
    }
    
}
