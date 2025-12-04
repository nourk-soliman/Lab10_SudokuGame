/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modes;

import java.util.List;
import validators.BoxValidator;
import validators.ColumnValidator;
import validators.RowValidator;

/**
 *
 * @author AltAWKEl
 */
public class Mode0Validator implements ModeValidator {

    @Override
    public boolean validate(int[][] board, List<String> errors) {

        boolean rowsValid = new RowValidator().validate(board, errors);
        boolean colsValid = new ColumnValidator().validate(board, errors);
        boolean boxesValid = new BoxValidator().validate(board, errors);
        return rowsValid && colsValid && boxesValid;
    }

}
