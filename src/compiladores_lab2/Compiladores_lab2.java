/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiladores_lab2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Milton
 */
public class Compiladores_lab2 {

    public static ArrayList<String> p = new ArrayList<>();
    public static ArrayList<String> t = new ArrayList<>();
    public static ArrayList<String> nt = new ArrayList<>();
    public static ArrayList<String> nte = new ArrayList<>();
    public static ArrayList<primero> primeros = new ArrayList<>();

    /**
     * @param args the command line arguments
     */
    public static void fileChooser() throws IOException {
        JFileChooser fileIn = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT File", "txt");
        fileIn.setFileFilter(filter);
        int returnVal = fileIn.showOpenDialog(fileIn);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fileGrammar = fileIn.getSelectedFile();
            productions(fileGrammar);
            primeros();
        } else {
            System.out.println("No escogio ningun archivo");
        }
    }

    public static void productions(File data) throws FileNotFoundException, IOException {
        BufferedReader br = null;
        String line;
        br = new BufferedReader(new FileReader(data));
        while ((line = br.readLine()) != null) {
            String[] produccion = line.replaceAll("\\s", "").split("->");
            if (!line.isEmpty()) {
                p.add(line.replaceAll("\\s", ""));
                System.out.println("producciones");
                System.out.println(p);
            }
            String noTerminal = produccion[0];
            if (!nt.contains(noTerminal)) {
                nt.add(noTerminal);
                System.out.println("No Terminales");
                System.out.println(nt);
            }
            String epsilon = produccion[1];
            if (nte.contains(noTerminal)){
                if (epsilon.equals("&")){
                    nte.add(noTerminal);
                }
            }

        }
        for (int i = 0; i < p.size(); i++) {
            String terminales = p.get(i).replaceAll("\\s", "").split("->")[1];
            for (int j = 0; j < terminales.length(); j++) {
                String caracter = terminales.substring(j, j + 1);
                if (!t.contains(caracter) && !nt.contains(caracter)) {
                    t.add(caracter);
                    System.out.println("Terminales");
                    System.out.println(t);
                }
            }
        }

    }
    
    public static void hasEpsilon(){
        for (int i = 0; i < p.size(); i++) {
            String terminales = p.get(i).replaceAll("\\s", "").split("->")[1];
            for (int j = 0; j < terminales.length(); j++) {
                String caracter = terminales.substring(j, j + 1);
                if (!t.contains(caracter) && !nt.contains(caracter)) {
                    t.add(caracter);
                    System.out.println("Terminales");
                    System.out.println(t);
                }
            }
        }
    }

    public static void primeros() {
        for (int i = 0; i < nt.size(); i++) {
            primeros.add(new primero(nt.get(i)));
            System.out.println(primeros.get(i).noTerminal);
            for (int j = 0; j < p.size(); j++) {
                String[] produccion = p.get(j).replaceAll("\\s", "").split("->");
                if(nt.get(i).equals(produccion[0])){
                    System.out.println("entro");
                    primeros.get(i).primeros.add(produccion[1].substring(0, 1));
                    System.out.println(primeros.get(i).primeros);
                }
            }
        }
        
    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        fileChooser();
    }

}
