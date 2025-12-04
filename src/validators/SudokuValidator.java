/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validators;

import java.util.List;

/**
 *
 * @author mariam
 */
public interface SudokuValidator {

      boolean validate(int[][] board, List<String> errors);
}
