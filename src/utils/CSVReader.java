package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {

    public static int[][] readCSV(String filename) throws IOException {
        int[][] board = new int[9][9];
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        int row = 0;

        while ((line = reader.readLine()) != null && row < 9) {
            String[] values = line.split(",");
            if (values.length != 9) {
                reader.close();
                throw new IllegalArgumentException("Invalid number of columns in row " + (row + 1));
            }
            for (int col = 0; col < 9; col++) {
                try {
                    board[row][col] = Integer.parseInt(values[col].trim());
                } catch (NumberFormatException e) {
                    reader.close();
                    throw new IllegalArgumentException("Invalid number format in row " + (row + 1));
                }
            }
            row++;
        }

        reader.close();

        if (row != 9) {
            throw new IllegalArgumentException("Invalid number of rows");
        }

        return board;
    }
}