
package controller.csv;
import java.io.*;
import java.util.*;

public class CSVGameHandler {

    // Save a board to CSV
    public static void writeCSV(String filePath, int[][] board) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (int[] row : board) {
                String line = Arrays.stream(row)
                                    .mapToObj(String::valueOf)
                                    .reduce((a, b) -> a + "," + b)
                                    .orElse("");
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update an existing CSV with a new board
    public static void updateCSV(String filePath, int[][] board) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist: " + filePath);
            return;
        }
        // Simply overwrite the CSV
        writeCSV(filePath, board);
    }

    // Delete a CSV file
    public static void deleteCSV(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.delete()) {
            System.out.println("Deleted file: " + filePath);
        } else {
            System.out.println("Failed to delete file: " + filePath);
        }
    }

    // Read a CSV and return the board as int[][]
    public static int[][] readCSV(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist: " + filePath);
            return null;
        }

        List<int[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int[] row = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    row[i] = Integer.parseInt(parts[i].trim());
                }
                rows.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rows.toArray(new int[0][0]);
    }
}
