package controller.model;

import controller.logic.CellFlyweight;
import controller.logic.CellFlyweightFactory;

public class Game {

    private int[][] board;

    public Game() {
       
    }
    
    public Game(int[][] board) {
        setBoard(board);
    }

    public void setBoard(int[][] board) {
        if (board.length != 9) {
            throw new IllegalArgumentException("Invalid number of rows.");
        }
        for (int i = 0; i < 9; i++) {
            if (board[i].length != 9) {
                throw new IllegalArgumentException("Invalid number of columns at row number " + (i + 1) + ".");
            }
            for (int j = 0; j < 9; j++) {
                if (board[i][j] < 0 || board[i][j] > 9) {
                    throw new IllegalArgumentException("Invalid number entered.");
                }
            }
        }
        this.board = board;
    }

    public int[][] getBoard() {
        return this.board;
    }

    public CellFlyweight[] findEmptyCells(int[][] board) {
        CellFlyweightFactory.reset();
        
        int zeros = 0;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) zeros++;
            }
        }

        if (zeros != 5) {
            throw new IllegalArgumentException("Exactly 5 empty cells required, found: " + zeros);
        }

        CellFlyweight[] cells = new CellFlyweight[5];
        int found = 0;

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    cells[found++] = CellFlyweightFactory.getCell(r, c);
                }
            }
        }
        return cells;
    }
}