package org.example;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        StudentDB.initializeDatabase();

        Scanner input = new Scanner(System.in);
        int option;

        do {
            System.out.println("\n--- Grade 10 Student Management ---");
            System.out.println("1. Display All Students");
            System.out.println("2. Add Student");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Average Score");
            System.out.println("6. Search Student");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            try {
                option = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number from 1 to 6. or 0 to Exit");
                option = -1; // Invalid option to handle below
            }

            switch (option) {
                case 1 -> StudService.displayAll();
                case 2 -> StudService.addStudent(input);
                case 3 -> StudService.updateStudent(input);
                case 4 -> StudService.deleteStudent(input);
                case 5 -> StudService.calculateAverage();
                case 6 -> StudService.searchStudent(input);
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Please enter a valid option.");
            }

        } while (option != 0);

        input.close();
    }
}
