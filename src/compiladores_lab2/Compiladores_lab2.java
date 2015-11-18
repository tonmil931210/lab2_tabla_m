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
import java.util.Scanner;
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
    public static ArrayList<String> pila = new ArrayList<>();
    public static ArrayList<String> entrada = new ArrayList<>();
    public static ArrayList<String> salida = new ArrayList<>();
    public static String[][] tablaM;

    /**
     * @param args the command line arguments
     */
    public static void fileChooser() throws IOException {
        Scanner in = new Scanner(System.in);
        JFileChooser fileIn = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT File", "txt");
        fileIn.setFileFilter(filter);
        int returnVal = fileIn.showOpenDialog(fileIn);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File fileGrammar = fileIn.getSelectedFile();
            productions(fileGrammar);
            hasEpsilon();
            primeros();
            System.out.println("siguientes");
            siguientes();
            System.out.println("TABLA M");
            tablaM();
            mostrarTablaM();
            System.out.println("INGRESE CADENA A RECONOCER");
            String cadena = in.next();
            comprobacion(cadena);
        } else {
            System.out.println("No escogio ningun archivo");
        }
    }

    public static void mostrarTablaM() {
        System.out.print(" ---  ");
        for (int i = 0; i < t.size(); i++) {
            System.out.print(t.get(i) + " --- ");
        }
        System.out.println("");
        for (int i = 0; i < nt.size(); i++) {
            System.out.print(nt.get(i) + " --- ");
            for (int j = 0; j < t.size(); j++) {
                if (tablaM[i][j] == null) {
                    System.out.print("vacio");
                } else {
                    System.out.print(tablaM[i][j]);
                }
                if (!(j == t.size() - 1)) {
                    System.out.print(" --- ");
                }
            }
            System.out.println("");
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
            }
            String noTerminal = produccion[0];
            if (!nt.contains(noTerminal)) {
                nt.add(noTerminal);
            }
            String epsilon = produccion[1];
            if (!nte.contains(noTerminal)) {
                if (epsilon.equals("&")) {
                    nte.add(noTerminal);
                }
            }

        }
        for (int i = 0; i < p.size(); i++) {
            String[] produccion = p.get(i).replaceAll("\\s", "").split("->");
            String terminales = produccion[1];
            for (int j = 0; j < terminales.length(); j++) {
                String caracter = terminales.substring(j, j + 1);
                if (!t.contains(caracter) && !nt.contains(caracter) && !caracter.equals("&")) {
                    t.add(caracter);
                }
            }
        }
    }

    public static void hasEpsilon() {
        ArrayList<String> temp_nte = new ArrayList<>();
        for (int i = 0; i < nt.size(); i++) {
            if (!nte.contains(nt.get(i))) {
                boolean temp = false;
                for (int j = 0; j < p.size(); j++) {
                    String[] produccion = p.get(j).replaceAll("\\s", "").split("->");
                    if (nt.get(i).equals(produccion[0])) {
                        for (int k = 0; k < produccion[1].length(); k++) {
                            String caracter = produccion[1].substring(k, k + 1);
                            if (t.contains(caracter)) {
                                temp = true;
                                break;
                            }
                        }
                        if (temp) {
                            break;
                        }
                    }
                }
                if (!temp) {
                    temp_nte.add(nt.get(i));
                }
            }
        }
        for (int i = 0; i < temp_nte.size(); i++) {
            if (!nte.contains(temp_nte.get(i))) {
                int temp = 0;
                for (int j = 0; j < p.size(); j++) {
                    String[] produccion = p.get(j).replaceAll("\\s", "").split("->");
                    if (temp_nte.get(i).equals(produccion[0])) {
                        for (int k = 0; k < produccion[1].length(); k++) {
                            String caracter = produccion[1].substring(k, k + 1);
                            if (!nte.contains(caracter)) {
                                temp = 1;
                                if (temp_nte.contains(caracter)) {
                                    temp = 2;
                                }
                                break;
                            }
                        }
                        if (temp == 1 || temp == 2) {
                            break;
                        }
                    }
                }
                if (temp == 0) {
                    nte.add(temp_nte.get(i));
                }
                if (temp == 0 || temp == 1) {
                    temp_nte.remove(temp_nte.get(i));
                    i = -1;
                }
            }
        }
    }

    public static void primeros() {
        for (int i = 0; i < nt.size(); i++) {
            primeros.add(new primero(nt.get(i)));
            for (int j = 0; j < p.size(); j++) {
                String[] produccion = p.get(j).replaceAll("\\s", "").split("->");
                if (nt.get(i).equals(produccion[0])) {
                    for (int k = 0; k < produccion[1].length(); k++) {
                        if ((t.contains(produccion[1].substring(k, k + 1)) || nt.contains(produccion[1].substring(k, k + 1))) && !nte.contains(produccion[1].substring(k, k + 1))) {
                            primeros.get(i).primeros.add(produccion[1].substring(k, k + 1));
                            break;
                        } else {
                            primeros.get(i).primeros.add(produccion[1].substring(k, k + 1));
                        }
                    }
                }
            }
        }
        System.out.println("Primeros");
        for (int i = 0; i < primeros.size(); i++) {
            primero p = primeros.get(i);
            System.out.println(p.noTerminal);
            p.primeros.remove(p.noTerminal);
            for (int j = 0; j < p.primeros.size(); j++) {
                if (nt.contains(p.primeros.get(j))) {
                    String nt = p.primeros.get(j);
                    ArrayList<String> temp = getPrimeros(nt);
                    if (temp.contains("&") && j < p.primeros.size() - 1) {
                        temp.remove("&");
                    }
                    p.primeros.addAll(temp);
                    p.primeros = new ArrayList<String>(new LinkedHashSet<String>(p.primeros));
                    p.primeros.remove(p.primeros.get(j));
                    p.primeros.remove(p.noTerminal);
                    j = -1;
                }
            }
            System.out.println(p.primeros);
        }

    }

    public static void tablaM() {
        t.add("$");
        tablaM = new String[nt.size()][t.size()];
        for (int i = 0; i < p.size(); i++) {
            String[] produccion = p.get(i).replaceAll("\\s", "").split("->");
            if (produccion[1].contains("&")) {
                ArrayList<String> siguienteM = getSiguientes(produccion[0]);
                int row;
                int colunm;
                for (int j = 0; j < siguienteM.size(); j++) {
                    row = nt.indexOf(produccion[0]);
                    colunm = t.indexOf(siguienteM.get(j));
                    tablaM[row][colunm] = p.get(i).replaceAll("\\s", "");
                }
            } else {
                ArrayList<String> primeroM = primeroBeta(produccion[1]);
                int row;
                int colunm;
                for (int j = 0; j < primeroM.size(); j++) {
                    if (!primeroM.get(j).equals("&")) {
                        row = nt.indexOf(produccion[0]);
                        colunm = t.indexOf(primeroM.get(j));
                        tablaM[row][colunm] = p.get(i).replaceAll("\\s", "");
                    }

                }
            }
        }
    }

    public static void initEntrada(String cadena) {
        for (int i = 0; i < cadena.length(); i++) {
            entrada.add(cadena.substring(i, i + 1));
        }
        entrada.add("$");
    }

    public static void comprobacion(String cadena) {
        System.out.println("Pila --- Entrada --- Salida");
        pila.add("$");
        pila.add(nt.get(0));
        initEntrada(cadena);
        String last, first, produccion;
        int row, colunm;
        while (!pila.get(pila.size() - 1).equals("$") && !entrada.get(0).equals("$")) {
            last = pila.get(pila.size() - 1);
            first = entrada.get(0);
            if (nt.contains(last) || last.equals("&")) {
                row = nt.indexOf(last);
                colunm = t.indexOf(first);
                produccion = tablaM[row][colunm];
                salida.add(produccion);
                System.out.println(concaternar(pila) + " --- " + concaternar(entrada) + " --- " + salida.get(salida.size() - 1));
                pila.remove(pila.size() - 1);
                if (!produccion.replaceAll("\\s", "").split("->")[1].equals("&")) {
                    invertir(produccion);
                }
            } else {
                if (last.equals(first)) {
                    salida.add(" ");
                    System.out.println(concaternar(pila) + " --- " + concaternar(entrada) + " --- " + salida.get(salida.size() - 1));
                    pila.remove(pila.size() - 1);
                    entrada.remove(0);
                } else {
                    break;
                }
            }
        }
        if (pila.get(pila.size() - 1).equals("$") && entrada.get(0).equals("$")) {
            System.out.println(concaternar(pila) + " --- " + concaternar(entrada) + " --- " + salida.get(salida.size() - 1));
            System.out.println("Cadena reconocida");
        } else {
            System.out.println("Cadena no reconocida");
        }
    }

    public static String concaternar(ArrayList<String> array) {
        String cadena = "";
        for (int i = 0; i < array.size(); i++) {
            cadena += array.get(i);
        }
        return cadena;
    }

    public static void invertir(String produccion) {
        String cadena = produccion.replaceAll("\\s", "").split("->")[1];
        for (int i = cadena.length(); i > 0; i--) {
            pila.add(cadena.substring(i - 1, i));
        }
    }

    public static ArrayList<String> getPrimeros(String nt) {
        for (int i = 0; i < primeros.size(); i++) {
            if (primeros.get(i).noTerminal.compareTo(nt) == 0) {
                return (ArrayList<String>) primeros.get(i).primeros.clone();
            }
        }
        return null;
    }

    //Siguientes
    public static void siguientes() {
        //inicializar primeros
        for (int i = 0; i < nt.size(); i++) {
            siguientes.add(new siguiente(nt.get(i)));
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
                if (nt.contains(s)) {
                    siguientes.get(i).siguientes.remove(s);
                    siguientes.get(i).siguientes.addAll(getSiguientes(s));
                    siguientes.get(i).siguientes = new ArrayList<String>(new LinkedHashSet<String>(siguientes.get(i).siguientes));
                    j = -1;
                }
            }
        }
        for (siguiente s : siguientes) {
            System.out.println(s.noTerminal);
            System.out.println(s.siguientes);
        }
    }

    public static ArrayList<String> getSiguientes(String nt) {
        for (int i = 0; i < siguientes.size(); i++) {
            if (siguientes.get(i).noTerminal.compareTo(nt) == 0) {
                return (ArrayList<String>) siguientes.get(i).siguientes.clone();
            }
        }
        return null;
    }

    public static int getSiguienteIndex(String nt) {
        for (int i = 0; i < siguientes.size(); i++) {
            if (siguientes.get(i).noTerminal.compareTo(nt) == 0) {
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
                String s = produccion.charAt(j) + "";
                //Es no terminal?
                if (nt.contains(s)) {
                    int k = getSiguienteIndex(s);
                    String beta = produccion.substring(j + 1);
                    if (beta.length() > 0) {
                        ArrayList<String> primeros = primeroBeta(beta);
                        siguientes.get(k).siguientes.addAll(primeros);
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
            String nter = p.get(i).split("->")[0];
            for (int j = 0; j < produccion.length(); j++) {
                String s = produccion.charAt(j) + "";
                //Es no terminal?
                if (nt.contains(s)) {
                    int k = getSiguienteIndex(s);
                    String beta = produccion.substring(j + 1);
                    if (beta.length() > 0) {
                        if (primeroBeta(beta).contains("&")) {
                            siguientes.get(k).siguientes.add(nter);
                        }
                    } else {
                        siguientes.get(k).siguientes.add(nter);
                    }
                }
            }
        }
        for (int i = 0; i < siguientes.size(); i++) {
            //Eliminar repetidos en cada siguiente
            siguientes.get(i).siguientes = new ArrayList<String>(new LinkedHashSet<String>(siguientes.get(i).siguientes));
        }
    }

    public static ArrayList<String> primeroBeta(String beta) {
        ArrayList<String> primeros = new ArrayList<>();
        for (int i = 0; i < beta.length(); i++) {
            String s = beta.charAt(i) + "";
            //es terminal?
            if (t.contains(s)) {
                primeros.add(s);
                break;
            } else if (nte.contains(s)) {
                // es un no terminal que produce epsilon?
                primeros.addAll(getPrimeros(s));
            } else {
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
        Scanner in = new Scanner(System.in);
        String choose = "1";
        while (true) {
            fileChooser();
            System.out.println("Ingrese 1 si desea continuar otro para salir");
            choose = in.next();
            if (choose.equals("1")) {
                p.clear();
                t.clear();
                nt.clear();
                nte.clear();
                primeros.clear();
                siguientes.clear();
                pila.clear();
                entrada.clear();
                salida.clear();
                tablaM = null;
            } else {
                break;
            }
        }

    }

}
