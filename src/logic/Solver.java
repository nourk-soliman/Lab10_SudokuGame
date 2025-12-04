/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import board.SudokuBoard;

/**
 *
 * @author Ali
 */
public class Solver {

    public Solver() {
    }
    public static boolean solve()
    SudokuBoard board=new SudokuBoard();
    int[][] empties=board.findEmptyCells(board);
}
