/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.model;

/*
 * @author Mariam
 */
public enum DifficultyEnum {
    EASY(10),
    MEDIUM(20),
    HARD(25);
    private final int removedCells;

    DifficultyEnum(int removedCells) {
        this.removedCells = removedCells;
    }
    public int getRemovedCells() {
        return removedCells;
    }
}