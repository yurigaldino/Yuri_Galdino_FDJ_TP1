/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package yuri_galdino_tp1;

import java.util.InputMismatchException;
import java.util.Scanner;
import yuri_galdino_tp1.Service;
import yuri_galdino_tp1.Yuri_Galdino_TP1;

/**
 *
 * @author Yuri Galdino
 */
public class Menu {

    public static int menu() {
        System.out.println("[1] Registrar as notas de um novo aluno.\n"
                + "[2] Consultar boletim de um aluno.\n"
                + "[3] Consultar notas da turma.\n"
                + "[4] Sair.");
        System.out.println("Digite escolha abaixo ↓");

        //Output variable
        int choiceApproved = 0;

        try ( Scanner input = new Scanner(System.in)) {
            //Try/Catch to check if the input is int
            try {
                int choice = input.nextInt();

                if (choice > 4 || choice < 1) {
                    redoMenuChoice();

                } else {
                    //Pass checked value to output variable "choiceApproved"
                    choiceApproved = choice;
                }

            } catch (InputMismatchException err) {
                redoMenuChoice();
            }
            input.close();
        }

        return choiceApproved;
    }

    public static void redoMenuChoice() {
        System.out.println("\nPor favor, escolha uma das opções permitidas...\n");

        //Thread.sleep for 2.4 seconds
        try {
            Thread.sleep(2400);
        } catch (InterruptedException e) {
            // this part is executed when an exception (in this example InterruptedException) occurs
        }
        //Force user to choose again.
        menu();
        //Yuri_Galdino_TP1.main(new String[0]);
    }

    public static void handleMenuChoice(int choice) {
        //Switch/Case way
//        switch (choice) {
//      case 1:
//        Service.studentGradeRegister();
//        break;
//      case 2:
//        //Service.studentCheckReport();
//        break;
//      case 3:
//        //Service.classCheckGrades();
//        break;
//      case 4:
//        System.out.println("\nObrigado por utilizar nosso sistema.\n");
//        break;
//    }
          //If/Else way
          System.out.println("choice = "+choice);
        if (choice == 1) {
            Service.studentGradeRegister();
        } else if (choice == 2) {
            //studentCheckReport();
        } else if (choice == 3) {
            //classCheckGrades();
        } else if (choice == 4) {
            System.out.println("\nObrigado por utilizar nosso sistema.\n");
        } else {
            System.out.println("\nPor favor, escolha novamente\n");
            menu();
        }
    }
}