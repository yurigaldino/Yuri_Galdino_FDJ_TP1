/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yuri_galdino_tp1;

import java.util.InputMismatchException;
import java.lang.ArrayIndexOutOfBoundsException;
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

    static String[][] nameStudent = new String[100][1];
    static double[][] gradeStudentAV1 = new double[100][1];
    static double[][] gradeStudentAV2 = new double[100][1];
    static int remainingCapacity = 100;

    //Bloco de teste em menor quantidade
//    static String[][] nameStudent = new String[2][1];
//    static double[][] gradeStudentAV1 = new double[2][1];
//    static double[][] gradeStudentAV2 = new double[2][1];
//    static int remainingCapacity = 2;
    static int usedCapacity = 0;

    public static void studentGradeRegister() {
        int slot = 0;

        if (remainingCapacity == 0) {
            System.out.println("Infelizmente todos os espaços foram preenchidos...\nÉ impossível realizar o registro.");
            Yuri_Galdino_TP1.main(new String[0]);
        }

        System.out.println("\n~REGISTRO DE NOTAS DE UM NOVO ALUNO~");

        System.out.println("Status de espaço: " + remainingCapacity + " espaço(s) restante(s)\n");

        Scanner input = new Scanner(System.in);

        //Try/Catch to check if the input is a String (NOT ACTUALLY DONE YET, EVERYTHING IS PASSING THROUGH)
        System.out.println("Nome ↓");;
        String inputString = input.nextLine();

        String regex = "^[A-Za-z, ]++$";
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

            if (inputAV1Float < 0 || inputAV1Float > 10) {
                System.out.println("ERRO: Insira somentes números entre 0 e 10.");
                //Zerar nameStudent
                nameStudent[slot][0] = null;
                studentGradeRegister();
            }

            //Put name in matrix
            for (int i = 0; i < gradeStudentAV1.length; i++) {
                if (nameStudent[i][0] == inputString) {
                    gradeStudentAV1[i][0] = inputAV1Float;
                    break;
                }
            }

            System.out.println("Nota da AV2 ↓");
            float inputAV2Float = input.nextFloat();

            if (inputAV2Float < 0 || inputAV2Float > 10) {
                System.out.println("ERRO: Insira somentes números entre 0 e 10.");
                //Zerar nameStudent
                nameStudent[slot][0] = null;
                studentGradeRegister();
            }

            //Put name in matrix
            for (int i = 0; i < gradeStudentAV2.length; i++) {
                if (nameStudent[i][0] == inputString) {
                    gradeStudentAV2[i][0] = inputAV2Float;
                    //Cálculo simples de espaços
//                    for (int i = 0; i < nameStudent.length; i++) {
//                        if (nameStudent[i][0] != null) {
//                            capacity -= 1;
//                        }
//                    }
                    remainingCapacity -= 1;
                    usedCapacity += 1;
                    break;
                }
            }

            //Catch error when its not a Float
        } catch (InputMismatchException err) {
            System.out.println("ERRO: Esta entrada só aceita itens do tipo float.");
            studentGradeRegister();
        }
        System.out.println("O registro de " + nameStudent[slot][0] + " foi feito com o código " + slot + ".");

        Yuri_Galdino_TP1.main(new String[0]);
    }

    public static void studentCheckReport() {
        System.out.println("\n~CONSULTA DE BOLETIM DE ALUNO~");
        System.out.println("Status de espaço: " + usedCapacity + " espaço(s) ocupado(s)\n");

        Scanner input = new Scanner(System.in);

        try {
            System.out.println("Código do aluno ↓");
            int inputCodeInt = input.nextInt();

            try {
                if (nameStudent[inputCodeInt][0] == null) {
                    System.out.println("ERRO: Este não é um código válido ou conhecido.");
                    Yuri_Galdino_TP1.main(new String[0]);
                }
            } catch (ArrayIndexOutOfBoundsException err) {
                System.out.println("ERRO: Este não é um código válido ou conhecido.");
                Yuri_Galdino_TP1.main(new String[0]);
            }
            double AV1 = gradeStudentAV1[inputCodeInt][0];
            double AV2 = gradeStudentAV2[inputCodeInt][0];

            double media = (AV1 + AV2) / 2.0;
            //Put name in matrix
            System.out.println("Nome do aluno: " + nameStudent[inputCodeInt][0]);
            System.out.println("Nota da AV1: " + Yuri_Galdino_TP1.round1(AV1));
            System.out.println("Nota da AV2: " + Yuri_Galdino_TP1.round1(AV2));
            System.out.println("Média final: " + Yuri_Galdino_TP1.doubleWith1Decimals(media));

            if (media >= 7) {
                System.out.println("Situação: Aprovado(a)");
            } else if (media < 7 && media >= 4) {
                System.out.println("Situação: Prova final");
            } else {
                System.out.println("Situação: Reprovado(a)");
            }

            //Catch error when its not a Int.
        } catch (InputMismatchException err) {
            System.out.println("ERRO: Esta entrada só aceita itens do tipo inteiro.");
            studentCheckReport();
        }

        Yuri_Galdino_TP1.main(new String[0]);

//System.exit(0);
    }

    public static void classCheckGrades() {
        System.out.println("\n~CONSULTA DE NOTAS DA TURMA~\n");

        if (nameStudent[0][0] == null) {
            System.out.println("Nada ainda para exibir... Experimente registrar as notas de um novo aluno!");
            Yuri_Galdino_TP1.main(new String[0]);
        }

        double averageGradeOfClass = 0.0;

        for (int i = 0; i < gradeStudentAV1.length; i++) {
            if (nameStudent[i][0] != null) {

                double AV1 = gradeStudentAV1[i][0];
                double AV2 = gradeStudentAV2[i][0];

                double media = (AV1 + AV2) / 2.0;
                averageGradeOfClass += media;

                //Put name in matrix
                System.out.println("Nome do aluno: " + nameStudent[i][0]);
                System.out.println("Nota da AV1: " + Yuri_Galdino_TP1.round1(AV1));
                System.out.println("Nota da AV2: " + Yuri_Galdino_TP1.round1(AV2));
                System.out.println("Média final: " + Yuri_Galdino_TP1.doubleWith1Decimals(media));

                if (media >= 7) {
                    System.out.println("Situação: Aprovado(a)");
                } else if (media < 7 && media >= 4) {
                    System.out.println("Situação: Prova final");
                } else {
                    System.out.println("Situação: Reprovado(a)");
                }
                System.out.println("");
                //break;
            } else {
                System.out.println("A média atual da turma é " + Yuri_Galdino_TP1.round1(averageGradeOfClass / usedCapacity) + " com " + usedCapacity + " aluno(s). ");
                Yuri_Galdino_TP1.main(new String[0]);
            }

        }

//        System.out.println("Feature ainda não implementada.");
//        System.exit(0);
    }
;
}
