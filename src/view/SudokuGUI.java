package view;

import view.adapter.Controllable;
import view.model.UserAction;
import controller.exceptions.InvalidGame;
import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.*;
import java.io.IOException;

public class SudokuGUI extends JFrame {
    
    private Controllable controller;
    private int[][] gameBoard;
    private int[][] originalBoard;
    private JTextField[][] cells;
    private JButton verifyBtn, solveBtn, undoBtn, newGameBtn;
    private JLabel difficultyLabel, emptyCellsLabel;
    private char difficulty;
    private int emptyCells;
    
    private final Color FIXED_CELL_BG = new Color(240, 240, 240);
    private final Color EDITABLE_CELL_BG = Color.WHITE;
    private final Color INVALID_CELL_BG = new Color(255, 200, 200);
    private final Color GRID_COLOR = new Color(45, 52, 54);
    private final Color BUTTON_VERIFY = new Color(0, 184, 148);
    private final Color BUTTON_SOLVE = new Color(85, 170, 255);
    private final Color BUTTON_UNDO = new Color(253, 203, 110);
    private final Color BUTTON_NEW = new Color(255, 118, 117);

    public SudokuGUI(Controllable controller, int[][] game, char difficulty) {
        this.controller = controller;
        this.gameBoard = copyBoard(game);
        this.originalBoard = copyBoard(game);
        this.difficulty = difficulty;
        this.emptyCells = countEmptyCells();
        
        setupUI();
    }
    
    private void setupUI() {
        setTitle("Sudoku Game - " + getDifficultyName());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(650, 750);
        setLocationRelativeTo(null);
        setResizable(false);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                handleWindowClose();
            }
        });
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 245));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createGridPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private String getDifficultyName() {
        return difficulty == 'e' || difficulty == 'E' ? "EASY" :
               difficulty == 'm' || difficulty == 'M' ? "MEDIUM" : "HARD";
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new GridLayout(3, 1, 5, 5));
        header.setBackground(new Color(240, 240, 245));
        
        JLabel title = new JLabel("Sudoku Game - " + getDifficultyName(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(new Color(45, 52, 54));
        
        difficultyLabel = new JLabel("Difficulty: " + getDifficultyName(), SwingConstants.CENTER);
        difficultyLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        
        emptyCellsLabel = new JLabel("Empty cells: " + emptyCells, SwingConstants.CENTER);
        emptyCellsLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        
        header.add(title);
        header.add(difficultyLabel);
        header.add(emptyCellsLabel);
        
        return header;
    }
    
    private JPanel createGridPanel() {
        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        gridPanel.setBackground(GRID_COLOR);
        gridPanel.setBorder(BorderFactory.createLineBorder(GRID_COLOR, 3));
        
        cells = new JTextField[9][9];
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col] = createCell(row, col);
                gridPanel.add(cells[row][col]);
            }
        }
        
        return gridPanel;
    }
    
    private JTextField createCell(int row, int col) {
        JTextField cell = new JTextField();
        cell.setHorizontalAlignment(JTextField.CENTER);
        cell.setFont(new Font("Arial", Font.BOLD, 20));
        
        int top = (row % 3 == 0) ? 2 : 1;
        int left = (col % 3 == 0) ? 2 : 1;
        int bottom = (row == 8) ? 2 : 1;
        int right = (col == 8) ? 2 : 1;
        cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, GRID_COLOR));
        
        if (gameBoard[row][col] != 0) {
            cell.setText(String.valueOf(gameBoard[row][col]));
            cell.setEditable(false);
            cell.setBackground(FIXED_CELL_BG);
            cell.setForeground(Color.BLACK);
        } else {
            cell.setBackground(EDITABLE_CELL_BG);
            cell.setForeground(new Color(0, 119, 182));
        }
        
        final int finalRow = row;
        final int finalCol = col;
        
        cell.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateCell(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateCell(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateCell(); }
            
            private void updateCell() {
                SwingUtilities.invokeLater(() -> {
                    String text = cell.getText();
                    if (text.length() > 1) {
                        cell.setText(text.substring(text.length() - 1));
                    }
                    
                    if (!text.isEmpty() && !text.matches("[1-9]")) {
                        cell.setText("");
                        return;
                    }
                    
                    int previousValue = gameBoard[finalRow][finalCol];
                    int newValue = text.isEmpty() ? 0 : Integer.parseInt(text);
                    
                    if (newValue != previousValue) {
                        gameBoard[finalRow][finalCol] = newValue;
                        emptyCells = countEmptyCells();
                        emptyCellsLabel.setText("Empty cells: " + emptyCells);
                        
                        try {
                            UserAction action = new UserAction(finalRow, finalCol, newValue, previousValue);
                            controller.logUserAction(action);
                            saveCurrentGameState();
                        } catch (IOException ex) {
                            System.err.println("Failed to log action: " + ex.getMessage());
                        }
                        
                        solveBtn.setEnabled(emptyCells == 5);
                        
                        if (emptyCells == 0) {
                            verifyGame();
                        }
                    }
                });
            }
        });
        
        return cell;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBackground(new Color(240, 240, 245));
        panel.setBorder(new EmptyBorder(10, 80, 0, 80));
        
        verifyBtn = createStyledButton("✓ VERIFY", BUTTON_VERIFY);
        verifyBtn.addActionListener(e -> verifyGame());
        
        solveBtn = createStyledButton("🧩 SOLVE", BUTTON_SOLVE);
        solveBtn.addActionListener(e -> solveGame());
        solveBtn.setEnabled(emptyCells == 5);
        
        undoBtn = createStyledButton("↶ UNDO", BUTTON_UNDO);
        undoBtn.addActionListener(e -> undoLastMove());
        
        newGameBtn = createStyledButton("🔄 NEW GAME", BUTTON_NEW);
        newGameBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Start a new game? Current progress will be lost.",
                "New Game",
                JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
                new MenuGUI();
            }
        });
        
        panel.add(verifyBtn);
        panel.add(solveBtn);
        panel.add(undoBtn);
        panel.add(newGameBtn);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 50));
        return button;
    }
    
    private void verifyGame() {
        try {
            boolean[][] validity = controller.verifyGame(gameBoard);
            
            if (validity == null) {
                JOptionPane.showMessageDialog(this, 
                    "Unable to verify game. Please check your input.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean allValid = true;
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    if (originalBoard[row][col] == 0) {
                        if (validity[row][col]) {
                            cells[row][col].setBackground(EDITABLE_CELL_BG);
                        } else {
                            cells[row][col].setBackground(INVALID_CELL_BG);
                            allValid = false;
                        }
                    }
                }
            }
            
            if (allValid && emptyCells == 0) {
                JOptionPane.showMessageDialog(this, "Congratulations! You solved it!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else if (allValid) {
                JOptionPane.showMessageDialog(this, "So far, so good! Keep going!", "Valid", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Some cells are incorrect (shown in red)", "Invalid", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error verifying game: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void solveGame() {
        if (emptyCells != 5) {
            JOptionPane.showMessageDialog(this, "Solve only works when exactly 5 cells are empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        System.out.println("Attempting to solve...");
        System.out.println("Empty cells: " + emptyCells);
        
        try {
            int[][] solutions = controller.solveGame(gameBoard);
            
            System.out.println("Solutions returned: " + (solutions != null ? solutions.length : "null"));
            
            if (solutions == null || solutions.length == 0) {
                JOptionPane.showMessageDialog(this, "No solution found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            for (int i = 0; i < solutions.length; i++) {
                int row = solutions[i][0];
                int col = solutions[i][1];
                int value = solutions[i][2];
                
                System.out.println("Filling cell [" + row + "," + col + "] = " + value);
                
                gameBoard[row][col] = value;
                cells[row][col].setText(String.valueOf(value));
                cells[row][col].setBackground(new Color(200, 255, 200));
            }
            
            emptyCells = 0;
            emptyCellsLabel.setText("Empty cells: 0");
            solveBtn.setEnabled(false);
            
            JOptionPane.showMessageDialog(this, "Puzzle solved!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (InvalidGame ex) {
            System.err.println("Solve error: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            System.err.println("Unexpected error: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Unexpected error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void undoLastMove() {
        try {
            int[][] updatedBoard = controller.undoLastMove(gameBoard);
            
            if (updatedBoard == null) {
                JOptionPane.showMessageDialog(this, "No moves to undo!", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    if (originalBoard[row][col] == 0 && gameBoard[row][col] != updatedBoard[row][col]) {
                        gameBoard[row][col] = updatedBoard[row][col];
                        cells[row][col].setText(updatedBoard[row][col] == 0 ? "" : String.valueOf(updatedBoard[row][col]));
                        cells[row][col].setBackground(EDITABLE_CELL_BG);
                    }
                }
            }
            
            emptyCells = countEmptyCells();
            emptyCellsLabel.setText("Empty cells: " + emptyCells);
            solveBtn.setEnabled(emptyCells == 5);
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error undoing move: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int countEmptyCells() {
        int count = 0;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (gameBoard[row][col] == 0) {
                    count++;
                }
            }
        }
        return count;
    }
    
    private int[][] copyBoard(int[][] board) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, 9);
        }
        return copy;
    }
    
    private void saveCurrentGameState() {
        try {
            String basePath = System.getProperty("user.dir") + "/";
            String folderPath = basePath + "current game";
            
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            
            String filePath = folderPath + "/game.csv";
            java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(filePath));
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    writer.write(String.valueOf(gameBoard[row][col]));
                    if (col < 8) writer.write(",");
                }
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Failed to save current game: " + e.getMessage());
        }
    }
    
    private void handleWindowClose() {
        if (emptyCells == 0) {
            dispose();
            System.exit(0);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Do you want to save your current game progress?",
            "Save Game",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            saveCurrentGameState();
            JOptionPane.showMessageDialog(this, 
                "Game saved! You can resume later.", 
                "Saved", 
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            System.exit(0);
        } else if (choice == JOptionPane.NO_OPTION) {
            deleteCurrentGame();
            dispose();
            System.exit(0);
        }
    }
    
    private void deleteCurrentGame() {
        try {
            String basePath = System.getProperty("user.dir") + "/";
            String folderPath = basePath + "current game";
            
            File gameFile = new File(folderPath + "/game.csv");
            if (gameFile.exists()) {
                gameFile.delete();
            }
            
            File logFile = new File(folderPath + "/incomplete.log");
            if (logFile.exists()) {
                logFile.delete();
            }
        } catch (Exception e) {
            System.err.println("Error deleting current game: " + e.getMessage());
        }
    }
}