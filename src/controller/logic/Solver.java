/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.logic;

import controller.exceptions.SolutionInvalidException;
import controller.model.Game;



public class Solver {

    public Solver() {
    }

    public static boolean solve(int[][] board) {

        // Get the 5 empty cell positions as Flyweight objects
        Game game = new Game(board);
        CellFlyweight[] emptyCells = game.findEmptyCells(board);

        PermutationIterator iterator = new PermutationIterator();
        SudokuValidator validator = new SudokuValidator();

        while (iterator.hasNext()) {

            int[] values = iterator.next();

            // Fill the empty cells using Flyweight positions
            for (int i = 0; i < 5; i++) {
                int row = emptyCells[i].getRow();
                int col = emptyCells[i].getCol();
                board[row][col] = values[i];
            }

            // Validate the full board
            try {
                validator.validateSourceSolution(board);
                // Valid solution found
                return true;

            } catch (SolutionInvalidException e) {
                // Invalid permutation, continue
            }

            // Reset cells to 0
            for (int i = 0; i < 5; i++) {
                int row = emptyCells[i].getRow();
                int col = emptyCells[i].getCol();
                board[row][col] = 0;
            }
        }

        // No valid solution found
        return false;
    }
}
