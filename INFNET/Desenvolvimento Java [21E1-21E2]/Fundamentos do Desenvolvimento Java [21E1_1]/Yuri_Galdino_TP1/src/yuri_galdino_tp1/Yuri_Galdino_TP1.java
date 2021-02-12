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

/**
 *
 * @author Yuri Galdino
 */
public class Yuri_Galdino_TP1 {

    //Funções auxiliares
    public static String typeOf(Object o) {
        return o.getClass().getSimpleName();
    }

    //Funções do programa
    public static void main(String[] args) {

        //Programa
        Menu.handleMenuChoice(Menu.menu());
    }

}
