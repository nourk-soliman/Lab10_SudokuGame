/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package system;

/**
 *
 * @author Nour
 */
public class GameCatalog {
    
    private boolean current;
    private boolean allModesExist;


    
    public GameCatalog(boolean current,boolean allModesExist){
        this.current=current;
        this.allModesExist=allModesExist;
    }//this is for the viewer

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current=current;
    
    }

    public boolean isAllModesExist() {
        return allModesExist;
    }

    public void setAllModesExist(boolean allModesExist) {
        this.allModesExist=allModesExist;

}
 
 
    
}

