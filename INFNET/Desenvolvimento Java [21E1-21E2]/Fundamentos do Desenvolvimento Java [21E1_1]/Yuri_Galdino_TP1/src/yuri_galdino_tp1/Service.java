/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yuri_galdino_tp1;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.ArrayList;
import static yuri_galdino_tp1.Menu.choiceApproved;
import static yuri_galdino_tp1.Menu.redoMenuChoice;
import yuri_galdino_tp1.Yuri_Galdino_TP1;

/**
 *
 * @author Yuri Galdino
 */
public class Service {
    //Dados

//    static String[][] nameStudent = new String[100][1];
//    static double[][] gradeStudentAV1 = new double[100][1];
//    static double[][] gradeStudentAV2 = new double[100][1];
//    static int capacity = 100;
    
    static String[][] nameStudent = new String[2][1];
    static double[][] gradeStudentAV1 = new double[2][1];
    static double[][] gradeStudentAV2 = new double[2][1];
    static int capacity = 2;

    public static void studentGradeRegister() {
        int slot = 0;

        if (capacity == 0) {
            System.out.println("Infelizmente todos os espaços foram preenchidos...\nÉ impossível realizar o registro.");
            Yuri_Galdino_TP1.main(new String[0]);
        }

        System.out.println("\n~REGISTRO DE NOTAS DE UM NOVO ALUNO~");

        System.out.println("Status de espaço: " + capacity + " espaço(s) restante(s)\n");

        Scanner input = new Scanner(System.in);

        //Try/Catch to check if the input is a String (NOT ACTUALLY DONE YET, EVERYTHING IS PASSING THROUGH)
        System.out.println("Nome ↓");;
        String inputString = input.nextLine();

        String regex = "^[a-zA-Z]+$";
        if (inputString.matches(regex)) {
            for (int i = 0; i < nameStudent.length; i++) {
                if (nameStudent[i][0] == null) {
                    nameStudent[i][0] = inputString;
                    slot = i;
                    break;
                }
            }
        } else {
            System.out.println("ERRO: Esta entrada só aceita itens do tipo String.");
            studentGradeRegister();
        }

        //Try/Catch to check if the input is an float
        try {
            System.out.println("Nota da AV1 ↓");
            float inputAV1Float = input.nextFloat();
            //Put name in matrix
            for (int i = 0; i < gradeStudentAV1.length; i++) {
                if (gradeStudentAV1[i][0] == 0.0) {
                    gradeStudentAV1[i][0] = inputAV1Float;
                    break;
                }
            }

            System.out.println("Nota da AV2 ↓");
            float inputAV2Float = input.nextFloat();
            //Put name in matrix
            for (int i = 0; i < gradeStudentAV2.length; i++) {
                if (gradeStudentAV2[i][0] == 0.0) {
                    gradeStudentAV2[i][0] = inputAV2Float;
                    //Cálculo simples de espaços
//                    for (int i = 0; i < nameStudent.length; i++) {
//                        if (nameStudent[i][0] != null) {
//                            capacity -= 1;
//                        }
//                    }
                    capacity -= 1;
                    break;
                }
            }

            //Catch error when its not a String
        } catch (InputMismatchException err) {
            System.out.println("ERRO: Esta entrada só aceita itens do tipo float.");
            studentGradeRegister();
        }
        System.out.println("O registro de " + nameStudent[slot][0] + " foi feito com o código " + slot + ".");

        Yuri_Galdino_TP1.main(new String[0]);
    }

    public static void studentCheckReport() {
        System.out.println("Consultar boletim aluno");
        System.exit(0);
    }
;
}
