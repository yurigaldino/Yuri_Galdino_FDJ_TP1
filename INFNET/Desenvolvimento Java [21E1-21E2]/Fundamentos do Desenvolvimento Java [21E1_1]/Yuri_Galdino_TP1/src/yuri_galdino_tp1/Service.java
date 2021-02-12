/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yuri_galdino_tp1;

import java.util.Scanner;
import yuri_galdino_tp1.Yuri_Galdino_TP1;

/**
 *
 * @author Yuri Galdino
 */
public class Service {

    public static void studentGradeRegister() {
        //Dados
        String[][] nameStudent = new String[100][1];
        double[][] gradeStudentAV1 = new double[100][1];
        double[][] gradeStudentAV2 = new double[100][1];

        System.out.println("\nREGISTRO DE NOTAS DE UM NOVO ALUNO\n");
        
        System.out.println("Nome ↓");
        Scanner inputName = new Scanner(System.in);
//        System.out.println("Nota da AV1 ↓");
//        Scanner inputAV1 = new Scanner(System.in);
//        System.out.println("Nota da AV2 ↓");
//        Scanner inputAV2 = new Scanner(System.in);
//        System.out.println("\n");

        for (int i = 0; i < nameStudent.length; i++) {
            if(nameStudent[i][0] == "null") {
                System.out.println(nameStudent[i][0] );
            }
        }
    }
}
