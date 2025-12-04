/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package factory;

import modes.*;

/**
 *
 * @author Nour
 */
public class ValidatorFactory {

    public ModeValidator getType(int mode) {
        switch (mode) {
            case 0 -> {
                return new Mode0Validator();
            }
            case 3 -> {
                return new Mode3Validator();
            }
            case 27 -> {
                return new Mode27Validator();
            }
            default -> {
                System.out.println("Invalid mode entered.");
                return null;
            }
        }

    }
}
