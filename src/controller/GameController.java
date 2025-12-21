package controller;

import controller.exceptions.InvalidGame;
import controller.exceptions.NotFoundException;
import controller.exceptions.SolutionInvalidException;
import controller.logic.CellFlyweight;
import controller.logic.Solver;
import controller.model.DifficultyEnum;
import controller.model.Game;
import controller.system.GameCatalog;
import controller.system.GameStorage;
import controller.system.LogFileHandler;
import java.io.IOException;

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
        
        // Check if board has any zeros (incomplete)
        boolean hasZeros = false;
        for (int i = 0; i < 9 && !hasZeros; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    hasZeros = true;
                    break;
                }
            }
        }

        // Find all invalid cells
        StringBuilder invalidCells = new StringBuilder();

        // Check rows for duplicates
        for (int row = 0; row < 9; row++) {
            boolean[] seen = new boolean[10];
            for (int col = 0; col < 9; col++) {
                int val = board[row][col];
                if (val != 0) {
                    if (seen[val]) {
                        // Found duplicate - mark this cell and find the first occurrence
                        invalidCells.append(" ").append(row).append(",").append(col);
                        // Also mark the first occurrence
                        for (int c = 0; c < col; c++) {
                            if (board[row][c] == val) {
                                invalidCells.append(" ").append(row).append(",").append(c);
                                break;
                            }
                        }
                    }
                    seen[val] = true;
                }
            }
        }

        // Check columns for duplicates
        for (int col = 0; col < 9; col++) {
            boolean[] seen = new boolean[10];
            for (int row = 0; row < 9; row++) {
                int val = board[row][col];
                if (val != 0) {
                    if (seen[val]) {
                        // Found duplicate - mark this cell
                        String cellStr = " " + row + "," + col;
                        if (!invalidCells.toString().contains(cellStr)) {
                            invalidCells.append(cellStr);
                        }
                        // Also mark the first occurrence
                        for (int r = 0; r < row; r++) {
                            if (board[r][col] == val) {
                                String firstCell = " " + r + "," + col;
                                if (!invalidCells.toString().contains(firstCell)) {
                                    invalidCells.append(firstCell);
                                }
                                break;
                            }
                        }
                    }
                    seen[val] = true;
                }
            }
        }

        // Check 3x3 boxes for duplicates
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                boolean[] seen = new boolean[10];
                for (int r = boxRow * 3; r < boxRow * 3 + 3; r++) {
                    for (int c = boxCol * 3; c < boxCol * 3 + 3; c++) {
                        int val = board[r][c];
                        if (val != 0) {
                            if (seen[val]) {
                                // Found duplicate - mark this cell
                                String cellStr = " " + r + "," + c;
                                if (!invalidCells.toString().contains(cellStr)) {
                                    invalidCells.append(cellStr);
                                }
                                // Find and mark the first occurrence in this box
                                boolean found = false;
                                for (int r2 = boxRow * 3; r2 < boxRow * 3 + 3 && !found; r2++) {
                                    for (int c2 = boxCol * 3; c2 < boxCol * 3 + 3 && !found; c2++) {
                                        if ((r2 < r || (r2 == r && c2 < c)) && board[r2][c2] == val) {
                                            String firstCell = " " + r2 + "," + c2;
                                            if (!invalidCells.toString().contains(firstCell)) {
                                                invalidCells.append(firstCell);
                                            }
                                            found = true;
                                        }
                                    }
                                }
                            }
                            seen[val] = true;
                        }
                    }
                }
            }
        }

        // Return result
        if (invalidCells.length() > 0) {
            return "Invalid" + invalidCells.toString();
        }

        if (hasZeros) {
            return "Incomplete";
        }

        return "Valid";
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