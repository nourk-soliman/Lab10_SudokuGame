package controller.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LogFileHandler {
    private final String filename = System.getProperty("user.dir") + "/current game/incomplete.log";
    
    public String readAndRemoveLastMove() {
        ArrayList<String> lines = new ArrayList<>();
        
        // Use try-with-resources to automatically close the reader
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("File not found.");
            return null;
        }

        if (lines.isEmpty()) {
            return null;
        }

        String lastLine = lines.get(lines.size() - 1);
        lines.remove(lines.size() - 1);
        
        // Use try-with-resources to automatically close the writer
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Could not write to file.");
        }
         
        return lastLine;
    }
    
    public void writeToFile(String userAction) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(userAction);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error in writing file");
        }
    }
    
    public void deleteLog() {
        File file = new File(filename);
        if (file.exists() && file.delete()) {
            System.out.println("Deleted file: " + filename);
        } else {
            System.out.println("Failed to delete file: " + filename);
        }
    }
}