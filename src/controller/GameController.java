/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;
import controller.exceptions.InvalidGame;
import controller.exceptions.NotFoundException;
import controller.exceptions.SolutionInvalidException;
import controller.logic.CellFlyweight;
import controller.logic.Solver;
import java.io.File;
import controller.system.GameCatalog;
import java.io.IOException;
import controller.model.DifficultyEnum;
import controller.model.Game;
import controller.system.GameStorage;
import controller.system.LogFileHandler;

/**
 *
 * @author Nour
 */
public  class GameController implements Viewable {

    
    private final GameDriver driver ;
    private final GameStorage storage;
    public GameController(controller.GameDriver driver, controller.system.GameStorage storage) {
        this.driver = driver;
        this.storage = storage;
    }  

   
    
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
    Game game = storage.readGame(level);
        if (game == null) {
            throw new NotFoundException("No game found for level: " + level);
        }
        return game; }

    @Override
    public void driveGames(Game sourceGame) throws SolutionInvalidException {
   if (sourceGame == null) {
            throw new SolutionInvalidException("Source game is null");
        }
        driver.generateFromSolved(sourceGame.getBoard()); 
    }
//-------------------------------------------look  ------------------------------------------------
    @Override
    public String verifyGame(Game game) {
   try {
            driver.verifySolution(game.getBoard());
            // check if there are zeros -> incomplete
            boolean incomplete = false;
            for (int i = 0; i < 9 && !incomplete; i++) {
                for (int j = 0; j < 9; j++) {
                    if (game.getBoard()[i][j] == 0) {
                        incomplete = true;
                        break;
                    }
                }
            }
            return incomplete ? "incomplete" : "valid";
        } catch (SolutionInvalidException e) {
            return e.getMessage(); 
        }
    }

    @Override
    public int[] solveGame(Game game) throws InvalidGame {
   if (game == null || game.getBoard() == null) {
        throw new InvalidGame("The game or board is null");
    }

    int[][] board = game.getBoard();

    
    CellFlyweight[] emptyCells = new Game(board).findEmptyCells(board);

    
    boolean solved = Solver.solve(board);

    if (!solved) {
        throw new InvalidGame("No valid solution found for the game");
    }

    
    int[] solvedValues = new int[emptyCells.length];
    for (int i = 0; i < emptyCells.length; i++) {
        solvedValues[i] = board[emptyCells[i].getRow()][emptyCells[i].getCol()];
    }

    return solvedValues;
    }

    @Override
    public void logUserAction(String userAction) throws IOException {
        LogFileHandler handler=new LogFileHandler();
        handler.writeToFile(userAction);
    }
        


    @Override
    public Game undoLastMove(int[][]board) throws IOException {
       LogFileHandler handler=new LogFileHandler();
       String lastMove= handler.readAndRemoveLastMove();
         lastMove = lastMove.replaceAll("[()]", "");
        String[] parts = lastMove.split(",");
        if(parts==null){
            return null;
        }
      int row= Integer.parseInt(parts[0].trim());
      int colomn=  Integer.parseInt(parts[1].trim());
      int previous=  Integer.parseInt(parts[3].trim());
      board[row][colomn]=previous;
      return new Game(board);
      
      
    }
        //testing the catalog method
     
}


