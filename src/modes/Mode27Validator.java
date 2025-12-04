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
public class Mode27Validator implements ModeValidator {

@Override
public boolean validate(int[][] board, List<String> errors) {

    // ---------------- rows  ----------------
    for (int r = 0; r < 9; r++) {
        int row = r;
        Thread t = new Thread(() -> {
            new RowValidator(row).validate(board, errors);
        });

        t.start();
        try {
            t.join();  
        } catch (InterruptedException e) {
        }
    }

    // ---------------- colums-------------
    for (int c = 0; c < 9; c++) {
        int col = c;
        Thread t = new Thread(() -> {
            new ColumnValidator(col).validate(board, errors);
        });

        t.start();
        try {
            t.join();   
        } catch (InterruptedException e) {
        }
    }

    // ---------------- boxes---------------
    for (int b = 0; b < 9; b++) {
        int boxRow = b / 3;
        int boxCol = b % 3;

        Thread t = new Thread(() -> {
            new BoxValidator(boxRow, boxCol).validate(board, errors);
        });

        t.start();
        try {
            t.join();   
        } catch (InterruptedException e) {
        }
    }

    return errors.isEmpty();
}


}
