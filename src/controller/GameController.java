package controller;

import controller.exceptions.InvalidGame;
import controller.exceptions.NotFoundException;
import controller.exceptions.SolutionInvalidException;
import controller.logic.CellFlyweight;
import controller.logic.Solver;
import controller.logic.SudokuValidator;
import java.io.File;
import controller.system.GameCatalog;
import java.io.IOException;
import controller.model.DifficultyEnum;
import controller.model.Game;
import controller.system.GameStorage;
import controller.system.LogFileHandler;

public class GameController implements Viewable {

    private final GameDriver driver;
    private final GameStorage storage;

    public GameController(controller.GameDriver driver, controller.system.GameStorage storage) {
        this.driver = driver;
        this.storage = storage;
    }

    @Override
    public GameCatalog getCatalog() {
        return GameCatalog.checkGames();
    }

    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        Game game = storage.readGame(level);
        if (game == null) {
            throw new NotFoundException("No game found for level: " + level);
        }
        return game;
    }

    @Override
    public void driveGames(Game sourceGame) throws SolutionInvalidException {
        if (sourceGame == null) {
            throw new SolutionInvalidException("Source game is null");
        }
        driver.generateFromSolved(sourceGame.getBoard());
    }

    @Override
    public String verifyGame(Game game) {
        if (game == null || game.getBoard() == null) {
            return null;
        }

        int[][] board = game.getBoard();
        SudokuValidator validator = new SudokuValidator();
        
        boolean hasZeros = false;
        for (int i = 0; i < 9 && !hasZeros; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    hasZeros = true;
                    break;
                }
            }
        }

        StringBuilder invalidCells = new StringBuilder();
        boolean hasInvalid = false;

        for (int row = 0; row < 9; row++) {
            if (hasDuplicates(board[row], row, -1)) {
                hasInvalid = true;
            }
        }

        for (int col = 0; col < 9; col++) {
            int[] column = new int[9];
            for (int row = 0; row < 9; row++) {
                column[row] = board[row][col];
            }
            if (hasDuplicates(column, -1, col)) {
                hasInvalid = true;
            }
        }

        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                int[] box = new int[9];
                int index = 0;
                for (int r = boxRow * 3; r < boxRow * 3 + 3; r++) {
                    for (int c = boxCol * 3; c < boxCol * 3 + 3; c++) {
                        box[index++] = board[r][c];
                    }
                }
                if (hasDuplicatesInBox(box, boxRow, boxCol, board)) {
                    hasInvalid = true;
                }
            }
        }

        boolean[] seen = new boolean[10];
        for (int row = 0; row < 9; row++) {
            java.util.Arrays.fill(seen, false);
            for (int col = 0; col < 9; col++) {
                int val = board[row][col];
                if (val != 0) {
                    if (seen[val]) {
                        invalidCells.append(" ").append(row).append(",").append(col);
                    }
                    seen[val] = true;
                }
            }
        }

        for (int col = 0; col < 9; col++) {
            java.util.Arrays.fill(seen, false);
            for (int row = 0; row < 9; row++) {
                int val = board[row][col];
                if (val != 0) {
                    if (seen[val]) {
                        invalidCells.append(" ").append(row).append(",").append(col);
                    }
                    seen[val] = true;
                }
            }
        }

        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                java.util.Arrays.fill(seen, false);
                for (int r = boxRow * 3; r < boxRow * 3 + 3; r++) {
                    for (int c = boxCol * 3; c < boxCol * 3 + 3; c++) {
                        int val = board[r][c];
                        if (val != 0) {
                            if (seen[val]) {
                                invalidCells.append(" ").append(r).append(",").append(c);
                            }
                            seen[val] = true;
                        }
                    }
                }
            }
        }

        if (invalidCells.length() > 0) {
            return "Invalid" + invalidCells.toString();
        }

        if (hasZeros) {
            return "Incomplete";
        }

        return "Valid";
    }

    private boolean hasDuplicates(int[] array, int row, int col) {
        boolean[] seen = new boolean[10];
        for (int val : array) {
            if (val != 0) {
                if (seen[val]) {
                    return true;
                }
                seen[val] = true;
            }
        }
        return false;
    }

    private boolean hasDuplicatesInBox(int[] box, int boxRow, int boxCol, int[][] board) {
        boolean[] seen = new boolean[10];
        for (int val : box) {
            if (val != 0) {
                if (seen[val]) {
                    return true;
                }
                seen[val] = true;
            }
        }
        return false;
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
        LogFileHandler handler = new LogFileHandler();
        handler.writeToFile(userAction);
    }

    @Override
    public Game undoLastMove(int[][] board) throws IOException {
        LogFileHandler handler = new LogFileHandler();
        String lastMove = handler.readAndRemoveLastMove();
        
        if (lastMove == null || lastMove.trim().isEmpty()) {
            return null;
        }
        
        lastMove = lastMove.replaceAll("[()]", "");
        String[] parts = lastMove.split(",");
        
        if (parts.length < 4) {
            return null;
        }
        
        try {
            int row = Integer.parseInt(parts[0].trim());
            int col = Integer.parseInt(parts[1].trim());
            int previous = Integer.parseInt(parts[3].trim());
            board[row][col] = previous;
            return new Game(board);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}