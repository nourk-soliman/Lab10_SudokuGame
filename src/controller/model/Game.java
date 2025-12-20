/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.model;

/**
 *
 * @author Mariam
 */
public class Game {
    private int[][] board;
    private DifficultyEnum difficulty;

  public Game(int[][] board, DifficultyEnum difficulty) {
        this.board = board;
        this.difficulty = difficulty;
    }
    public int[][] getBoard() {
        return board;
    }   
    public DifficultyEnum getDifficulty() {
        return difficulty;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }
}