package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



// Abstract

public class Main {
static int idCount = 1;
static  StudentMainExtend studentMainExtend = new StudentMainExtend();
static  Scanner input = new Scanner(System.in);
    public static void main(String[] args) throws SQLException {

       Database.initDB();
//        ArrayList<Student> students = new ArrayList<>();





        int select;
        do {
            System.out.println("\n--- Grade 10 Students ---");
            System.out.println("1. Display All Students");
            System.out.println("2. Add Student");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Average Score");
            System.out.println("6. Search Student by Name");
            System.out.println("0. Exit");
            System.out.print("Select a number: ");
            select = input.nextInt();

            switch (select) {
                case 1 ->{

                    List<Student> studentList = studentMainExtend.displayAllStudent();

                    for (Student s: studentList){
                        System.out.println("name "+s.getName()  +" grade " +s.getGrade() );
                    }
                }
//                case 2 -> studentMainExtend.addNewStudent(newStudents);
                case 2 -> {
//
//                      StudentMainExtend studentMainExtend1 = new StudentMainExtend();
//
                    System.out.println("Add student name: ");
                    String sName = input.next();
                    System.out.println("Add student marks: ");
                    int sMarks = input.nextInt();

                    Student newStudents = new Student(idCount, sName, sMarks);
                    studentMainExtend.addNewStudent(newStudents);
                }

                case 3 -> updateStudent(input);
//                case 4 -> deleteStudent(students, input);
//                case 5 -> calculateAverage(students);

                case 6 -> {
                    System.out.print("Enter name to search: ");
                    String searchName = input.next();
                    Student searchStudent = new Student(0, searchName, 0); // Only name is used
                    List<Student> results = studentMainExtend.searchStudent(searchStudent);

                    if (results.isEmpty()) {
                        System.out.println("No students found with that name.");
                    } else {

                        System.out.println("Search results:");
                        System.out.println("-----------------------------------");
                        System.out.println("ID     | NAME  |  MARKS  |  GRADE  |");
                        System.out.println("-----------------------------------");
                        for (Student student : results) {
                            System.out.printf("| %-1d    | %-6s   ", student.getId(), student.getName());
                        }
                    }
                }
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice.");
            }

        } while (select != 0);
    }

    static String formatter( String name){
        name = name.substring(0, 1).toUpperCase().trim() + name.substring(1).toLowerCase().trim()
        ;
        return name;

    }
    static void updateStudent(Scanner input) throws SQLException {
        List<Student> students = studentMainExtend.displayAllStudent();

        if (students.isEmpty()) {
            System.out.println("No students available to update.");
            return;
        }
studentMainExtend.displayAllStudent();

        System.out.println("\n\t--- Update Student ---");

        Student studentToUpdate = null;
        int studentId;


        while (studentToUpdate == null) {
            System.out.print("Enter student ID:");
            String idInput = input.nextLine();

            try {
                studentId = Integer.parseInt(idInput);

                for (Student s : students) {
                    if (s.getId() == studentId) {
                        studentToUpdate = s;
                        break;
                    }
                }

                if (studentToUpdate == null) {
                    System.out.println("No student found with ID: " + studentId);
                    System.out.print("Do you want to try again? (y/n): ");
                    String retry = input.nextLine().trim().toLowerCase();
                    if (!retry.equals("y")) {
                        System.out.println("Returning to main menu.");
                        return;
                    }
                }

            } catch (NumberFormatException e) {
//
            }
        }


        System.out.println("----------------------------------------------");
        System.out.println("| ID  | NAME           | GRADE | SCORE/MARKS |");
        System.out.println("----------------------------------------------");
        System.out.printf("| %-3d | %-14s | %-5d | \t%-8d |%n",
                studentToUpdate.getId(),
                formatter(studentToUpdate.getName()),
                studentToUpdate.getGrade(),
                studentToUpdate.getScore());
        System.out.println("----------------------------------------------");

        boolean fieldUpdated = false;
        String newName;
        String inputStr;

        while (!fieldUpdated) {
            System.out.print("Enter field to update (name/score): ");
            String field = input.nextLine().trim().toLowerCase();

            switch (field) {
                case "name":
                    System.out.print("Enter new name: ");
                    newName = input.nextLine();

                    if (newName.trim().isEmpty() || !newName.matches("[a-zA-Z ]+") || newName.length() > 50) {
                        System.out.println("Invalid name format.");
                        break;
                    }

                    studentToUpdate.setName(newName);
                    fieldUpdated = true;
                    break;

                case "score":
                    System.out.print("Enter new score (0-100): ");
                    inputStr = input.nextLine();

                    if (!inputStr.matches("\\d+")) {
                        System.out.println("Please enter a valid whole number for the score.");
                        continue;
                    }

                    int newScore = Integer.parseInt(inputStr);

                    if (newScore < 0 || newScore > 100) {
                        System.out.println("Score must be between 0-100.");
                        break;
                    }

                    studentToUpdate.setScore(newScore);
                    fieldUpdated = true;
                    break;

                default:
//                    System.out.println("Invalid field. Please enter (name/score).");
            }

            if (!fieldUpdated) {
                System.out.print("Do you want to try updating again? (y/n): ");
                String retry = input.nextLine().trim().toLowerCase();
                if (retry.equals("n")) {
                    System.out.println("Returning to main menu.");
                    return;
                }
            }
        }

        System.out.print("Are you sure you want to update this student? (y/n): ");
        String confirmation = input.nextLine().trim().toLowerCase();

        if (confirmation.equals("y")) {
            studentMainExtend.updateStudentDetails(studentToUpdate);
        } else {
            System.out.println("Returning to main menu.");
        }
    }


}




