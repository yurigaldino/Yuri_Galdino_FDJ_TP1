/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yuri_galdino_tp1;

import java.util.Scanner;
import java.lang.Object;
import java.util.InputMismatchException;
import yuri_galdino_tp1.Menu;
import static yuri_galdino_tp1.Menu.menu;

/**
 *
 * @author Yuri Galdino
 */
public class Yuri_Galdino_TP1 {

    static final Scanner scan = new Scanner(System.in);

    //Funções auxiliares
    public static String typeOf(Object o) {
        return o.getClass().getSimpleName();
    }

    public static double round1(double value) {
        return Math.round(value * 10d) / 10d;

    }

    public static double round2(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
    
//    public static double round3(double value, int places) {
//        return DoubleRounder.round(value, places);
//    }


    public static double doubleWith1Decimals(double d) {
        return Math.floor(d * 1e1) / 1e1;
    }
//    public static String readString(String message) {
//        String result = "";
//
//        Scanner scan = Yuri_Galdino_TP1.scan;
//        System.out.println(message);
//        //Checagem para saber se existe linha para ser lida -> scan.hasNextLine(), pois se eu não checar, dará erro
//        if (scan.hasNextLine() == false) {
//            System.out.println("ERRO: Não existe linha para ser lida.");
//        } else {
//            result = scan.nextLine();
//            System.out.println("String capturada: " + result);
//        }
//        return result;
//    }
//    public static float readFloat(String message) {
//        Scanner scan = Yuri_Galdino_TP1.scan;
//        System.out.println(message);
//        return scan.nextFloat();
//    }
    //Funções do programa

    public static void main(String[] args) {

        //Programa
        //Primeira tentativa dividindo em classes
        //Menu.handleMenuChoice(Menu.menu());
        int choice = Menu.menu();

        //If/Else way
        if (choice == 1) {
            Service.studentGradeRegister();
        } else if (choice == 2) {
            Service.studentCheckReport();
        } else if (choice == 3) {
            Service.classCheckGrades();
        } else if (choice == 4) {
            System.out.println("\nObrigado por utilizar nosso sistema.\n");
            System.exit(0);
        }

        //Teste de função
//        String nome = readString("Nome ↓");
    }

}
