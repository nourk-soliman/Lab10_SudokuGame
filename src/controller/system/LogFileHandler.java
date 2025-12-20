/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Nour
 */
public class LogFileHandler {
    private final String filename="N:\\Term 5\\Programming 2\\SudokuGame\\current game\\incomplete.log";
    
    public String readAndRemoveLastMove()  {
        ArrayList<String> lines = new ArrayList<>();
        try{BufferedReader br = new BufferedReader(new FileReader(filename)); 
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        catch(IOException e)
        {System.out.println("File not found.");}

        if (lines.isEmpty()) {
            return null; // nothing to undo
        }

        String lastLine = lines.get(lines.size() - 1);
        lines.remove(lines.size()-1);
         try {BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
         }
         catch(IOException e)
         {System.out.println("Could not write to file.");}
         
        return lastLine;
    }
    
    public void writeToFile(String userAction){
        try{
        BufferedWriter writer=new BufferedWriter(new FileWriter(filename,true));
        writer.write(userAction);
        writer.newLine();
        writer.close();
        }
        catch(IOException e){
            System.out.println("Error in writing file");
        }
        
    }
    
        public  void deleteLog() {
        File file = new File(filename);
        if (file.exists() && file.delete()) {
            System.out.println("Deleted file: " + filename);
        } else {
            System.out.println("Failed to delete file: " + filename);
        }
    }
    
}
