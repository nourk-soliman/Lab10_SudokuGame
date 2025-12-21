package view;

import view.adapter.Controllable;
import view.model.UserAction;
import controller.exceptions.InvalidGame;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.IOException;

public class SudokuGUI extends JFrame {
    
    private Controllable controller;
    private int[][] gameBoard;
    private int[][] originalBoard;
    private JTextField[][] cells;
    private JButton verifyBtn, solveBtn, undoBtn, hintBtn, newGameBtn;
    private JLabel difficultyLabel, emptyCellsLabel;
    private char difficulty;
    private int emptyCells;
    
    // Colors
    private final Color FIXED_CELL_BG = new Color(240, 240, 240);
    private final Color EDITABLE_CELL_BG = Color.WHITE;
    private final Color INVALID_CELL_BG = new Color(255, 200, 200);
    private final Color GRID_COLOR = new Color(45, 52, 54);
    private final Color BUTTON_VERIFY = new Color(0, 184, 148);
    private final Color BUTTON_SOLVE = new Color(85, 170, 255);
    private final Color BUTTON_UNDO = new Color(253, 203, 110);
    private final Color BUTTON_HINT = new Color(162, 155, 254);
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 900);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 245));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Top panel with title and info
        JPanel topPanel = createTopPanel();
        mainPanel.add(topPanel, BorderLayout.NORTH);
        
        // Sudoku grid
        JPanel gridPanel = createGridPanel();
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        updateSolveButtonState();
        setVisible(true);
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 240, 245));
        panel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Title
        JLabel titleLabel = new JLabel("Sudoku Game - " + getDifficultyName(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 28));
        titleLabel.setForeground(new Color(45, 52, 54));
        
        // Info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 5));
        infoPanel.setBackground(new Color(240, 240, 245));
        
        difficultyLabel = new JLabel("Difficulty: " + getDifficultyName());
        difficultyLabel.setFont(new Font("Arial", Font.BOLD, 16));
        difficultyLabel.setForeground(new Color(45, 52, 54));
        
        emptyCellsLabel = new JLabel("Empty cells: " + emptyCells);
        emptyCellsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        emptyCellsLabel.setForeground(new Color(45, 52, 54));
        
        infoPanel.add(difficultyLabel);
        infoPanel.add(emptyCellsLabel);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createGridPanel() {
        JPanel gridPanel = new JPanel(new GridLayout(9, 9, 0, 0));
        gridPanel.setBackground(GRID_COLOR);
        gridPanel.setBorder(BorderFactory.createLineBorder(GRID_COLOR, 3));
        gridPanel.setPreferredSize(new Dimension(630, 630));
        
        cells = new JTextField[9][9];
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                cells[row][col] = createCell(row, col);
                gridPanel.add(cells[row][col]);
            }
        }
        
        // Center the grid
        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(new Color(240, 240, 245));
        container.add(gridPanel);
        
        return container;
    }
    
    private JTextField createCell(int row, int col) {
        JTextField cell = new JTextField();
        cell.setHorizontalAlignment(JTextField.CENTER);
        cell.setFont(new Font("Arial", Font.BOLD, 24));
        
        // Set border for 3x3 boxes
        int top = (row % 3 == 0) ? 2 : 1;
        int left = (col % 3 == 0) ? 2 : 1;
        int bottom = (row == 8) ? 2 : 1;
        int right = (col == 8) ? 2 : 1;
        cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, GRID_COLOR));
        
        boolean isFixed = originalBoard[row][col] != 0;
        
        if (isFixed) {
            cell.setText(String.valueOf(originalBoard[row][col]));
            cell.setEditable(false);
            cell.setBackground(FIXED_CELL_BG);
            cell.setForeground(Color.BLACK);
        } else {
            if (gameBoard[row][col] != 0) {
                cell.setText(String.valueOf(gameBoard[row][col]));
            }
            cell.setEditable(true);
            cell.setBackground(EDITABLE_CELL_BG);
            cell.setForeground(new Color(0, 102, 204));
        }
        
        final int finalRow = row;
        final int finalCol = col;
        
        // Add document listener for input
        cell.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateCell(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateCell(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateCell(); }
            
            private void updateCell() {
                SwingUtilities.invokeLater(() -> {
                    String text = cell.getText();
                    if (text.length() > 1) {
                        cell.setText(text.substring(text.length() - 1));
                        return;
                    }
                    
                    int previousValue = gameBoard[finalRow][finalCol];
                    
                    if (text.isEmpty()) {
                        gameBoard[finalRow][finalCol] = 0;
                    } else {
                        try {
                            int value = Integer.parseInt(text);
                            if (value >= 1 && value <= 9) {
                                gameBoard[finalRow][finalCol] = value;
                            } else {
                                cell.setText("");
                                gameBoard[finalRow][finalCol] = 0;
                            }
                        } catch (NumberFormatException ex) {
                            cell.setText("");
                            gameBoard[finalRow][finalCol] = 0;
                        }
                    }
                    
                    // Log user action
                    if (!isFixed && gameBoard[finalRow][finalCol] != previousValue) {
                        try {
                            UserAction action = new UserAction(finalRow, finalCol, 
                                gameBoard[finalRow][finalCol], previousValue);
                            controller.logUserAction(action);
                        } catch (IOException ex) {
                            System.err.println("Error logging action: " + ex.getMessage());
                        }
                    }
                    
                    emptyCells = countEmptyCells();
                    emptyCellsLabel.setText("Empty cells: " + emptyCells);
                    updateSolveButtonState();
                    
                    // Check if board is complete
                    if (emptyCells == 0) {
                        checkBoardCompletion();
                    }
                });
            }
        });
        
        return cell;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 3, 15, 15));
        panel.setBackground(new Color(240, 240, 245));
        panel.setBorder(new EmptyBorder(20, 50, 10, 50));
        
        // Verify button
        verifyBtn = createGameButton("✓ VERIFY", BUTTON_VERIFY);
        verifyBtn.addActionListener(e -> verifyBoard());
        
        // Solve button
        solveBtn = createGameButton("🧩 SOLVE", BUTTON_SOLVE);
        solveBtn.addActionListener(e -> solveBoard());
        
        // Undo button
        undoBtn = createGameButton("↶ UNDO", BUTTON_UNDO);
        undoBtn.addActionListener(e -> undoLastMove());
        
        // Hint button
        hintBtn = createGameButton("💡 HINT", BUTTON_HINT);
        hintBtn.addActionListener(e -> showHint());
        
        // New Game button
        newGameBtn = createGameButton("🔄 NEW GAME", BUTTON_NEW);
        newGameBtn.addActionListener(e -> newGame());
        
        // Empty space
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(new Color(240, 240, 245));
        
        panel.add(verifyBtn);
        panel.add(solveBtn);
        panel.add(undoBtn);
        panel.add(hintBtn);
        panel.add(newGameBtn);
        panel.add(emptyPanel);
        
        return panel;
    }
    
    private JButton createGameButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 50));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(bgColor.brighter());
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }
    
    private void verifyBoard() {
        boolean[][] validation = controller.verifyGame(gameBoard);
        
        if (validation == null) {
            JOptionPane.showMessageDialog(this, "Error verifying board!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        boolean hasInvalid = false;
        
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (!validation[row][col]) {
                    cells[row][col].setBackground(INVALID_CELL_BG);
                    hasInvalid = true;
                } else if (originalBoard[row][col] == 0) {
                    cells[row][col].setBackground(EDITABLE_CELL_BG);
                }
            }
        }
        
        if (hasInvalid) {
            JOptionPane.showMessageDialog(this, 
                "Board has invalid cells! Check the highlighted cells.",
                "Invalid Board", JOptionPane.WARNING_MESSAGE);
        } else if (emptyCells == 0) {
            JOptionPane.showMessageDialog(this, 
                "Congratulations! The puzzle is solved correctly!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Board is valid so far! Keep going!",
                "Valid", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void solveBoard() {
        try {
            int[][] solution = controller.solveGame(gameBoard);
            
            if (solution != null) {
                for (int i = 0; i < solution.length; i++) {
                    int row = solution[i][0];
                    int col = solution[i][1];
                    int value = solution[i][2];
                    
                    gameBoard[row][col] = value;
                    cells[row][col].setText(String.valueOf(value));
                    cells[row][col].setForeground(new Color(0, 153, 0));
                }
                
                emptyCells = countEmptyCells();
                emptyCellsLabel.setText("Empty cells: " + emptyCells);
                updateSolveButtonState();
                
                JOptionPane.showMessageDialog(this, 
                    "Puzzle solved successfully!",
                    "Solved", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (InvalidGame ex) {
            JOptionPane.showMessageDialog(this, 
                "Error solving: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void undoLastMove() {
        try {
            int[][] updatedBoard = controller.undoLastMove(gameBoard);
            if (updatedBoard != null) {
                gameBoard = copyBoard(updatedBoard);
                refreshBoard();
                emptyCells = countEmptyCells();
                emptyCellsLabel.setText("Empty cells: " + emptyCells);
                updateSolveButtonState();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No moves to undo!",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error undoing move: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showHint() {
        JOptionPane.showMessageDialog(this, 
            "Look for cells with only one possible value!\nCheck rows, columns, and 3x3 boxes.",
            "Hint", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void newGame() {
        int choice = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to start a new game? Current progress will be lost.",
            "New Game", JOptionPane.YES_NO_OPTION);
        
        if (choice == JOptionPane.YES_OPTION) {
            this.dispose();
            new MenuGUI();
        }
    }
    
    private void checkBoardCompletion() {
        boolean[][] validation = controller.verifyGame(gameBoard);
        boolean isValid = true;
        
        if (validation != null) {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    if (!validation[row][col]) {
                        isValid = false;
                        break;
                    }
                }
                if (!isValid) break;
            }
            
            if (isValid) {
                JOptionPane.showMessageDialog(this, 
                    "🎉 Congratulations! You solved the puzzle correctly! 🎉",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void refreshBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (originalBoard[row][col] == 0) {
                    if (gameBoard[row][col] == 0) {
                        cells[row][col].setText("");
                    } else {
                        cells[row][col].setText(String.valueOf(gameBoard[row][col]));
                    }
                }
            }
        }
    }
    
    private void updateSolveButtonState() {
        solveBtn.setEnabled(emptyCells == 5);
        if (!solveBtn.isEnabled()) {
            solveBtn.setBackground(new Color(180, 180, 180));
        } else {
            solveBtn.setBackground(BUTTON_SOLVE);
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
    
    private String getDifficultyName() {
        switch (difficulty) {
            case 'e':
            case 'E':
                return "EASY";
            case 'm':
            case 'M':
                return "MEDIUM";
            case 'h':
            case 'H':
                return "HARD";
            case 'i':
                return "INCOMPLETE";
            default:
                return "UNKNOWN";
        }
    }
}