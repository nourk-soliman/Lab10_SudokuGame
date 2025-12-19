/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import exceptions.SolutionInvalidException;

/**
 *
 * @author Mariam
 */
public class SudokuValidator {

      public void validateSourceSolution(int[][] board) throws SolutionInvalidException{
        if (!isBoardValid(board)) {
            throw new SolutionInvalidException("The Sudoku solution is invalid.");
        }
        if (!isComplete(board)) {
            throw new SolutionInvalidException("The Sudoku board is not complete.");
        }
        System.out.println("The Sudoku solution is valid.");
        
      }

      private boolean isBoardValid(int[][] board) {
        return isValidFormat(board) &&
         validateAllRows(board) && validateAllcolumns(board) &&
          validateAllBoxes(board);
      }
      private boolean isValidFormat(int[][] board) {//check if all values are between 0 and 9
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                int value = board[i][j];
                if(value < 0 || value > 9){
                    System.out.println("Invalid value found: " + value);
                    return false;
                }
            }
        }
        return true;
    }
    private boolean isComplete(int[][] board) {//check if there is any 0 in the board
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    System.out.println("Board is not complete");
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateAllRows(int[][] board) {
        for (int row = 0; row < 9; row++) {
            if (hasDuplicates(board[row])) {
                return false;
            }
        }
        return true;
    }

    private boolean validateAllcolumns(int[][] board){
        for(int column = 0; column<9;column++){
            int[] colArray = new int[9];
            for(int row = 0; row<9;row++){
                colArray[row] = board[row][column];
            }
            if(hasDuplicates(colArray)){
                return false;
            }
        }
        return true;
    }

    private boolean validateAllBoxes(int[][] board){
        for(int boxRow = 0; boxRow < 3; boxRow++){
            for(int boxCol = 0; boxCol < 3; boxCol++){
                int[] boxArray = new int[9];
                int index = 0;
                for(int row = boxRow * 3; row < boxRow * 3 + 3; row++){
                    for(int col = boxCol * 3; col < boxCol * 3 + 3; col++){
                        boxArray[index++] = board[row][col];
                    }
                }
                if(hasDuplicates(boxArray)){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasDuplicates(int[] array) {
        boolean[] seen = new boolean[10];

        for (int value : array) {
            if (value != 0) {
                if (seen[value]) {
                    return true;
                }
                seen[value] = true;
            }
        }
        return false;
    }

}

