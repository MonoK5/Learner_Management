package org.example;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class StudentData {
    private static final HighSchoolStudent schoolStudent = new HighSchoolStudent();
    private static int idCounter = 1;

    public static String formatter(String name){
        name = name.substring(0,1).toUpperCase().trim() +
                name.substring(1).toLowerCase().trim();

        return  name;
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
          schoolStudent.displayAllStudent();
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
                    System.out.print("Are you sure you want to delete ID " + toDelete.getId() + "? (y/n): ");
                    String confirm = input.nextLine().trim().toLowerCase();
                    // Perform Deletion
                    if (confirm.equals("y")) {
                        boolean success = schoolStudent.deleteStudent(toDelete.getId(), toDelete.getName());
                        if (success) {
                            studentList = schoolStudent.displayAllStudent();
                        } else {
                            System.out.println("Failed to delete student.");
                        }
                    } else {
                        System.out.println("Deletion cancelled.");
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
