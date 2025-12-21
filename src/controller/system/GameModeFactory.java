package controller.system;

import controller.model.DifficultyEnum;
import controller.model.Game;

public class GameModeFactory {

    public Game getGameMode(DifficultyEnum difficultyChoice) {
        GameStorage storage = new GameStorage();

        if (difficultyChoice == null) return null;

        // Use relative paths or basePath from GameStorage
        String basePath = System.getProperty("user.dir") + "/";
        
        switch (difficultyChoice) {
            case EASY:
                return storage.importBoardFromFile(basePath + "easy/easy.csv");
            case MEDIUM:
                return storage.importBoardFromFile(basePath + "medium/medium.csv");
            case HARD:
                return storage.importBoardFromFile(basePath + "hard/hard.csv");
            default:
                return null;
        }
    }
}