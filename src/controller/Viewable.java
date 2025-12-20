/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controller;
import controller.exceptions.SolutionInvalidException;
import controller.exceptions.InvalidGame;
import controller.exceptions.NotFoundException;
import controller.model.DifficultyEnum;
import controller.model.Game;
import controller.system.GameCatalog;
import java.io.IOException;
/**
 *
 * @author Nour
*/
    
public interface Viewable
{
  GameCatalog getCatalog();
  // Returns a random game with the specified difficulty
  // Note: the Game class is the representation of the soduko game in the controller
  Game getGame(DifficultyEnum level) throws NotFoundException;
  // Gets a sourceSolution and generates three levels of difficulty
  void driveGames(Game sourceGame) throws SolutionInvalidException;
  // Given a game, if invalid returns invalid and the locates the invalid duplicates
  // if valid and complete, return a value
  // if valid and incomplete, returns another value
  // The exact repersentation as a string is done as you best see fit
  // Example for return values:
  // Game Valid -> "valid"
  // Game incomplete -> "incomplete"
  // Game Invalid -> "invalid 1,2 3,3 6,7"
  String verifyGame(Game game);
  // returns the correct combination for the missing numbers
  // Hint: So, there are many ways you can approach this, one way is
  // to have a way to map an index in the combination array to its location in the board

  // one other way to to try to encode the location and the answer all in just one int

  int[] solveGame(Game game) throws InvalidGame;
  // Logs the user action
  void logUserAction(String userAction) throws IOException;
}
