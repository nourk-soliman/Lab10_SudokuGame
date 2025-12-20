/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.system;

import controller.model.DifficultyEnum;
import controller.model.Game;

/**
 *
 * @author Nour
 */
public class GameModeFactory {
    
    public Game getGameMode(DifficultyEnum difficultyChoice){
        GameStorage storage=new GameStorage();
    if(difficultyChoice==null)
        return null;
    if(difficultyChoice==DifficultyEnum.EASY){
        return storage.importBoardFromFile("N:\\Term 5\\Programming 2\\SudokuGame\\easy");
        
    }
    
        if(difficultyChoice==DifficultyEnum.MEDIUM){
       return storage.importBoardFromFile("N:\\Term 5\\Programming 2\\SudokuGame\\medium");
        
    }
            if(difficultyChoice==DifficultyEnum.HARD){
        return storage.importBoardFromFile("N:\\Term 5\\Programming 2\\SudokuGame\\hard");
        
    }
            return null;
    }
    
}
