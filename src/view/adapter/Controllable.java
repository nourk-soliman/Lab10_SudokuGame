/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package view.adapter;
import controller.exceptions.InvalidGame;
import controller.exceptions.NotFoundException;
import controller.exceptions.SolutionInvalidException;
import java.io.IOException;
import view.model.UserAction;


/**
 *
 * @author Nour
 */
public interface Controllable {
   boolean[] getCatalog();
  int[][] getGame(char level) throws NotFoundException;
  void driveGames(String sourcePath) throws SolutionInvalidException;
  // A boolean array which says if a specifc cell is correct or invalid
  boolean[][] verifyGame(int[][] game);
  // contains the cell x, y and solution for each missing cell
  int[][] solveGame(int[][] game) throws InvalidGame;
  // Logs the user action
  void logUserAction(UserAction userAction) throws IOException;
 int[][] undoLastMove(int[][]board) throws IOException;
 // Gets the original board for a saved game
 int[][] getOriginalBoard();
}