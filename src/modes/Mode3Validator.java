/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modes;

import java.util.ArrayList;
import java.util.List;
import validators.BoxValidator;
import validators.ColumnValidator;
import validators.RowValidator;
import validators.SudokuValidator;

/**
 *
 * @author AltAWKEl
 */
public class Mode3Validator implements ModeValidator {

    @Override
    public boolean validate(int[][] board, List<String> errors) {

        // -------------row-------------
        Thread rowThread = new Thread(() -> {
            new RowValidator().validate(board, errors);
        });

        rowThread.start();
        try {
            rowThread.join();
        } catch (InterruptedException ignored) {
        }

        // ---------------colum-----------
        Thread colThread = new Thread(() -> {
            new ColumnValidator().validate(board, errors);
        });
        colThread.start();

        try {
            colThread.join();
        } catch (InterruptedException ignored) {
        }

        // -------------- BOX -----------------
        Thread boxThread = new Thread(() -> {
            new BoxValidator().validate(board, errors);
        });
        boxThread.start();
        try {
            boxThread.join();
        } catch (InterruptedException ignored) {
        }

        return errors.isEmpty();
    }

}
