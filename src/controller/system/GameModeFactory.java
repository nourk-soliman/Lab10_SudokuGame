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

    public Game getGameMode(DifficultyEnum difficultyChoice) {
        GameStorage storage = new GameStorage();

        if (difficultyChoice == null) return null;

        switch (difficultyChoice) {
            case EASY:
                return storage.importBoardFromFile(
                    "N:\\Term 5\\Programming 2\\SudokuGame\\easy\\easy.csv"
                );
            case MEDIUM:
                return storage.importBoardFromFile(
                    "N:\\Term 5\\Programming 2\\SudokuGame\\medium\\medium.csv"
                );
            case HARD:
                return storage.importBoardFromFile(
                    "N:\\Term 5\\Programming 2\\SudokuGame\\hard\\hard.csv"
                );
            default:
                return null;
        }
    }
}
