package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ButtonPanel extends JPanel {

    private JButton verifyBtn, solveBtn, undoBtn, hintBtn, newGameBtn;
    private final Color BUTTON_VERIFY = new Color(0, 184, 148);
    private final Color BUTTON_SOLVE = new Color(85, 170, 255);
    private final Color BUTTON_UNDO = new Color(253, 203, 110);
    private final Color BUTTON_HINT = new Color(162, 155, 254);
    private final Color BUTTON_NEW = new Color(255, 118, 117);

    public ButtonPanel() {
        setupPanel();
    }
    
    private void setupPanel() {
        setLayout(new GridLayout(2, 3, 15, 15));
        setBackground(new Color(240, 240, 245));
        setBorder(new EmptyBorder(20, 50, 10, 50));
        
        // Create buttons
        verifyBtn = createButton("✓ VERIFY", BUTTON_VERIFY);
        solveBtn = createButton("🧩 SOLVE", BUTTON_SOLVE);
        undoBtn = createButton("↶ UNDO", BUTTON_UNDO);
        hintBtn = createButton("💡 HINT", BUTTON_HINT);
        newGameBtn = createButton("🔄 NEW GAME", BUTTON_NEW);
        
        // Add buttons to panel
        add(verifyBtn);
        add(solveBtn);
        add(undoBtn);
        add(hintBtn);
        add(newGameBtn);
        
        // Empty space
        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(new Color(240, 240, 245));
        add(emptyPanel);
    }
    
    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 50));
        
        // Hover effect
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
    
    // Getters for buttons
    public JButton getVerifyBtn() { return verifyBtn; }
    public JButton getSolveBtn() { return solveBtn; }
    public JButton getUndoBtn() { return undoBtn; }
    public JButton getHintBtn() { return hintBtn; }
    public JButton getNewGameBtn() { return newGameBtn; }
}