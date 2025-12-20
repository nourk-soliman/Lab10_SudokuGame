/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.exceptions.SolutionInvalidException;
import controller.logic.RandomPairs;
import controller.logic.SudokuValidator;
import controller.model.DifficultyEnum;
import controller.model.Game;
import java.util.List;



/**
 *
 * @author Mariam
 */
public class GameDriver {
    SudokuValidator validator;// to check board validity
    RandomPairs randomPairs;// to generate random row and column pairs

    public GameDriver() {
        validator = new SudokuValidator();
        randomPairs = new RandomPairs();
    }

    public void generateFromSolved(int[][] solvedBoard) throws SolutionInvalidException {
        verifySolution(solvedBoard);
       //generate games of different difficulties
        Game easyGame = createGame(solvedBoard, DifficultyEnum.EASY); // Remove 10 cells
        Game mediumGame = createGame(solvedBoard, DifficultyEnum.MEDIUM); // Remove 25 cells
        Game hardGame = createGame(solvedBoard, DifficultyEnum.HARD); // Remove 20 cells

        //save games
        saveGame(easyGame);
        saveGame(mediumGame);
        saveGame(hardGame);
    }

    public void verifySolution(int[][] board) throws SolutionInvalidException {

        validator.validateSourceSolution(board);
    }

    private Game createGame(int[][] solvedBoard, DifficultyEnum difficulty) {
        int[][] boardCopy = copyBoard(solvedBoard);
        int cellsToRemove = difficulty.getRemovedCells(); // 10, 25, or 20

        // Step 3: Get random positions to remove
        List<int[]> positionsToRemove = randomPairs.generateDistinctPairs(cellsToRemove);
        for (int[] pos : positionsToRemove) {
            int cellIndex = pos[0]; // 0-80
            int row = cellIndex / 9; // Convert to row 0-8
            int col = cellIndex % 9; // Convert to column 0-8

            // Safety check (should always be valid)
            if (row >= 0 && row < 9 && col >= 0 && col < 9) {
                boardCopy[row][col] = 0; // Make cell empty
            }
        }

        return new Game(boardCopy, difficulty);
    }

    //the reason for making a board copy because for example: in the beginning we have a solved board
    // when we create an easy game we remove 10 cells from the solved board
    // then when we create a medium game we should remove 25 cells from the original solved board not from the easy game board
    //which will cause 35 cells to be removed from the original solved board
    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        }
        return copy;
    }

}

