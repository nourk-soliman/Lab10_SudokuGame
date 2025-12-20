/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.adapter;


import controller.Viewable;
import controller.exceptions.InvalidGame;
import controller.exceptions.NotFoundException;
import controller.exceptions.SolutionInvalidException;
import controller.model.DifficultyEnum;
import controller.model.Game;
import controller.system.GameCatalog;
import controller.system.GameStorage;
import java.io.IOException;
import view.model.UserAction;

/**
 *
 * @author Nour
 */
public class ControllerAdapter implements Controllable {
    Viewable controller;

    public ControllerAdapter(Viewable controller) {
        this.controller = controller;
    }
    
    

    @Override
    public boolean[] getCatalog() {
        GameCatalog catalog=controller.getCatalog();
        if(catalog!=null)
        {boolean check[]=new boolean[2];
        check[0]=catalog.isAllModesExist();
        check[1]=catalog.isCurrent();
        return check;}
        return null;
        
        
    }

    @Override
    public int[][] getGame(char level) throws NotFoundException {
        DifficultyEnum difficultyChoice = null;
        switch (level){
            case 'e':
              return controller.getGame(difficultyChoice.EASY).getBoard(); 
            case 'm':
            controller.getGame(difficultyChoice.MEDIUM).getBoard();     
            case'h':
                controller.getGame(difficultyChoice.HARD).getBoard();
            default:
                return null;
        }
     
        
        
        
    }

    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException {
        GameStorage gameStorage=new GameStorage();
       Game game=gameStorage.importBoardFromFile(sourcePath); 
       controller.driveGames(game);
        
    }

    @Override
    public boolean[][] verifyGame(int[][] game) {
         /* GameStorage gameStorage=new GameStorage();
         Game newGame=new Game(game);
         controller.driveGames(newGame);*/
    }

    @Override
    public int[][] solveGame(int[][] game) throws InvalidGame {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void logUserAction(UserAction userAction) throws IOException {
    String action="("+userAction.getRow()+","+userAction.getColomn()+","+userAction.getValue()+","+userAction.getPrevious()+")";
    controller.logUserAction(action);
    }

    @Override
    public int[][] undoLastMove(int [][] game) throws IOException {
     return controller.undoLastMove(game).getBoard();
    }
    
}
