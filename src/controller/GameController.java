/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import controller.exceptions.InvalidGame;
import controller.exceptions.NotFoundException;
import controller.exceptions.SolutionInvalidException;
import java.io.File;
import controller.system.GameCatalog;
import java.io.IOException;
import controller.model.DifficultyEnum;
import controller.model.Game;

/**
 *
 * @author Nour
 */
public  class GameController implements Viewable {
    
    @Override
    public GameCatalog getCatalog(){
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
       GameCatalog catalog=new GameCatalog(current,allModesExist);
       return catalog;
       }
  
    


    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void driveGames(Game sourceGame) throws SolutionInvalidException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String verifyGame(Game game) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int[] solveGame(Game game) throws InvalidGame {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void logUserAction(String userAction) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
        
    //testing the catalog method
        public static void main(String[] args) {
        GameController controller=new GameController();
        GameCatalog catalog=controller.getCatalog();
        System.out.println("Current: "+catalog.isCurrent()+" allModes: "+catalog.isAllModesExist());
    }
    
}
