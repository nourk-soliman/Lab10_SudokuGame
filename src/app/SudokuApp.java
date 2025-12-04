/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;
import board.SudokuBoard;
import factory.ValidatorFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.ValidationResult;
import modes.ModeValidator;
/**
 *
 * @author Nour
 */
public class SudokuApp {
    
    public static void main(String[] args) throws Exception {
        if(args.length!=2)
        {System.out.println("Invalid command entered, Valid format: java-jar<app-name>.jar<csv filepath> <mode>");}
        else if(!args[1].trim().equals("0") && !args[1].trim().equals("3") && !args[1].trim().equals("27"))
        {System.out.println("Invalid mode entered, choose a valid mode: 0,3,27.");}
        else{String filename=args[0];
     
        int mode=Integer.parseInt(args[1]);
        ValidatorFactory validatorFactory=new ValidatorFactory();
            ModeValidator modeValidator = validatorFactory.getType(mode);
            SudokuBoard board=new SudokuBoard();
            board.importBoardFromFile(filename);
            List<String>errors=Collections.synchronizedList(new ArrayList<>());
            boolean isValid=modeValidator.validate(board.getBoard(), errors);
            ValidationResult result=new ValidationResult(isValid,errors);
            result.printResult();
       
        
     
     
    }
    
}
}
