/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view.model;

/**
 *
 * @author Nour
 */
public class UserAction {
    private int row;
    private int colomn;
    private int value;
    private int previous;

    public UserAction(int row, int colomn, int value, int previous) {
        this.row = row;
        this.colomn = colomn;
        this.value = value;
        this.previous = previous;
    }

    public int getRow() {
        return row;
    }

    public int getColomn() {
        return colomn;
    }

    public int getValue() {
        return value;
    }

    public int getPrevious() {
        return previous;
    }
    
    
    
}
