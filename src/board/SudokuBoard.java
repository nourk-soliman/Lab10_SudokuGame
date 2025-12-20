/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package board;

import java.io.IOException;
import utils.CSVReader;
import logic.CellFlyweight;
import logic.CellFlyweightFactory;
/**
 *
 * @author Nour
 */
public class SudokuBoard {

    private int[][] board;

    public SudokuBoard() {
        this.board = new int[9][9];
        this.board = null;
    }

    public void setBoard(int[][] board) {
        if (board.length != 9) {
            throw new IllegalArgumentException("Invalid number of rows.");
        }
        for (int i = 0; i < 9; i++) {
            if (board[i].length != 9) {
                throw new IllegalArgumentException("Invalid number of coloumns at row number " + (i + 1) + ".");
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

    public void importBoardFromFile(String filename) {
        int[][] newBoard;
        try {
            newBoard = CSVReader.readCSV(filename);
            setBoard(newBoard);
        } catch (IOException e) {
            System.out.println("Error in reading file.");
        } catch (IllegalArgumentException e) {
            System.out.println("Try a different file or edit the file.");
        }

    }

 public CellFlyweight[] findEmptyCells(int[][] board) {

    int zeros = 0;
    for (int r = 0; r < 9; r++) {
        for (int c = 0; c < 9; c++) {
            if (board[r][c] == 0) zeros++;
        }
    }

    if (zeros != 5) {
        throw new IllegalArgumentException("Exactly 5 empty cells required.");
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
