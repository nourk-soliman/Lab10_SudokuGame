package controller.system;

import controller.GameController;
import controller.csv.CSVGameHandler;
import controller.model.DifficultyEnum;
import controller.model.Game;
import java.io.File;

public class GameStorage {
    private final String basePath;
    
    public GameStorage() {
        // Use current directory or specify your project directory
        this.basePath = System.getProperty("user.dir") + "/";
        System.out.println("GameStorage base path: " + basePath);
    }
    
    public Game readCurrentGame() {
        GameController controller = new GameController(null, null);
        GameCatalog catalog = controller.getCatalog();
        
        if (catalog != null && catalog.isCurrent()) {
            return importBoardFromFile(basePath + "current game/game.csv");
        }
        return null;
    }
    
    public Game readOriginalGame() {
        String filePath = basePath + "current game/original.csv";
        
        File file = new File(filePath);
        if (file.exists()) {
            return importBoardFromFile(filePath);
        }
        return null;
    }
        
    public Game readGame(DifficultyEnum difficultyChoice) {
        GameController controller = new GameController(null, null);
        GameCatalog catalog = controller.getCatalog();
        
        if (catalog == null || !catalog.isAllModesExist()) {
            System.out.println("Catalog check failed - not all modes exist");
            return null;
        }
        
        GameModeFactory factory = new GameModeFactory();
        return factory.getGameMode(difficultyChoice);
    }     

    public Game loadSolvedBoard(String filename) {
        if (filename == null) return null;
        return importBoardFromFile(filename);
    }
   
    public void saveGame(String difficulty, Game game) {
        String folderPath = basePath + difficulty;
        String filePath = folderPath + "/" + difficulty + ".csv";
        
        // Create the folder if it doesn't exist
        File folder = new File(folderPath);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            System.out.println("Creating folder: " + folderPath + " - " + (created ? "SUCCESS" : "FAILED"));
        }
        
        System.out.println("Saving game to: " + filePath);
        CSVGameHandler.writeCSV(filePath, game.getBoard());
    }

    public void updateCurrentGame(String difficulty, Game game) {
        String folderPath = basePath + difficulty;
        String filePath = folderPath + "/" + difficulty + ".csv";
        
        // Create the folder if it doesn't exist
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        
        CSVGameHandler.updateCSV(filePath, game.getBoard());
    }
    
    public void saveCurrentGame(Game game) {
        String folderPath = basePath + "current game";
        String filePath = folderPath + "/game.csv";
        
        // Create the folder if it doesn't exist
        File folder = new File(folderPath);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            System.out.println("Creating current game folder: " + folderPath + " - " + (created ? "SUCCESS" : "FAILED"));
        }
        
        CSVGameHandler.writeCSV(filePath, game.getBoard());
    }
    
    public void saveCurrentGameWithOriginal(Game currentGame, Game originalGame) {
        String folderPath = basePath + "current game";
        String currentFilePath = folderPath + "/game.csv";
        String originalFilePath = folderPath + "/original.csv";
        
        // Create the folder if it doesn't exist
        File folder = new File(folderPath);
        if (!folder.exists()) {
            boolean created = folder.mkdirs();
            System.out.println("Creating current game folder: " + folderPath + " - " + (created ? "SUCCESS" : "FAILED"));
        }
        
        CSVGameHandler.writeCSV(currentFilePath, currentGame.getBoard());
        CSVGameHandler.writeCSV(originalFilePath, originalGame.getBoard());
    }

    public void deleteGameFromFolder(String difficulty) {
        CSVGameHandler.deleteCSV(basePath + difficulty + "/" + difficulty + ".csv");
    }

    public void deleteCurrentGame() {
        CSVGameHandler.deleteCSV(basePath + "current game/game.csv");
        CSVGameHandler.deleteCSV(basePath + "current game/original.csv");
    }

    public Game importBoardFromFile(String filename) {
        System.out.println("Attempting to read from: " + filename);
        int[][] newBoard = null;
        try {
            newBoard = CSVGameHandler.readCSV(filename);
            if (newBoard == null) {
                System.out.println("CSVGameHandler returned null for: " + filename);
                return null;
            }
            return new Game(newBoard);
        } catch (IllegalArgumentException e) {
            System.out.println("Error reading file: " + filename);
            System.out.println("Error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Unexpected error reading: " + filename);
            e.printStackTrace();
            return null;
        }
    }
}