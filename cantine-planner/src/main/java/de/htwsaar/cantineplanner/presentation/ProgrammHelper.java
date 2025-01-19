package de.htwsaar.cantineplanner.presentation;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ProgrammHelper {

    private final Scanner scanner = new Scanner(System.in);

    public int promptNumber(String prompt) {
        try {
            if (prompt != "") {
                System.out.println(prompt + ":");
            }
            int number = scanner.nextInt();
            System.out.println("STDIN: " + number);
            return number;
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input:Only Integer!");
            scanner.nextLine();
        }
        return -1;
    }


    public String promptString(String prompt) {
        try {
            if (prompt != "") {
                System.out.println(prompt + ":");
            }
            String string = scanner.nextLine();
            System.out.println("STDIN: " + string);
            return string;
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input:Only String!");
            scanner.nextLine();
        }
        return "";
    }

    public double promptDouble(String prompt) {
        try {
            if (prompt != "") {
                System.out.println(prompt + ":");
            }
            System.out.print("> ");
            double number = scanner.nextDouble();
            System.out.println("STDIN: " + number);
            return number;
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input:Only Double!");
            scanner.nextLine();
        }
        return -1;
    }
}