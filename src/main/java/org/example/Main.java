package org.example;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

import static org.example.StudentData.deleteStudent;


public class Main {
    static HighSchoolStudent schoolStudent = new HighSchoolStudent();
    public static void main(String[] args) throws SQLException {

        Database.initDB();

        Scanner input = new Scanner(System.in);

        int select = -1;

        do {
            System.out.println("\n--- Students Management (Grade 10) ---");
            System.out.println("1. Display All Students");
            System.out.println("2. Add Student");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Average Score");
            System.out.println("6. Search Student by Name");
            System.out.println("0. Exit");
            System.out.print("Select a number: ");

            String userInput = input.nextLine();

            if (!userInput.matches("\\d+")) {
                System.out.println("Please enter a valid number (positive only).");
                continue;
            }

            select = Integer.parseInt(userInput);

            switch (select) {
                //case 1 -> displayAllStudents();
               // case 2 -> addStudent(input);
               // case 3 -> updateStudent(input);
                case 4 -> deleteStudent(input);
               // case 5 -> calculateAverage(input);
                case 6 -> {
                    System.out.print("Enter name to search: ");
                    String searchName = input.nextLine();
                    Student searchStudent = new Student(0, searchName, 0);
                    List<Student> results = schoolStudent.searchStudent(searchStudent) ;

                    if (results.isEmpty()) {
                        System.out.println("No students found with that name.");
                    } else {
                        System.out.println("Search results:");
                        System.out.println("-----------------------------------");
                        System.out.println("ID     | NAME  |  MARKS  |  GRADE  |");
                        System.out.println("-----------------------------------");
                        for (Student student : results) {
                            System.out.printf("| %-1d    | %-6s   | %-9d | %-3s",
                                    student.getId(), student.getName(), student.getScore(), student.getGrade());
                        }
                    }
                }
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice.");
            }

        } while (select != 0);
    }
}
