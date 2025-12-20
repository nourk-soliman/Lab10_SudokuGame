/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.system;
import controller.GameController;
import controller.csv.CSVGameHandler;
import controller.model.Game;
import controller.model.DifficultyEnum;
/**
 *
 * @author Nour
 */
public class GameStorage {
    private final String basePath="N:\\Term 5\\Programming 2\\SudokuGame\\";
    
    public Game readCurrentGame(){
        GameController controller=new GameController(null, null);
        GameCatalog catalog=controller.getCatalog();
        
        if(!catalog.isCurrent())
        {return null;}
         return importBoardFromFile(basePath+"current game\\game.csv");          
    }
        
   public Game readGame(DifficultyEnum difficultyChoice){
     GameController controller=new GameController(null, null);
     GameCatalog catalog=controller.getCatalog();
     if(!catalog.isAllModesExist()){
         return null;
     }
     GameModeFactory factory=new GameModeFactory();
     return factory.getGameMode(difficultyChoice);
   }     

   
public Game loadSolvedBoard(String filename){
if(filename==null)
    return null;
 
   return importBoardFromFile(filename);
    
}
   
public void saveGame(String difficulty, Game game) {
    CSVGameHandler.writeCSV(basePath+ difficulty + "\\" + difficulty + ".csv",game.getBoard());
}

public void updateCurrentGame(String difficulty, Game game) {
    CSVGameHandler.updateCSV(basePath+ difficulty + "\\" + difficulty + ".csv",game.getBoard());
}

public void deleteGameFromFolder(String difficulty) {
    CSVGameHandler.deleteCSV(basePath+ difficulty + "\\" + difficulty + ".csv");
}



public void deleteCurrentGame(){
    CSVGameHandler.deleteCSV(basePath+"current game\\game.csv");
}


    public Game importBoardFromFile(String filename) {
        int[][] newBoard = null;
        try {
            newBoard = CSVGameHandler.readCSV(filename);
        } catch (IllegalArgumentException e) {
            System.out.println("Try a different file or edit the file.");
        }
        return new Game(newBoard);

    }
}
