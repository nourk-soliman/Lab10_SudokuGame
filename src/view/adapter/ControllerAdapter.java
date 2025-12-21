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

public class ControllerAdapter implements Controllable {
    Viewable controller;

    public ControllerAdapter(Viewable controller) {
        this.controller = controller;
    }

    @Override
    public boolean[] getCatalog() {
        GameCatalog catalog = controller.getCatalog();
        if (catalog != null) {
            boolean check[] = new boolean[2];
            check[0] = catalog.isAllModesExist();
            check[1] = catalog.isCurrent();
            return check;
        }
        return null;
    }

    @Override
    public int[][] getGame(char level) throws NotFoundException {
        switch (level) {
            case 'e':
            case 'E':
                return controller.getGame(DifficultyEnum.EASY).getBoard();
            case 'm':
            case 'M':
                return controller.getGame(DifficultyEnum.MEDIUM).getBoard();
            case 'h':
            case 'H':
                return controller.getGame(DifficultyEnum.HARD).getBoard();
            case 'i':
            case 'I':
                // Load incomplete game
                GameStorage storage = new GameStorage();
                Game incompleteGame = storage.readCurrentGame();
                if (incompleteGame != null) {
                    return incompleteGame.getBoard();
                }
                return null;
            default:
                return null;
        }
    }

    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException {
        GameStorage gameStorage = new GameStorage();
        Game game = gameStorage.importBoardFromFile(sourcePath);
        controller.driveGames(game);
    }

    @Override
    public boolean[][] verifyGame(int[][] game) {
        if (game == null) return null;
        Game newGame = new Game(game);
        boolean cellState[][] = new boolean[9][9];
        String boardState = controller.verifyGame(newGame);
        
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++)
                cellState[i][j] = true;
        }
        
        if (boardState.equalsIgnoreCase("Valid".trim()) || boardState.equalsIgnoreCase("Incomplete".trim())) {
            return cellState;
        }
        
        if (boardState.toLowerCase().startsWith("invalid")) {
            String[] parts = boardState.split(" ");
            for (int i = 1; i < parts.length; i++) {
                String[] coords = parts[i].split(",");
                if (coords.length == 2) {
                    try {
                        int row = Integer.parseInt(coords[0]);
                        int col = Integer.parseInt(coords[1]);
                        cellState[row][col] = false;
                    } catch (NumberFormatException e) {
                        // Skip invalid format
                    }
                }
            }
            return cellState;
        }
        return null;
    }

    @Override
    public int[][] solveGame(int[][] game) throws InvalidGame {
        if (game == null) {
            throw new InvalidGame("The game board is null");
        }

        ArrayList<int[]> emptyCells = new ArrayList<>();
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

        Game controllerGame = new Game(game);
        int[] solvedValues = controller.solveGame(controllerGame);

        int[][] solvedCells = new int[solvedValues.length][3];
        for (int i = 0; i < solvedValues.length; i++) {
            solvedCells[i][0] = emptyCells.get(i)[0]; // row
            solvedCells[i][1] = emptyCells.get(i)[1]; // column
            solvedCells[i][2] = solvedValues[i];      // value
        }

        return solvedCells;
    }

    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        String action = "(" + userAction.getRow() + "," + userAction.getColomn() + "," + 
                       userAction.getValue() + "," + userAction.getPrevious() + ")";
        controller.logUserAction(action);
    }

    @Override
    public int[][] undoLastMove(int[][] game) throws IOException {
        Game result = controller.undoLastMove(game);
        if (result != null) {
            return result.getBoard();
        }
        return null;
    }
}