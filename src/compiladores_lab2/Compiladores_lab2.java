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
import java.util.LinkedHashSet;
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
    public static ArrayList<siguiente> siguientes = new ArrayList<>();

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
    
    public static ArrayList<String> getPrimeros(String nt) {        
        for (int i = 0; i < primeros.size(); i++) {            
            if(primeros.get(i).noTerminal.compareTo(nt)==0){
                return primeros.get(i).primeros;
            }
        }
        return null;
    }
    
    //Siguientes
    public static void siguientes() {
        //inicializar primeros
        for (int i = 0; i < nt.size(); i++) {            
            if(!siguientes.get(i).siguientes.contains(nt.get(i))){
                siguientes.add(new siguiente(nt.get(i)));
            }
        }
        //Aplicar reglas
        regla1();
        regla2();
        regla3();
        //Reemplazar
        for (int i = 0; i < siguientes.size(); i++) {
            siguientes.get(i).siguientes.remove(siguientes.get(i).noTerminal);
            for (int j = 0; j < siguientes.get(i).siguientes.size(); j++) {
                String s = siguientes.get(i).siguientes.get(j);
                if(nt.contains(s)){
                    siguientes.get(i).siguientes.remove(s);
                    siguientes.get(i).siguientes.addAll(getSiguientes(s));
                    siguientes.get(i).siguientes = new ArrayList<String>(new LinkedHashSet<String>(siguientes.get(i).siguientes));
                }
            }
        }            
    }
    
    public static ArrayList<String> getSiguientes(String nt) {
        for (int i = 0; i < siguientes.size(); i++) {
            if(siguientes.get(i).noTerminal.compareTo(nt)==0){
                return siguientes.get(i).siguientes;
            }
        }
        return null;
    }
    
    public static int getSiguienteIndex(String nt) {
        for (int i = 0; i < siguientes.size(); i++) {
            if(siguientes.get(i).noTerminal.compareTo(nt)==0){
                return i;
            }
        }
        return -1;
    }
    //Regla #1
    public static void regla1() {
        siguientes.get(0).siguientes.add("$");
    }
    //REGLA #2
    public static void regla2() {
        for (int i = 0; i < p.size(); i++) {
            String produccion = p.get(i).split("->")[1];
            for (int j = 0; j < produccion.length(); j++) {
                String s = produccion.charAt(i) + "";
                //Es no terminal?
                if(nt.contains(s)){
                    int k = getSiguienteIndex(s);
                    String beta = produccion.substring(i+1);
                    if(beta.length()>0){
                        siguientes.get(k).siguientes.addAll(primeroBeta(beta));
                        //Eliminar los epsilons
                        siguientes.get(k).siguientes.remove("&");
                    }
                }
            }
        }
        for (int i = 0; i < siguientes.size(); i++) {       
            //Eliminar repetidos en cada siguiente
            siguientes.get(i).siguientes = new ArrayList<String>(new LinkedHashSet<String>(siguientes.get(i).siguientes));            
        }
    }    
    //regla #3
    public static void regla3() {
        for (int i = 0; i < p.size(); i++) {
            String produccion = p.get(i).split("->")[1];
            String nt = p.get(i).split("->")[0];
            for (int j = 0; j < produccion.length(); j++) {
                String s = produccion.charAt(i) + "";
                //Es no terminal?
                if(nt.contains(s)){
                    int k = getSiguienteIndex(s);
                    String beta = produccion.substring(i+1);
                    if(beta.length()>0){
                        if(primeroBeta(beta).contains("&")){
                            siguientes.get(k).siguientes.add(nt);
                        }                        
                    }else{
                        siguientes.get(k).siguientes.add(nt);
                    }
                }
            }
        }
        for (int i = 0; i < siguientes.size(); i++) {       
            //Eliminar repetidos en cada siguiente
            siguientes.get(i).siguientes = new ArrayList<String>(new LinkedHashSet<String>(siguientes.get(i).siguientes));            
        }
    }
    
    public static ArrayList<String> primeroBeta(String beta){
        ArrayList<String> primeros = new ArrayList<>();
        for (int i = 0; i < beta.length(); i++) {            
            String s = beta.charAt(i) + "";
            //es terminal?
            if(t.contains(s)){
                primeros.add(s);
                break;
            }else if(nte.contains(s)){                
                // es un no terminal que produce epsilon?
                primeros.addAll(getPrimeros(s));
            }else {
                // es un no terminal que no produce epsilon
                primeros.addAll(getPrimeros(s));
                break;
            }
        }       
        //Eliminar repetidos
        primeros = new ArrayList<String>(new LinkedHashSet<String>(primeros));        
        return primeros;
    }    
    

    public static void main(String[] args) throws IOException {
        // TODO code application logic here                   
        fileChooser();
    }

}
