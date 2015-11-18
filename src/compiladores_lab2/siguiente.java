/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores_lab2;

import java.util.ArrayList;

/**
 *
 * @author Andoresu
 */
public class siguiente {
     public String noTerminal;
     public ArrayList<String> siguientes = new ArrayList<>();

    public siguiente(String noTerminal) {
        this.noTerminal = noTerminal;
    }
}
