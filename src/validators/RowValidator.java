package validators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mariam
 */
public class RowValidator implements SudokuValidator {

    private final Integer rowIndex;//to allow null

    public RowValidator() {
        this.rowIndex = null;
    }

    public RowValidator(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    @Override
    public boolean validate(int[][] board, List<String> errors) {
        if (rowIndex != null) {
            // Validate single row
            return validateSingleRow(board, errors, rowIndex);
        } else {
            // Validate all rows
            return validateAllRows(board, errors);
        }
    }

    public boolean validateAllRows(int[][] board, List<String> errors) {
        boolean allValid = true;
        for (int i = 0; i < 9; i++) {
            if (!validateSingleRow(board, errors, i)) {
                allValid = false; 
            }
        }
        return allValid;
    }

    public boolean validateSingleRow(int[][] board, List<String> errors, int rowIndex) {
        Set<Integer> seen = new HashSet<>();
        Set<Integer> reportedDuplicates = new HashSet<>(); // to check whether we already checked that duplicate or not
        boolean flag = true;

        for (int j = 0; j < 9; j++) {
            int num = board[rowIndex][j];
            if (num != 0 && !seen.add(num) && !reportedDuplicates.contains(num)) {
                StringBuilder positions = new StringBuilder();// to check the positions of the duplicate
                for (int col = 0; col < 9; col++) {
                    if (board[rowIndex][col] == num) {
                        positions.append(col + 1).append(" ");
                    }
                }
                errors.add("ROW " + (rowIndex + 1) + ", #" + num + ", [" + positions.toString().trim() + "]");
                reportedDuplicates.add(num);
                flag = false;
            }
        }
        return flag;
    }
}
