/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores_lab2;

import java.util.ArrayList;

/**
 *
 * @author Milton Casanova
 */
public class primero {
    public String noTerminal;
    public ArrayList<String> primeros = new ArrayList<>();
    
    public primero(String noTerminal) {
        this.noTerminal = noTerminal;
    }
}
