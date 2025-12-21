package view;

import controller.GameController;
import controller.GameDriver;
import controller.Viewable;
import controller.exceptions.NotFoundException;
import controller.exceptions.SolutionInvalidException;
import controller.system.GameStorage;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import view.adapter.Controllable;
import view.adapter.ControllerAdapter;

public class MenuGUI extends JFrame {
    
    private Controllable controller;
    private JButton continueBtn, easyBtn, mediumBtn, hardBtn, loadSolvedBtn, exitBtn;
    private JLabel titleLabel;
    private JPanel mainPanel, buttonPanel;
    
    private final Color BG_COLOR = new Color(45, 52, 54);
    private final Color TITLE_COLOR = new Color(0, 206, 201);
    private final Color CONTINUE_BTN = new Color(46, 213, 115);
    private final Color EASY_BTN = new Color(129, 236, 236);
    private final Color MEDIUM_BTN = new Color(250, 211, 144);
    private final Color HARD_BTN = new Color(255, 159, 128);
    private final Color LOAD_BTN = new Color(108, 117, 125);
    private final Color EXIT_BTN = new Color(222, 165, 164);

    public MenuGUI() {
        GameStorage storage = new GameStorage();
        GameDriver driver = new GameDriver();
        Viewable gameController = new GameController(driver, storage);
        this.controller = new ControllerAdapter(gameController);
        
        boolean[] catalog = controller.getCatalog();
        System.out.println("=== Game Catalog Status ===");
        System.out.println("All modes exist (Easy/Medium/Hard): " + catalog[0]);
        System.out.println("Has incomplete game: " + catalog[1]);
        
        setupUI(catalog);
    }
    
    private void setupUI(boolean[] catalog) {
        setTitle("Sudoku Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        
        int windowWidth = Math.min(600, (int)(screenWidth * 0.5));
        int windowHeight = Math.min(700, (int)(screenHeight * 0.8));
        
        setSize(windowWidth, windowHeight);
        setLocationRelativeTo(null);
        setResizable(true);
        setMinimumSize(new Dimension(400, 500));
        
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(BG_COLOR);
        
        int padding = Math.max(20, windowWidth / 15);
        mainPanel.setBorder(new EmptyBorder(padding, padding, padding, padding));
        
        titleLabel = new JLabel("SUDOKU GAME", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, Math.min(48, windowWidth / 12)));
        titleLabel.setForeground(TITLE_COLOR);
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(createButtonPanel(catalog), BorderLayout.CENTER);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createButtonPanel(boolean[] catalog) {
        boolean allModesExist = catalog[0];
        boolean hasIncompleteGame = catalog[1];
        
        buttonPanel = new JPanel(new GridLayout(hasIncompleteGame ? 6 : 5, 1, 15, 15));
        buttonPanel.setBackground(BG_COLOR);
        
        if (hasIncompleteGame) {
            continueBtn = createStyledButton("CONTINUE CURRENT GAME", CONTINUE_BTN);
            continueBtn.addActionListener(e -> loadCurrentGame());
            buttonPanel.add(continueBtn);
        }
        
        if (allModesExist) {
            easyBtn = createStyledButton("EASY - 10 empty cells", EASY_BTN);
            easyBtn.addActionListener(e -> startGame('e'));
            buttonPanel.add(easyBtn);
            
            mediumBtn = createStyledButton("MEDIUM - 20 empty cells", MEDIUM_BTN);
            mediumBtn.addActionListener(e -> startGame('m'));
            buttonPanel.add(mediumBtn);
            
            hardBtn = createStyledButton("HARD - 25 empty cells", HARD_BTN);
            hardBtn.addActionListener(e -> startGame('h'));
            buttonPanel.add(hardBtn);
        }
        
        loadSolvedBtn = createStyledButton("LOAD SOLVED SUDOKU", LOAD_BTN);
        loadSolvedBtn.addActionListener(e -> loadSolvedSudoku());
        buttonPanel.add(loadSolvedBtn);
        
        exitBtn = createStyledButton("EXIT GAME", EXIT_BTN);
        exitBtn.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitBtn);
        
        return buttonPanel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(bgColor == EASY_BTN || bgColor == MEDIUM_BTN ? Color.BLACK : Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void loadCurrentGame() {
        try {
            String basePath = System.getProperty("user.dir") + "/";
            String gameFilePath = basePath + "current game/game.csv";
            String originalFilePath = basePath + "current game/original.csv";
            
            File gameFile = new File(gameFilePath);
            File originalFile = new File(originalFilePath);
            
            if (!gameFile.exists()) {
                JOptionPane.showMessageDialog(this, 
                    "No saved game found!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            GameStorage storage = new GameStorage();
            int[][] currentGame = storage.importBoardFromFile(gameFilePath).getBoard();
            
            // Load original board if it exists, otherwise use current as original
            int[][] originalBoard;
            if (originalFile.exists()) {
                originalBoard = storage.importBoardFromFile(originalFilePath).getBoard();
            } else {
                // Fallback: treat all zeros in current game as editable
                originalBoard = createOriginalFromCurrent(currentGame);
            }
            
            char difficulty = determineDifficulty(originalBoard);
            
            dispose();
            new SudokuGUI(controller, currentGame, originalBoard, difficulty);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading game: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int[][] createOriginalFromCurrent(int[][] currentBoard) {
        // Create an original board where all zeros remain as zeros (editable)
        // and non-zeros remain as non-zeros (but we can't distinguish user-entered from original)
        // This is a fallback for games saved before the original.csv feature
        int[][] original = new int[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                original[row][col] = currentBoard[row][col];
            }
        }
        return original;
    }
    
    private char determineDifficulty(int[][] board) {
        int emptyCells = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) emptyCells++;
            }
        }
        
        if (emptyCells <= 12) return 'e';
        else if (emptyCells <= 23) return 'm';
        else return 'h';
    }
    
    private void startGame(char level) {
        try {
            int[][] game = controller.getGame(level);
            dispose();
            // For new games, the original is the same as the starting game
            new SudokuGUI(controller, game, game, level);
        } catch (NotFoundException ex) {
            JOptionPane.showMessageDialog(this, 
                "Game not found: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadSolvedSudoku() {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        fileChooser.setDialogTitle("Select Solved Sudoku CSV File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            
            try {
                controller.driveGames(filePath);
                
                JOptionPane.showMessageDialog(this, 
                    "Games generated successfully!\nEasy, Medium, and Hard games are now available.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                dispose();
                new MenuGUI();
                
            } catch (SolutionInvalidException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid solution: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Error: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuGUI());
    }
}