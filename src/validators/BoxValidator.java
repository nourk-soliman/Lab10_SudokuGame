package validators;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mariam
 */
public class BoxValidator implements SudokuValidator {

    private final Integer boxRow;
    private final Integer boxCol;

    public BoxValidator() {
        this.boxRow = null;
        this.boxCol = null;
    }

    public BoxValidator(int boxRow, int boxCol) {
        this.boxRow = boxRow;
        this.boxCol = boxCol;
    }

    @Override
    public boolean validate(int[][] board, List<String> errors) {
        if (boxRow != null && boxCol != null) {
            // Validate single box
            return validateSingleBox(board, errors, boxRow, boxCol);
        } else {
            // Validate all boxes
            return validateAllBoxes(board, errors);
        }
    }

    public boolean validateAllBoxes(int[][] board, List<String> errors) {
        boolean allValid = true;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (!validateSingleBox(board, errors, row, col)) {
                    allValid = false; 
                }
            }
        }
        return allValid;
    }

    public boolean validateSingleBox(int[][] board, List<String> errors, int boxRow, int boxCol) {
        Set<Integer> seen = new HashSet<>();
        Set<Integer> reportedDuplicates = new HashSet<>();
        int startRow = boxRow * 3;
        int startCol = boxCol * 3;
        int boxNumber = boxRow * 3 + boxCol + 1;

        boolean flag = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int num = board[startRow + i][startCol + j];
                if (num != 0 && !seen.add(num) && !reportedDuplicates.contains(num)) {
                    StringBuilder positions = new StringBuilder();
                    for (int x = 0; x < 3; x++) {
                        for (int y = 0; y < 3; y++) {
                            if (board[startRow + x][startCol + y] == num) {
                                positions.append(x * 3 + y + 1).append(" ");
                            }
                        }
                    }
                    errors.add("BOX " + boxNumber + ", #" + num + ", [" + positions.toString().trim() + "]");
                    reportedDuplicates.add(num);
                    flag = false;
                }
            }
        }
        return flag;
    }
}
