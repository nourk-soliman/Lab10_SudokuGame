package view;

import view.adapter.Controllable;
import view.adapter.ControllerAdapter;
import controller.GameController;
import controller.GameDriver;
import controller.system.GameStorage;
import controller.exceptions.NotFoundException;
import controller.exceptions.SolutionInvalidException;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.File;

public class MenuGUI extends JFrame {
    
    private Controllable controller;
    private JButton easyBtn, mediumBtn, hardBtn, loadSolvedBtn, exitBtn;
    private JLabel titleLabel;
    private JPanel mainPanel, buttonPanel;
    private boolean hasIncomplete;
    private boolean hasAllModes;

    public MenuGUI() {
        GameDriver driver = new GameDriver();
        GameStorage storage = new GameStorage();
        GameController gameController = new GameController(driver, storage);
        this.controller = new ControllerAdapter(gameController);
        
        checkGameCatalog();
        setupUI();
    }
    
    private void checkGameCatalog() {
        boolean[] catalog = controller.getCatalog();
        if (catalog != null) {
            hasAllModes = catalog[0];
            hasIncomplete = catalog[1];
            System.out.println("=== Game Catalog Status ===");
            System.out.println("All modes exist (Easy/Medium/Hard): " + hasAllModes);
            System.out.println("Has incomplete game: " + hasIncomplete);
        } else {
            hasAllModes = false;
            hasIncomplete = false;
            System.out.println("=== Game Catalog Status ===");
            System.out.println("Catalog is NULL - no games available");
        }
    }
    
    private void setupUI() {
        setTitle("Sudoku Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 750);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with dark background
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(45, 52, 54));
        mainPanel.setBorder(new EmptyBorder(40, 60, 40, 60));
        
        // Title
        titleLabel = new JLabel("SUDOKU GAME");
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 48));
        titleLabel.setForeground(new Color(0, 184, 148));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(80));
        
        // Button panel
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(new Color(45, 52, 54));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Check if incomplete game exists
        if (hasIncomplete) {
            JButton continueBtn = createMenuButton("CONTINUE LAST GAME", new Color(116, 185, 255));
            continueBtn.addActionListener(e -> loadIncompleteGame());
            buttonPanel.add(continueBtn);
            buttonPanel.add(Box.createVerticalStrut(15));
        }
        
        // Always show difficulty buttons if modes exist
        if (hasAllModes) {
            easyBtn = createMenuButton("EASY - 10 empty cells", new Color(129, 236, 236));
            easyBtn.addActionListener(e -> startGame('e'));
            buttonPanel.add(easyBtn);
            buttonPanel.add(Box.createVerticalStrut(15));
            
            mediumBtn = createMenuButton("MEDIUM - 20 empty cells", new Color(253, 203, 110));
            mediumBtn.addActionListener(e -> startGame('m'));
            buttonPanel.add(mediumBtn);
            buttonPanel.add(Box.createVerticalStrut(15));
            
            hardBtn = createMenuButton("HARD - 25 empty cells", new Color(255, 118, 117));
            hardBtn.addActionListener(e -> startGame('h'));
            buttonPanel.add(hardBtn);
            buttonPanel.add(Box.createVerticalStrut(15));
        }
        
        // Always show load solved sudoku button
        loadSolvedBtn = createMenuButton("LOAD SOLVED SUDOKU", new Color(162, 155, 254));
        loadSolvedBtn.addActionListener(e -> loadSolvedSudoku());
        buttonPanel.add(loadSolvedBtn);
        buttonPanel.add(Box.createVerticalStrut(15));
        
        // Exit button
        exitBtn = createMenuButton("EXIT GAME", new Color(223, 230, 233));
        exitBtn.setForeground(new Color(45, 52, 54));
        exitBtn.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitBtn);
        
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JButton createMenuButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(450, 60));
        btn.setPreferredSize(new Dimension(450, 60));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }
    
    private void startGame(char difficulty) {
        try {
            int[][] game = controller.getGame(difficulty);
            if (game != null) {
                this.dispose();
                new SudokuGUI(controller, game, difficulty);
            } else {
                String diffName = (difficulty == 'e') ? "EASY" : (difficulty == 'm') ? "MEDIUM" : "HARD";
                JOptionPane.showMessageDialog(this, 
                    "Game not found for " + diffName + " level!\n\n" +
                    "The game files may not have been saved correctly.\n" +
                    "Check the console for error messages.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NotFoundException ex) {
            String diffName = (difficulty == 'e') ? "EASY" : (difficulty == 'm') ? "MEDIUM" : "HARD";
            JOptionPane.showMessageDialog(this, 
                "Game not found for " + diffName + " level!\n\n" +
                "Error: " + ex.getMessage() + "\n\n" +
                "Make sure:\n" +
                "1. The folders 'easy', 'medium', 'hard' exist\n" +
                "2. The CSV files were saved correctly\n" +
                "3. Check the paths in GameModeFactory.java",
                "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("NotFoundException: " + ex.getMessage());
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Unexpected error: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void loadIncompleteGame() {
        try {
            int[][] game = controller.getGame('i'); // 'i' for incomplete
            if (game != null) {
                this.dispose();
                new SudokuGUI(controller, game, 'i');
            }
        } catch (NotFoundException ex) {
            JOptionPane.showMessageDialog(this, 
                "Incomplete game not found!",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void loadSolvedSudoku() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Solved Sudoku CSV File");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV files", "csv"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                controller.driveGames(selectedFile.getAbsolutePath());
                
                // Show success message with options
                String[] options = {"EASY", "MEDIUM", "HARD"};
                int choice = JOptionPane.showOptionDialog(this,
                    "✓ Games generated successfully!\n\n" +
                    "Select a difficulty level to start playing:",
                    "Success - Choose Difficulty",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);
                
                // Start game based on choice
                if (choice >= 0 && choice <= 2) {
                    char difficulty = (choice == 0) ? 'e' : (choice == 1) ? 'm' : 'h';
                    this.dispose();
                    startGame(difficulty);
                }
                
            } catch (SolutionInvalidException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid solution: " + ex.getMessage() + "\n\n" +
                    "Please make sure the CSV file contains a valid, complete Sudoku solution.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new MenuGUI());
    }
}