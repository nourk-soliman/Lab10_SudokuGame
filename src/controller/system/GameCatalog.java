package controller.system;

import java.io.File;

public class GameCatalog {
    
    private boolean current;
    private boolean allModesExist;

    public GameCatalog(boolean current, boolean allModesExist) {
        this.current = current;
        this.allModesExist = allModesExist;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean isAllModesExist() {
        return allModesExist;
    }

    public void setAllModesExist(boolean allModesExist) {
        this.allModesExist = allModesExist;
    }
    
    public static GameCatalog checkGames() {
        String basePath = System.getProperty("user.dir") + "/";
        String[] difficultyPaths = {"easy", "medium", "hard"};
        
        System.out.println("=== Checking Game Catalog ===");
        System.out.println("Base path: " + basePath);
        
        boolean current = false;
        File currentFolder = new File(basePath + "current game");
        
        if (currentFolder.isDirectory() && currentFolder.list() != null && currentFolder.list().length > 0) {
            current = true;
            System.out.println("Current game exists: YES");
        } else {
            System.out.println("Current game exists: NO");
        }
        
        boolean allModesExist = true;
        for (int i = 0; i < 3; i++) {
            File folder = new File(basePath + difficultyPaths[i]);
            File csvFile = new File(basePath + difficultyPaths[i] + "/" + difficultyPaths[i] + ".csv");
            
            if (!folder.isDirectory()) {
                System.out.println(difficultyPaths[i] + " folder: NOT FOUND");
                allModesExist = false;
            } else if (!csvFile.exists()) {
                System.out.println(difficultyPaths[i] + " CSV: NOT FOUND");
                allModesExist = false;
            } else {
                System.out.println(difficultyPaths[i] + " game: FOUND");
            }
        }
        
        System.out.println("All modes exist: " + allModesExist);
        System.out.println("=============================");
        
        return new GameCatalog(current, allModesExist);
    }
}