package controller;

import controller.exceptions.SolutionInvalidException;
import controller.logic.RandomPairs;
import controller.logic.SudokuValidator;
import controller.model.DifficultyEnum;
import controller.model.Game;
import controller.system.GameStorage;
import java.util.List;

public class GameDriver {
    SudokuValidator validator;
    RandomPairs randomPairs;

    public GameDriver() {
        validator = new SudokuValidator();
        randomPairs = new RandomPairs();
    }

    public void generateFromSolved(int[][] solvedBoard) throws SolutionInvalidException {
        verifySolution(solvedBoard);
        
        Game easyGame = createGame(solvedBoard, DifficultyEnum.EASY);
        Game mediumGame = createGame(solvedBoard, DifficultyEnum.MEDIUM);
        Game hardGame = createGame(solvedBoard, DifficultyEnum.HARD);
        GameStorage storage = new GameStorage();

        storage.saveGame("easy", easyGame);
        storage.saveGame("medium", mediumGame);
        storage.saveGame("hard", hardGame);
    }

    public void verifySolution(int[][] board) throws SolutionInvalidException {
        validator.validateSourceSolution(board);
    }

    private Game createGame(int[][] solvedBoard, DifficultyEnum difficulty) {
        int[][] boardCopy = copyBoard(solvedBoard);
        int cellsToRemove = difficulty.getRemovedCells();

        List<int[]> positionsToRemove = randomPairs.generateDistinctPairs(cellsToRemove);
        
        for (int[] pos : positionsToRemove) {
            int row = pos[0];
            int col = pos[1];
            boardCopy[row][col] = 0;
        }

        return new Game(boardCopy);
    }

    private int[][] copyBoard(int[][] original) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        }
        return copy;
    }
}