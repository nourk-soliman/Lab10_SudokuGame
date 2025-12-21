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
import java.util.ArrayList;
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
        switch (level){
            case 'e':
              return controller.getGame(DifficultyEnum.EASY).getBoard(); 
            case 'm':
            controller.getGame(DifficultyEnum.MEDIUM).getBoard();     
            case'h':
                controller.getGame(DifficultyEnum.HARD).getBoard();
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
        if(game==null) return null;
         Game newGame=new Game(game);
         boolean cellState[][]=new boolean[9][9];
         String boardState=controller.verifyGame(newGame);
        
          for(int i=0;i<9;i++)//initialise boolean array with all true.
         {for(int j=0;j<9;j++)
             cellState[i][j]=true;}
          
         if(boardState.equalsIgnoreCase("Valid".trim())|| boardState.equalsIgnoreCase("Incomplete".trim())){
             //return as all true, even if incomplete and valid.
         return cellState;}
         
         if(boardState.equalsIgnoreCase("Invalid".trim())){
             String [] invalidCells=boardState.split(" ");//to split each location.
             
             for(int i=1;i<invalidCells.length;i++)//we started from 1 to ignore the "invalid" word
             {String[]invalidCellPosition=invalidCells[i].split(",");//to split the row and coloumn
             int row=Integer.parseInt(invalidCellPosition[0]);
             int colomn=Integer.parseInt(invalidCellPosition[1]);
             cellState[row][colomn]=false;} 
             
             return cellState;
         }
         return null;
        
    }
@Override
public int[][] solveGame(int[][] game) throws InvalidGame {

    if (game == null) {
        throw new InvalidGame("The game board is null");
    }

    // Find empty cells before solving,if they are are not equal to 5, then the game will not be solved.
    ArrayList<int[]> emptyCells = new ArrayList<>();//ArrayList of an array of integers because we need to store the row and colomn of the empty cell.

    for (int row = 0; row < 9; row++) {
        for (int col = 0; col < 9; col++) {
            if (game[row][col] == 0) {
                emptyCells.add(new int[]{row, col});
            }
        }
    }

    if (emptyCells.size() != 5) {
        throw new InvalidGame("Solve allowed only when exactly 5 cells are empty");
    }

    // adapter pattern here.
    Game controllerGame = new Game(game);
    int[] solvedValues = controller.solveGame(controllerGame);

    int[][] solvedCells = new int[solvedValues.length][3];

    for (int i = 0; i < solvedValues.length; i++) {
        solvedCells[i][0] = emptyCells.get(i)[0]; // row
        solvedCells[i][1] = emptyCells.get(i)[1]; // colomn
        solvedCells[i][2] = solvedValues[i];      // value
    }

    return solvedCells;
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
