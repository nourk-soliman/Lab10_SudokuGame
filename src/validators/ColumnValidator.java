package validators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mariam
 */
public class ColumnValidator implements SudokuValidator {

    private final Integer columnIndex;//to allow null

    public ColumnValidator() {
        this.columnIndex = null;
    }

    public ColumnValidator(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    @Override
    public boolean validate(int[][] board, List<String> errors) {
        if (columnIndex != null) {
            // Validate single column
            return validateSingleColumn(board, errors, columnIndex);
        } else {
            // Validate all columns
            return validateAllColumns(board, errors);
        }
    }

 public boolean validateAllColumns(int[][] board, List<String> errors) {
    boolean allValid = true;
    for (int i = 0; i < 9; i++) {
        if (!validateSingleColumn(board, errors, i)) {
            allValid = false;
        }
    }
    return allValid;
}

    public boolean validateSingleColumn(int[][] board, List<String> errors, int columnIndex) {
        Set<Integer> seen = new HashSet<>();
        Set<Integer> reportedDuplicates = new HashSet<>();
        boolean flag = true;
        for (int i = 0; i < 9; i++) {
            int num = board[i][columnIndex];
            if (num != 0 && !seen.add(num) && !reportedDuplicates.contains(num)) {
                StringBuilder positions = new StringBuilder();
                for (int row = 0; row < 9; row++) {
                    if (board[row][columnIndex] == num) {
                        positions.append(row + 1).append(" ");
                    }
                }
                errors.add("COL " + (columnIndex + 1) + ", #" + num + ", [" + positions.toString().trim() + "]");
                reportedDuplicates.add(num);
                flag = false;
            }
        }
        return flag;
    }
}
