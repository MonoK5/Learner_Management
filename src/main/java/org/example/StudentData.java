package org.example;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public final class StudentData {
    private static final HighSchoolStudent schoolStudent = new HighSchoolStudent();
  //  private static int idCounter = 1;

    public static String formatter(String name){
        name = name.substring(0,1).toUpperCase().trim() +
                name.substring(1).toLowerCase().trim();

        return  name;
    }

    static void addStudent(Scanner input) throws SQLException {
        int score = 0;
        String name;
        boolean isValid;
        boolean addNewStud = true;
        String choice;
        int generatedId;
        Random random = new Random();

        System.out.println("\n--- Add Student ---");

        while (addNewStud) {

            do {
                System.out.print("Enter student name (0 to cancel adding student): ");
                name = input.nextLine().trim();
                isValid = true;

                if ("0".equals(name) ) {
                    System.out.println("Returning to main menu.");
                    return;
                }

                if (name.isEmpty()) {
                    System.out.println("Name cannot be empty");
                    isValid = false;
                } else if (!name.matches("[a-zA-Z ]+")) {
                    System.out.println("Only letters and spaces are allowed");
                    isValid = false;
                } else if (name.length() > 50) {
                    System.out.println("Maximum 50 characters are allowed");
                    isValid = false;
                }
            } while (!isValid);

            boolean validScore = false;
            while (!validScore) {
                System.out.print("Enter student score  (0 to cancel adding student): ");
                String scoreInput = input.nextLine().trim();

                if ("0".equals(scoreInput)) {
                    System.out.println("Returning to main menu.");
                    return;
                }

                if (scoreInput.matches("\\d+")) {
                    score = Integer.parseInt(scoreInput);
                    if (score < 0 || score > 100) {
                        System.out.println("Score must be between 0-100.");
                    } else {
                        validScore = true;
                    }
                } else {
                    System.out.println("Invalid input. Please enter a number between 0-100.");
                }
            }

            System.out.print("Are you sure you want to add this student (y/n)?: ");
            choice = input.nextLine().trim().toLowerCase();

            if ("y".equals(choice)) {
                do {
                    generatedId = random.nextInt(1000);
                } while (!schoolStudent.isIdUnique(generatedId));

                Student student = new Student(generatedId, name, score);
                schoolStudent.addNewStudent(student);
            } else {
                System.out.println("Student was not added.");
            }

            System.out.print("Do you want to add another student (y/n)?: ");
            String again = input.nextLine().trim().toLowerCase();
            if (again.equals("n")) {
                addNewStud = false;
                System.out.println("Returning to main menu.");
            }
        }
    }

    // Method Declaration and Variable Initialization
    static void deleteStudent(Scanner input) throws SQLException, NumberFormatException {
        int idToDelete;
        // Method Declaration and Variable Initialization
        List<Student> studentList = schoolStudent.displayAllStudent();
        if (studentList == null || studentList.isEmpty()) {
            System.out.println("No students found in the database.");
            return;
        }
        // Start Deletion Loop
        boolean continueDeleting = true;
        while (continueDeleting) {
            // Display All Students
            displayAllStudents();
            // Ask User If They Want to Delete
            System.out.println("\n\t--- Delete Student ---");
            System.out.print("Do you want to delete a student? (y/n): ");
            String choice = input.nextLine().trim().toLowerCase();
            // Handle y/n Response
            switch (choice) {
                case "y":
                    //  Get Student ID Input
                    System.out.print("Enter student ID to delete: ");
                    String userInput = input.nextLine();

                    if (!userInput.matches("\\d+")) {
                        System.out.println("Enter a valid student ID to remove the record.");
                        continue;
                    }
                    //  Parse ID and Prepare for Deletion
                    idToDelete = Integer.parseInt(userInput);
                    Student toDelete = null;
                    // Search for Student by ID
                    for (Student student : studentList) {
                        if (student.getId() == idToDelete) {
                            toDelete = student;
                            break;
                        }
                    }
                    // Handle Not Found Case
                    if (toDelete == null) {
                        System.out.println("No student found with ID: " + idToDelete);
                        break;
                    }
                    // Confirm Deletion
                    System.out.print("Are you sure you want to delete Student ID: " + toDelete.getId() +
                                    " (" + formatter(toDelete.getName()) + ")? (y/n): ");
                    String confirm = input.nextLine().trim().toLowerCase();
                    // Perform Deletion
                    if ("y".equalsIgnoreCase(confirm)) {
                        boolean success = schoolStudent.deleteStudent(toDelete.getId(), toDelete.getName());
                        if (success) {
                            System.out.println("Student deleted successfully!");
                            studentList = schoolStudent.displayAllStudent();
                        } else {
                            System.out.println("Failed to delete student.");
                        }
                    } else if ("n".equalsIgnoreCase(confirm)) {
                        System.out.println("Deletion cancelled.");
                    } else {
                        System.out.println("Invalid input. Deletion cancelled by default.");
                    }
                    // Check If Student List is Now Empty
                    if (studentList.isEmpty()) {
                        System.out.println("No students left.");
                        continueDeleting = false;
                    }
                    break;
                // Handle n Option
                case "n":
                    System.out.println("Returning to main menu.");
                    continueDeleting = false;
                    break;
                // Handle Invalid Option
                default:
                    System.out.println("Invalid input. Please enter (y/n).");
                    break;
            }
        }
    }


}
