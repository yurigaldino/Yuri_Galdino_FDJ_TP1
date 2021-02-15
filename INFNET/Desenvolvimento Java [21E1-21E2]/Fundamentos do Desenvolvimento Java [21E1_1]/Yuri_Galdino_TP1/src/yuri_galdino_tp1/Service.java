/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yuri_galdino_tp1;

import java.util.InputMismatchException;
import java.util.Scanner;
import static yuri_galdino_tp1.Menu.choiceApproved;
import static yuri_galdino_tp1.Menu.redoMenuChoice;
import yuri_galdino_tp1.Yuri_Galdino_TP1;

/**
 *
 * @author Yuri Galdino
 */
public class Service {

    public static void studentGradeRegister() {
        //Dados
        //static int choiceApproved;

        String[][] nameStudent = new String[100][1];
        double[][] gradeStudentAV1 = new double[100][1];
        double[][] gradeStudentAV2 = new double[100][1];

        System.out.println("\nREGISTRO DE NOTAS DE UM NOVO ALUNO\n");

        System.out.println("Nome ↓");
        try ( Scanner inputName = new Scanner(System.in);) {

            //Try/Catch to check if the input is a String
            try {
                String nameString = inputName.nextLine();
                //Put name in matrix
                for (int i = 0; i < nameStudent.length; i++) {;;
                    if (nameStudent[i][0] == "null") {
                        nameStudent[i][0] = nameString;
                    }
                }

                //Catch error when its not a String
            } catch (InputMismatchException err) {
                System.out.println("ERRO: Esta entrada só aceita itens do tipo String.");
                studentGradeRegister();
            }
        }

//        System.out.println("Nota da AV1 ↓");
//        Scanner inputAV1 = new Scanner(System.in);
//        System.out.println("Nota da AV2 ↓");
//        Scanner inputAV2 = new Scanner(System.in);
//        System.out.println("\n");
    }
}
